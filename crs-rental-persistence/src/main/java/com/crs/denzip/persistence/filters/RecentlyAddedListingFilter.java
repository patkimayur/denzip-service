package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@ToString
public class RecentlyAddedListingFilter implements Filter {

  private static final String QUERY = "l.created_time > now() - interval '%s'";

  @JsonProperty
  private final String daysLimit;

  @JsonCreator
  public RecentlyAddedListingFilter(@JsonProperty("daysLimit") String daysLimit) {
    this.daysLimit = daysLimit;
  }

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {

  }

  @Override
  public String getJoinArguments() {
    return String.format(QUERY,this.daysLimit);
  }

  @Override
  public boolean isValid() {
    return StringUtils.isNotEmpty(this.daysLimit);
  }
}
