package com.crs.denzip.core.service;

import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.persistence.dao.FiltersDAO;
import com.crs.denzip.persistence.filters.BachelorAllowedFilter;
import com.crs.denzip.persistence.filters.BedroomCountFilter;
import com.crs.denzip.persistence.filters.Filter;
import com.crs.denzip.persistence.filters.FilterCondition;
import com.crs.denzip.persistence.filters.FurnishingTypeFilter;
import com.crs.denzip.persistence.filters.ListingTypeFilter;
import com.crs.denzip.persistence.filters.ListingValueFilter;
import com.crs.denzip.persistence.filters.NonVegAllowedFilter;
import com.crs.denzip.persistence.filters.PropertyTypeFilter;
import com.crs.denzip.persistence.filters.TransactionModeFilter;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Transactional(rollbackFor={CRSException.class})
public class FilterService {

  private FiltersDAO filtersDAO;

  public List<Filter> getFilters() {
    List<Filter> filterList = new ArrayList<>();

    filterList.add(new ListingValueFilter(new Double[]{ListingValueFilter.getMinValue(), ListingValueFilter.getMaxValue()}));

    filterList.add(new PropertyTypeFilter(filtersDAO.getDefaultFilters(PropertyTypeFilter.getTableAlias(), PropertyTypeFilter.getColumnId())));

    filterList.add(new FurnishingTypeFilter(filtersDAO.getDefaultFilters(FurnishingTypeFilter.getTableAlias(), FurnishingTypeFilter.getColumnId())));

    filterList.add(new TransactionModeFilter(filtersDAO.getDefaultFilters(TransactionModeFilter.getTableAlias(), TransactionModeFilter.getColumnId())));

    filterList.add(new NonVegAllowedFilter(Collections.singletonList(FilterCondition.builder()
                                                                                    .condition("Non-Veg Allowed")
                                                                                    .build())));

    filterList.add(new BachelorAllowedFilter(Collections.singletonList(FilterCondition.builder()
                                                                                      .condition("Bachelors Allowed")
                                                                                      .build())));

    return filterList;
  }

  public Filter getBedroomCountFilter() {
    return new BedroomCountFilter(filtersDAO.getDefaultFilters(BedroomCountFilter.getTableAlias(), BedroomCountFilter.getColumnId()));
  }

  public Filter getListingTypeFilter() {
    return new ListingTypeFilter(filtersDAO.getDefaultFilters(ListingTypeFilter.getTableAlias(), ListingTypeFilter.getColumnId()));
  }

}
