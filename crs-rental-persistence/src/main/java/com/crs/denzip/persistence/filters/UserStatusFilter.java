package com.crs.denzip.persistence.filters;

import com.crs.denzip.model.entities.Listing;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Types;
import java.util.List;

@ToString
public class UserStatusFilter implements Filter {

  private static final String USER_ID_COLUMN = "user_id";
  private static final String SCHEDULE_STATUS_COLUMN = "schedule_status";
  private static final String LISTING_USER_SCHEDULE_MAPPING_TABLE = "listing_user_schedule_mapping";
  private static final String QUERY =
      "l.listing_id = listing_user_schedule_mapping.listing_id and listing_user_schedule_mapping.user_id = :user_id and listing_user_schedule_mapping.schedule_status in (:schedule_status)";

  @JsonProperty
  private final String userId;
  @JsonProperty
  private final List<String> userStatus;

  @JsonCreator
  public UserStatusFilter(@JsonProperty("userId") String userId, @JsonProperty("userStatus") List<String> userStatus) {
    this.userId = userId;
    this.userStatus = userStatus;
  }

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, this.userId));
    paramSourceMap.addValue(SCHEDULE_STATUS_COLUMN, new SqlParameterValue(Types.OTHER, this.userStatus));
  }

  @Override
  public String getJoinArguments() {
    return QUERY;
  }

  @Override
  public boolean isValid() {
    return !StringUtils.isEmpty(this.userId) && !CollectionUtils.isEmpty(this.userStatus);
  }

  @Override
  public String getSelectColumn() {
    return SCHEDULE_STATUS_COLUMN;
  }

  @Override
  public String getSelectColumnQuery() {
    return SCHEDULE_STATUS_COLUMN;
  }

  @Override
  public String getSelectTable() {
    return LISTING_USER_SCHEDULE_MAPPING_TABLE;
  }

  @Override
  public void enrichData(Listing listing, String value) {
    listing.setCurrentLoggedInUserStatus(value);
  }

}
