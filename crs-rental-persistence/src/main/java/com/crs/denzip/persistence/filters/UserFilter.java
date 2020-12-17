package com.crs.denzip.persistence.filters;

import com.crs.denzip.model.entities.Listing;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.StringUtils;

@ToString
public class UserFilter implements Filter {

  private static final String SCHEDULE_STATUS_COLUMN = "schedule_status";
  private static final String SELECT_USER_STATUS_QUERY =
      "(select schedule_status from listing_user_schedule_mapping where l.listing_id = listing_user_schedule_mapping.listing_id and listing_user_schedule_mapping.user_id = '%1$s' )";

  @JsonProperty
  private final String userId;

  @JsonCreator
  public UserFilter(@JsonProperty("userId") String userId) {
    this.userId = userId;
  }

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {

  }

  @Override
  public String getJoinArguments() {
    return EMPTY_STRING;
  }

  @Override
  public boolean isValid() {
    return !StringUtils.isEmpty(this.userId);
  }

  @Override
  public String getSelectColumn() {
    return SCHEDULE_STATUS_COLUMN;
  }

  @Override
  public String getSelectColumnQuery() {
    return String.format(SELECT_USER_STATUS_QUERY, this.userId);
  }

  @Override
  public String getSelectTable() {
    return EMPTY_STRING;
  }

  @Override
  public void enrichData(Listing listing, String value) {
    listing.setCurrentLoggedInUserStatus(value);
  }

}
