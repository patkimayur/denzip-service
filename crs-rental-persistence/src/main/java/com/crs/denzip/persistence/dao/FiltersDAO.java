package com.crs.denzip.persistence.dao;

import com.crs.denzip.persistence.filters.FilterCondition;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FiltersDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(FiltersDAO.class);

  private static final String DEFAULT_VALUES_QUERY = "select %1$s from %2$s";
  private final LoadingCache<FilterKey, List<FilterCondition>> filterCache = CacheBuilder.newBuilder()
                                                                                         .expireAfterAccess(1, TimeUnit.DAYS)
                                                                                         .recordStats()
                                                                                         .build(new CacheLoader<FilterKey, List<FilterCondition>>() {
                                                                                           @Override
                                                                                           public List<FilterCondition> load(FilterKey filterKey) {
                                                                                             return getDefaultFiltersFromDB(filterKey);
                                                                                           }
                                                                                         });

  public List<FilterCondition> getDefaultFilters(String tableAlias, String columnId) {
    FilterKey filterKey = FilterKey.builder()
                                   .tableAlias(tableAlias)
                                   .columnId(columnId)
                                   .build();
    try {
      return filterCache.get(filterKey);
    } catch (ExecutionException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return getDefaultFiltersFromDB(filterKey);
  }

  private List<FilterCondition> getDefaultFiltersFromDB(FilterKey filterKey) {
    LOGGER.info("Loading Default Filter from DB for {} , {}", filterKey.tableAlias, filterKey.columnId);
    String query = String.format(DEFAULT_VALUES_QUERY, filterKey.columnId, filterKey.tableAlias);
    return super.query(query, null, new RowMapper<FilterCondition>() {
      @Override
      public FilterCondition mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilterCondition.builder()
                              .condition(rs.getString(filterKey.columnId))
                              .build();
      }
    });
  }

  @Data
  @Builder
  private static class FilterKey {
    private String tableAlias;
    private String columnId;
  }
}
