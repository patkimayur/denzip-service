package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BachelorAllowedFilter extends AbstractBooleanConditionFilter<FilterCondition> {

  private static final String TABLE_ALIAS = "l";
  private static final String COLUMN_ID = "bachelor_allowed";

  @JsonCreator
  public BachelorAllowedFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("Tenant Preference", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }
}
