package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@ToString
public abstract class AbstractValueFilter<T> implements Filter {

  private static final String IN_QUERY = "%1$s.%2$s in (:%2$s)";

  @JsonProperty
  private final String title;
  @JsonProperty
  private final String filterType = "ValueFilter";
  private final List<T> conditions;
  private final String tableAlias;
  protected final int columnType;
  protected final String columnId;


  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue(this.columnId, new SqlParameterValue(this.columnType, this.conditions));
  }

  @Override
  public String getJoinArguments() {
    return String.format(IN_QUERY, this.tableAlias, this.columnId);
  }

  @Override
  public boolean isValid() {
    return !CollectionUtils.isEmpty(this.conditions);
  }

  public List<T> getConditions() {
    return this.conditions;
  }

}
