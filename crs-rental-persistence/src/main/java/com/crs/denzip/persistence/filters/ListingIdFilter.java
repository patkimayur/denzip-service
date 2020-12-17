package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Types;
import java.util.List;

public class ListingIdFilter extends AbstractValueFilter<String> {

  @JsonCreator
  public ListingIdFilter(@JsonProperty("values") List<String> values) {
    super(null, values, "l", Types.OTHER, "listing_id");
  }

}
