package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Types;

public class LatitudeFilter extends AbstractRangeFilter {

  @JsonCreator
  public LatitudeFilter(@JsonProperty("range") Double[] range) {
    super(null, range, "l", Types.DOUBLE, "listing_latitude");
  }
}
