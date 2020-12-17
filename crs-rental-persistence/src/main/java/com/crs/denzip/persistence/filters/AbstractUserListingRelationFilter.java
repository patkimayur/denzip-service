package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Types;

@AllArgsConstructor
@ToString
public class AbstractUserListingRelationFilter implements Filter {

  private static final String ANY_QUERY = "l.listing_id in (select listing_id from %1$s where user_id = :user_id)";

  @JsonProperty
  private final String title;
  @JsonProperty
  private final String filterType = "UserListingRelationFilter";
  private final String mappingTableName;
  private final String userId;

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.BIGINT, this.userId));
  }

  @Override
  public String getJoinArguments() {
    return String.format(ANY_QUERY, this.mappingTableName);
  }

  @Override
  public boolean isValid() {
    return this.userId != null && this.mappingTableName != null;
  }

}
