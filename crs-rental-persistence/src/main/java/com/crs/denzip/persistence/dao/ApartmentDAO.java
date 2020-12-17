package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.Apartment;
import com.crs.denzip.model.entities.CRSImage;
import com.crs.denzip.model.marshaller.AmenitiesMarshaller;
import com.crs.denzip.model.marshaller.CRSImageMarshaller;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ApartmentDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentDAO.class);


  private static final String GET_ALL_APARTMENTS = "select apartment_id, apartment_name from apartment";

  private static final String GET_APARTMENT_BY_ID = "select a.apartment_id, a.apartment_name, a.apartment_latitude, a.apartment_longitude, "
                                                    + "row_to_json(aa.*) as apartment_amenities from apartment a, apartment_amenity aa "
                                                    + "where a.apartment_id = :apartment_id and a.apartment_id = aa.apartment_id";


  private static final String GET_DEFAULT_APARTMENT_AMENITIES =
      "SELECT json_object_agg (column_name,false) as apartment_amenities  FROM information_schema.columns WHERE  table_name  = 'apartment_amenity'";

  private static final String ADD_APARTMENT = "with inserted as (insert into apartment (apartment_name, apartment_latitude, apartment_longitude) "
                                              + "values (:apartment_name, :apartment_latitude, :apartment_longitude) returning apartment_id ) "
                                              + "insert into apartment_amenity (apartment_id, powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball) "
                                              + "select inserted.apartment_id, :powerbackup, :lift, :security, :playarea, :clubhouse, :stp, :wtp, :visitorparking, :swimmingpool, :gym, :lawntennis, :snooker, :tabletennis, :basketball from inserted "
                                              + "returning apartment_id";

  private static final String UPDATE_APARTMENT = "with updated as (update apartment  set (apartment_name, apartment_latitude, apartment_longitude, last_modified_time) "
                                              + "= (:apartment_name, :apartment_latitude, :apartment_longitude, now()) returning apartment_id ) "
                                              + "update apartment_amenity set (powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball, last_modified_time) "
                                              + "= (select :powerbackup, :lift, :security, :playarea, :clubhouse, :stp, :wtp, :visitorparking, :swimmingpool, :gym, :lawntennis, :snooker, :tabletennis, :basketball, now() from updated) where apartment_id = (select updated.apartment_id from updated) "
                                              + "returning apartment_id";


  private static final String UPDATE_APARTMENT_IMAGES = "update apartment set apartment_images = :apartment_images where apartment_id = :apartment_id";


  private final AmenitiesMarshaller amenitiesMarshaller;
  private final CRSImageMarshaller crsImageMarshaller;


  public List<Apartment> getAllApartments() {
    return super.query(GET_ALL_APARTMENTS, null, new RowMapper<Apartment>() {
      @Override
      public Apartment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Apartment.builder()
                        .apartmentId(rs.getString("apartment_id"))
                        .apartmentName(rs.getString("apartment_name"))
                        .build();
      }
    });
  }

  public Map<String, Boolean> getDefaultApartmentAmenities() {

    return super.queryForObject(GET_DEFAULT_APARTMENT_AMENITIES, new MapSqlParameterSource(), new RowMapper<Map<String, Boolean>>() {

      @Override
      public Map<String, Boolean> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Boolean> listingAmenities = amenitiesMarshaller.unmarshall(rs.getString("apartment_amenities"));
        filterAmenitiesMap(listingAmenities).keySet()
                                            .forEach(key -> listingAmenities.put(key, false));
        return listingAmenities;
      }

    });

  }

  public Apartment getApartment(String apartmentId) {
    if (StringUtils.isEmpty(apartmentId)) {
      return null;
    }

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("apartment_id", apartmentId, Types.OTHER);


    return super.queryForObject(GET_APARTMENT_BY_ID, paramSourceMap, new RowMapper<Apartment>() {
      @Override
      public Apartment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Apartment.builder()
                        .apartmentId(rs.getString("apartment_id"))
                        .apartmentName(rs.getString("apartment_name"))
                        .apartmentLatitude(rs.getDouble("apartment_latitude"))
                        .apartmentLongitude(rs.getDouble("apartment_longitude"))
                        .apartmentAmenities(filterAmenitiesMap(amenitiesMarshaller.unmarshall(rs.getString("apartment_amenities"))))
                        .build();
      }
    });
  }

  public String addApartment(Apartment apartment) {

    MapSqlParameterSource paramSourceMap = mapApartmentToSqlParams(apartment);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(ADD_APARTMENT, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1 && null != keyHolder.getKeys()) {
      return String.valueOf(keyHolder.getKeys()
                                     .get("apartment_id"));
    }

    return null;
  }

  public String updateApartment(Apartment apartment) {


    if(StringUtils.isEmpty(apartment.getApartmentId())) {
      return null;
    }

    MapSqlParameterSource paramSourceMap = mapApartmentToSqlParams(apartment);
    paramSourceMap.addValue("apartment_id", apartment.getApartmentId(), Types.OTHER);


    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(UPDATE_APARTMENT, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1 && null != keyHolder.getKeys()) {
      return String.valueOf(keyHolder.getKeys()
                                     .get("apartment_id"));
    }

    return null;
  }

  private MapSqlParameterSource mapApartmentToSqlParams(Apartment apartment) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("apartment_name", apartment.getApartmentName(), Types.VARCHAR);
    paramSourceMap.addValue("apartment_latitude", apartment.getApartmentLatitude(), Types.DOUBLE);
    paramSourceMap.addValue("apartment_longitude", apartment.getApartmentLongitude(), Types.DOUBLE);

    // Apartment Amenities
    addApartmentAmenityToParamMap("powerbackup", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("lift", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("security", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("playarea", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("clubhouse", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("stp", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("wtp", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("visitorparking", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("swimmingpool", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("gym", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("lawntennis", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("snooker", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("tabletennis", apartment.getApartmentAmenities(), paramSourceMap);
    addApartmentAmenityToParamMap("basketball", apartment.getApartmentAmenities(), paramSourceMap);
    return paramSourceMap;
  }

  private void addApartmentAmenityToParamMap(String amenityKey, Map<String, Boolean> amenities, MapSqlParameterSource paramSourceMap) {
    boolean isPresent = false;
    if (amenities != null && amenities.containsKey(amenityKey)) {
      isPresent = amenities.get(amenityKey);
    }
    paramSourceMap.addValue(amenityKey, isPresent, Types.BOOLEAN);
  }

  private Map<String, Boolean> filterAmenitiesMap(Map<String, Boolean> amenitiesMap) {
    amenitiesMap.remove("created_time");
    amenitiesMap.remove("apartment_id");
    amenitiesMap.remove("last_modified_time");

    return amenitiesMap;
  }

  public int updateApartmentImages(String apartmentId, List<CRSImage> crsImages) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("apartment_id", new SqlParameterValue(Types.OTHER, apartmentId));
    paramSourceMap.addValue("apartment_images", new SqlParameterValue(Types.VARCHAR, crsImageMarshaller.marshall(crsImages)));

    return super.update(UPDATE_APARTMENT_IMAGES, paramSourceMap);
  }

}
