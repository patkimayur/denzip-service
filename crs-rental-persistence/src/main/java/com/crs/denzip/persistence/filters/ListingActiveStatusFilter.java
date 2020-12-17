package com.crs.denzip.persistence.filters;

import java.util.List;

public class ListingActiveStatusFilter extends AbstractBooleanConditionFilter<FilterCondition> {

  private static final String TABLE_ALIAS = "l";
  private static final String COLUMN_ID = "listing_active_ind";

  public ListingActiveStatusFilter(List<FilterCondition> conditions) {
    super("Listing Status", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }
}
