package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FurnishingTypeFilter extends AbstractStringConditionFilter<FilterCondition> {


  private static final String TABLE_ALIAS = "listing_furnishing_type";
  private static final String COLUMN_ID = "furnishing_type_name";

  @JsonCreator
  public FurnishingTypeFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("Furnishing", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }

}
