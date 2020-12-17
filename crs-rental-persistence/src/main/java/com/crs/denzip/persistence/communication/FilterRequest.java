package com.crs.denzip.persistence.communication;

import com.crs.denzip.persistence.filters.Filter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class FilterRequest {

  private final List<Filter> filterList;

  public FilterRequest(@JsonProperty("filterList") List<Filter> filterList) {
    this.filterList = filterList;
  }

}
