package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BedroomCountFilter extends AbstractStringConditionFilter<FilterCondition> {


  private static final String TABLE_ALIAS = "listing_bedroom_count";
  private static final String COLUMN_ID = "bedroom_count_name";

  @JsonCreator
  public BedroomCountFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("No. of Bedrooms", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }

}
