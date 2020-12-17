package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PropertyTypeFilter extends AbstractStringConditionFilter<FilterCondition> {


  private static final String TABLE_ALIAS = "listing_property_type";
  private static final String COLUMN_ID = "property_type_name";

  @JsonCreator
  public PropertyTypeFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("Property Type", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }

}
