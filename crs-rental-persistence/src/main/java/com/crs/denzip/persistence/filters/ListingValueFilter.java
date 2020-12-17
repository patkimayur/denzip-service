package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Types;

public class ListingValueFilter extends AbstractRangeFilter {

  private static final double MIN_VALUE = 0;
  private static final double MAX_VALUE = 100000000;

  @JsonCreator
  public ListingValueFilter(@JsonProperty("range") Double[] range) {
    super("Property Value", range, "l", Types.DOUBLE, "listing_value");
  }

  public static double getMinValue() {
    return MIN_VALUE;
  }

  public static double getMaxValue() {
    return MAX_VALUE;
  }

  @Override
  public boolean isValid() {
    return super.isValid() && (this.minValue > MIN_VALUE || this.maxValue < MAX_VALUE);
  }
}
