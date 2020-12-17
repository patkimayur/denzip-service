package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Types;

@ToString
public class AreaFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AreaFilter.class);
  private static final String IN_QUERY = "%1$s.%2$s between :%3$s and :%4$s";
  private static final double DEFAULT_PROXIMITY = 3;

  //https://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-km-distance
  private static final double LATITUDE_CONST = 1 / 110.574;
  private static final double LONGITUDE_CONST = 1 / 111.320;

  @JsonProperty
  private final double latitude;
  @JsonProperty
  private final double longitude;
  @JsonProperty
  private final double proximity;

  private double latitude_min_value;
  private double latitude_max_value;
  private double longitude_min_value;
  private double longitude_max_value;


  @JsonCreator
  public AreaFilter(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude, @JsonProperty("proximity") double proximity) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.proximity = proximity != 0 ? proximity : DEFAULT_PROXIMITY;

    initializeArea();

    LOGGER.debug("Created AreaFilter with values: {}", this);
  }

  private void initializeArea() {

    this.latitude_min_value = this.latitude - (LATITUDE_CONST * this.proximity);
    this.latitude_max_value = this.latitude + (LATITUDE_CONST * this.proximity);

    this.longitude_min_value = this.longitude - ((LONGITUDE_CONST * this.proximity) / Math.cos(Math.toRadians(this.latitude)));
    this.longitude_max_value = this.longitude + ((LONGITUDE_CONST * this.proximity) / Math.cos(Math.toRadians(this.latitude)));

  }


  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue("latitude_min_value", new SqlParameterValue(Types.DOUBLE, this.latitude_min_value));
    paramSourceMap.addValue("latitude_max_value", new SqlParameterValue(Types.DOUBLE, this.latitude_max_value));

    paramSourceMap.addValue("longitude_min_value", new SqlParameterValue(Types.DOUBLE, this.longitude_min_value));
    paramSourceMap.addValue("longitude_max_value", new SqlParameterValue(Types.DOUBLE, this.longitude_max_value));

  }

  @Override
  public String getJoinArguments() {
    String latitudeQuery = String.format(IN_QUERY, "l", "listing_latitude", "latitude_min_value", "latitude_max_value");
    String longitudeQuery = String.format(IN_QUERY, "l", "listing_longitude", "longitude_min_value", "longitude_max_value");
    return latitudeQuery.concat(" and ")
                        .concat(longitudeQuery);
  }

  @Override
  public boolean isValid() {
    return this.latitude != 0 && this.longitude != 0 && this.proximity > 0;
  }
}
