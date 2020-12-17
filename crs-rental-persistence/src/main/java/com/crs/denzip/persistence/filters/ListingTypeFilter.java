package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListingTypeFilter extends AbstractStringConditionFilter<FilterCondition> {


  private static final String TABLE_ALIAS = "listing_type";
  private static final String COLUMN_ID = "listing_type_name";

  @JsonCreator
  public ListingTypeFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("Rent / Buy", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }

}
