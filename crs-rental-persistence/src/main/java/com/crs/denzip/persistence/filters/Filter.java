package com.crs.denzip.persistence.filters;

import com.crs.denzip.model.entities.Listing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;


@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Filter {

  String EMPTY_STRING = "";

  void addParams(final MapSqlParameterSource paramSourceMap);

  @JsonIgnore
  String getJoinArguments();

  @JsonIgnore
  boolean isValid();

  @JsonIgnore
  default String getSelectColumn() {
    return EMPTY_STRING;
  }

  @JsonIgnore
  default String getSelectColumnQuery() {
    return EMPTY_STRING;
  }

  @JsonIgnore
  default String getSelectTable() {
    return EMPTY_STRING;
  }

  @JsonIgnore
  default void enrichData(Listing listing, String value) {

  }

}
