package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.BrokerOrg;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BrokerOrgDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(BrokerOrgDAO.class);

  private static final String GET_BROKER_ORG_DETAILS_BY_USER =
      "select broker_org_details.broker_org_id, broker_org_name, broker_org_address, broker_org_mobile, broker_org_email "
          + "from broker_org_details, broker_org_user_mapping, crs_user "
          + "where crs_user.user_id = :user_id "
          + "and crs_user.user_id = broker_org_user_mapping.user_id "
          + "and broker_org_user_mapping.broker_org_id = broker_org_details.broker_org_id;";

  private static final String ADD_BROKER_ORG = "insert into broker_org_details (broker_org_name, broker_org_address, broker_org_mobile, broker_org_email) "
      + "values (:broker_org_name, :broker_org_address, :broker_org_mobile, :broker_org_email) returning broker_org_id";

  private static final String BROKER_ORG_TAG_INSERT = "insert into broker_org_tag_mapping (broker_org_id, tag_id) values (:broker_org_id, :tag_id)  ON CONFLICT (broker_org_id, tag_id) DO NOTHING";

  private static final String BROKER_ORG_MAPPING_INSERT = "insert into broker_org_user_mapping (broker_org_id, user_id) values ((select broker_org_id from broker_org_details where broker_org_mobile = :broker_org_mobile), (select user_id from crs_user where user_mobile = :user_mobile))  ON CONFLICT (broker_org_id, user_id) DO NOTHING";



  public BrokerOrg getBrokerOrgByUserId(String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_id", userId, Types.OTHER);

    return super.queryForObject(GET_BROKER_ORG_DETAILS_BY_USER, paramSourceMap, new RowMapper<BrokerOrg>() {
      @Override
      public BrokerOrg mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BrokerOrg.builder()
            .brokerOrgId(String.valueOf(rs.getObject("broker_org_id")))
            .brokerOrgName(rs.getString("broker_org_name"))
            .brokerOrgAddress(rs.getString("broker_org_address"))
            .brokerOrgMobile(rs.getString("broker_org_mobile"))
            .brokerOrgEmail(rs.getString("broker_org_email"))
            .build();
      }
    });
  }

  public boolean addBrokerOrg(BrokerOrg brokerOrg) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("broker_org_name", brokerOrg.getBrokerOrgName(), Types.VARCHAR);
    paramSourceMap.addValue("broker_org_address", brokerOrg.getBrokerOrgAddress(), Types.VARCHAR);
    paramSourceMap.addValue("broker_org_mobile", brokerOrg.getBrokerOrgMobile(), Types.VARCHAR);
    paramSourceMap.addValue("broker_org_email", brokerOrg.getBrokerOrgEmail(), Types.VARCHAR);


    KeyHolder keyHolder = new GeneratedKeyHolder();
    String brokerOrgId = null;

    int result = super.update(ADD_BROKER_ORG, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1) {
      brokerOrgId = String.valueOf(keyHolder.getKeys()
          .get("broker_org_id"));
    } else {
      return false;
    }

    if (StringUtils.isNotBlank(brokerOrgId)) {
      List<String> tags = null;
      if(StringUtils.isNotBlank(brokerOrg.getTags())){
        tags = Arrays.asList(brokerOrg.getTags().split(","));
      }

      if (CollectionUtils.isEmpty(tags)) {
        return false;
      }

      List<Map<String, Object>> batchValues = new ArrayList<>(tags.size());
      for (String tagId : tags) {
        batchValues.add(
            new MapSqlParameterSource("broker_org_id", new SqlParameterValue(Types.OTHER, brokerOrgId))
                .addValue("tag_id", new SqlParameterValue(Types.OTHER, tagId))
                .getValues());
      }

      int[] updateCounts = super.batchUpdate(BROKER_ORG_TAG_INSERT, batchValues.toArray(new Map[tags.size()]));
      LOGGER.debug("Result of updating user {} with tags: {}", brokerOrgId, updateCounts);

      if (ArrayUtils.isEmpty(updateCounts)) {
        return false;
      }

      if (tags.size() == updateCounts.length) {
        return true;
      }

      return false;

    }

    return false;
  }

  public boolean addBrokerOrgMapping(String brokerOrgMobile, List<String> brokerMobileNumbers) {

    if (CollectionUtils.isEmpty(brokerMobileNumbers)) {
      return false;
    }

    List<Map<String, Object>> batchValues = new ArrayList<>(brokerMobileNumbers.size());
    for (String brokerMobNum : brokerMobileNumbers) {
      batchValues.add(
          new MapSqlParameterSource("broker_org_mobile", new SqlParameterValue(Types.OTHER, brokerOrgMobile))
              .addValue("user_mobile", new SqlParameterValue(Types.OTHER, brokerMobNum))
              .getValues());
    }

    int[] updateCounts = super.batchUpdate(BROKER_ORG_MAPPING_INSERT, batchValues.toArray(new Map[brokerMobileNumbers.size()]));
    LOGGER.debug("Result of updating broker org mobile {} with broker mobile: {}", brokerOrgMobile, updateCounts);

    if (ArrayUtils.isEmpty(updateCounts)) {
      return false;
    }

    if (brokerMobileNumbers.size() == updateCounts.length) {
      return true;
    }

    return false;

  }
}
