package com.crs.denzip.persistence.filters;


import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractStringConditionFilter<T extends FilterCondition> extends AbstractValueFilter<T> {

  protected final List<String> appliedConditions;

  public AbstractStringConditionFilter(String title, List<T> conditions, String tableAlias, String columnId) {
    super(title, conditions, tableAlias, Types.VARCHAR, columnId);
    this.appliedConditions = conditions.stream()
                                       .filter(Objects::nonNull)
                                       .filter(T::isApplied)
                                       .map(T::getCondition)
                                       .filter(Objects::nonNull)
                                       .collect(Collectors.toList());
  }

  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    paramSourceMap.addValue(this.columnId, new SqlParameterValue(super.columnType, this.appliedConditions));
  }

  @Override
  public boolean isValid() {
    return !CollectionUtils.isEmpty(this.appliedConditions) && this.appliedConditions.size() != super.getConditions()
                                                                                                     .size();
  }

}
