package com.crs.denzip.persistence.filters;


import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Types;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBooleanConditionFilter<T extends FilterCondition> extends AbstractValueFilter<T> {

  private final boolean anyConditionApplied;

  public AbstractBooleanConditionFilter(String title, List<T> conditions, String tableAlias, String columnId) {
    super(title, conditions, tableAlias, Types.BOOLEAN, columnId);
    this.anyConditionApplied = conditions.stream()
                                         .filter(Objects::nonNull)
                                         .map(T::isApplied)
                                         .findFirst()
                                         .orElse(false);
  }

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue(this.columnId, new SqlParameterValue(this.columnType, this.anyConditionApplied));
  }

  @Override
  public boolean isValid() {
    return anyConditionApplied;
  }

}
