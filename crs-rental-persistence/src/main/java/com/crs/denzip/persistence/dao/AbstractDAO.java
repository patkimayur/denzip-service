package com.crs.denzip.persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

public abstract class AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);
  private static final String LOGGER_MESSAGE = "Executing query : {}";


  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  protected int update(String sql, SqlParameterSource paramSource) {
    LOGGER.debug(LOGGER_MESSAGE, sql);
    return namedParameterJdbcTemplate.update(sql, paramSource);
  }

  protected int update(String sql, SqlParameterSource paramSource, KeyHolder keyHolder) {
    LOGGER.debug(LOGGER_MESSAGE, sql);
    return namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
  }

  protected int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
    LOGGER.debug(LOGGER_MESSAGE, sql);
    return namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
  }

  protected <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {
    LOGGER.debug(LOGGER_MESSAGE, sql);
    return namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
  }

  protected <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {
    LOGGER.debug(LOGGER_MESSAGE, sql);
    return namedParameterJdbcTemplate.query(sql, paramSource, rowMapper);
  }

}
