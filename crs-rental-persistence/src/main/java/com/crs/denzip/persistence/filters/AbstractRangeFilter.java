package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@ToString
public abstract class AbstractRangeFilter implements Filter {
  private static final String IN_QUERY = "%1$s.%2$s between :%3$s and :%4$s";
  private static final String MIN_VALUE = "MIN_VALUE";
  private static final String MAX_VALUE = "MAX_VALUE";

  @JsonProperty
  private final String title;
  @JsonProperty
  private final String filterType = "RangeFilter";
  @JsonProperty
  private final Double[] range;
  protected final Double minValue;
  protected final Double maxValue;
  private final String tableAlias;
  private final int columnType;
  private final String columnId;

  public AbstractRangeFilter(String title, Double[] range, String tableAlias, int columnType, String columnId) {
    if (range == null || range.length != 2) {
      throw new IllegalArgumentException(String.format("Range Filters range expected length: 2, got: %s", range != null ? range.length : null));
    }

    this.title = title;
    this.range = range;
    this.minValue = range[0];
    this.maxValue = range[1];
    this.tableAlias = tableAlias;
    this.columnType = columnType;
    this.columnId = columnId;
  }

  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue(MIN_VALUE, new SqlParameterValue(this.columnType, this.minValue));
    paramSourceMap.addValue(MAX_VALUE, new SqlParameterValue(this.columnType, this.maxValue));

  }

  @Override
  public String getJoinArguments() {
    return String.format(IN_QUERY, this.tableAlias, this.columnId, MIN_VALUE, MAX_VALUE);
  }

  @Override
  public boolean isValid() {
    return this.minValue != null && this.maxValue != null && this.minValue <= this.maxValue;
  }
}
