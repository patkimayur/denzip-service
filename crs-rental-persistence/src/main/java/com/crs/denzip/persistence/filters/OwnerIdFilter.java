package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Types;
import java.util.List;

public class OwnerIdFilter extends AbstractValueFilter<String> {

  @JsonCreator
  public OwnerIdFilter(@JsonProperty("values") List<String> values) {
    super(null, values, "l", Types.OTHER, "user_id");
  }

}
