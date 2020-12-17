package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.BrokerUserNotification;
import com.crs.denzip.model.entities.ListingNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NotificationDAO extends AbstractDAO {

  private static final String ADD_LISTING_NOTIFICATION = "insert into listing_notification_status (listing_id) values (:listing_id) ON CONFLICT (listing_id) DO NOTHING";

  private static final String UPDATE_LISTING_NOTIFICATION = "update listing_notification_status set notification_sent = true where listing_id in (:listing_id)";

  private static final String GET_PENDING_LISTING_NOTIFICATIONS =
      "select crs_user.user_id, crs_user.user_name, crs_user.user_mobile, crs_user.user_email, "
      + "array_agg(listing_tag_mapping.listing_id) as listing_ids "
      + "from listing_notification_status, "
      + "listing_tag_mapping left join user_tag_mapping on listing_tag_mapping.tag_id = user_tag_mapping.tag_id "
      + "left join crs_user on user_tag_mapping.user_id = crs_user.user_id "
      + "where "
      + "listing_notification_status.notification_sent = false "
      + "and listing_notification_status.listing_id = listing_tag_mapping.listing_id "
      + "group by crs_user.user_id";


  private static final String GET_PENDING_BROKER_USER_NOTIFICATIONS =
      "select broker_org_details.broker_org_id, broker_org_details.broker_org_name,  "
      + "broker_org_details.broker_org_mobile, broker_org_details.broker_org_email, "
      + "array_agg(tag.tag_name) as tag_names, array_agg(user_tag_mapping.user_id) as user_ids "
      + "from user_notification_status, user_tag_mapping, broker_org_tag_mapping, broker_org_details, tag "
      + "where user_notification_status.notification_sent = false "
      + "and user_notification_status.user_id = user_tag_mapping.user_id "
      + "and user_tag_mapping.tag_id = broker_org_tag_mapping.tag_id "
      + "and broker_org_tag_mapping.broker_org_id = broker_org_details.broker_org_id "
      + "and broker_org_tag_mapping.tag_id = tag.tag_id "
      + "group by broker_org_details.broker_org_id";


  private static final String ADD_USER_NOTIFICATION = "insert into user_notification_status (user_id) values (:user_id) ON CONFLICT (user_id) DO UPDATE SET notification_sent = false";

  private static final String UPDATE_USER_NOTIFICATION = "update user_notification_status set notification_sent = true where user_id in (:user_id)";


  Logger LOGGER = LoggerFactory.getLogger(NotificationDAO.class);

  public boolean addListingNotification(String listingId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("listing_id", new SqlParameterValue(Types.OTHER, listingId));

    int result = super.update(ADD_LISTING_NOTIFICATION, paramSourceMap);

    if (result == 1) {
      LOGGER.debug("Notification added for listingId {}", listingId);
      return true;
    }

    return false;
  }

  public List<ListingNotification> getPendingListingNotifications() {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    return super.query(GET_PENDING_LISTING_NOTIFICATIONS, paramSourceMap, new RowMapper<ListingNotification>() {
      @Override
      public ListingNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
        String userId = null;
        if (rs.getObject("user_id") != null) {
          userId = String.valueOf(rs.getObject("user_id"));
        }
        return ListingNotification.builder()
            .userId(userId)
            .userName(rs.getString("user_name"))
            .userEmail(rs.getString("user_email"))
            .userMobile(rs.getString("user_mobile"))
            .listingIds(buildListingIds(rs))
            .build();
      }
    });
  }

  private List<String> buildListingIds(ResultSet rs) throws SQLException {
    if(rs.getArray("listing_ids") != null) {
      Object listingIds = rs.getArray("listing_ids").getArray();
      if(Objects.nonNull(listingIds)) {
        return new ArrayList<>(Arrays.asList((Object[])listingIds).stream()
            .map(listingId -> String.valueOf(listingId)).collect(Collectors.toList()));
      }
    }
    return null;
  }

  public boolean updateListingNotification(List<String> listingIdList) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("listing_id", new SqlParameterValue(Types.OTHER, listingIdList));

    int result = super.update(UPDATE_LISTING_NOTIFICATION, paramSourceMap);

    if (result > 0) {
      LOGGER.debug("Notification status updated for listingIds {}", listingIdList);
      return true;
    }

    return false;
  }


  public boolean addUserNotification(String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userId));

    int result = super.update(ADD_USER_NOTIFICATION, paramSourceMap);

    if (result == 1) {
      LOGGER.debug("Notification added for userId {}", userId);
      return true;
    }

    return false;
  }

  public List<BrokerUserNotification> getPendingBrokerUserNotifications() {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    return super.query(GET_PENDING_BROKER_USER_NOTIFICATIONS, paramSourceMap, new RowMapper<BrokerUserNotification>() {
      @Override
      public BrokerUserNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BrokerUserNotification.builder()
            .brokerOrgId(String.valueOf(rs.getObject("broker_org_id")))
            .brokerOrgName(rs.getString("broker_org_name"))
            .brokerOrgEmail(rs.getString("broker_org_email"))
            .brokerOrgMobile(rs.getString("broker_org_mobile"))
            .tagNames(buildTagNames(rs))
            .userIds(buildUserIds(rs))
            .build();
      }
    });
  }

  private List<String> buildTagNames(ResultSet rs) throws SQLException {
    if(rs.getArray("tag_names") != null) {
      Object listingIds = rs.getArray("tag_names").getArray();
      if(Objects.nonNull(listingIds)) {
        return new ArrayList<>(Arrays.asList((String[])listingIds));
      }
    }
    return null;
  }

  private List<String> buildUserIds(ResultSet rs) throws SQLException {
    if(rs.getArray("user_ids") != null) {
      Object userIds = rs.getArray("user_ids").getArray();
      if(Objects.nonNull(userIds)) {
        return new ArrayList<>(Arrays.asList((Object[])userIds).stream()
            .map(listingId -> String.valueOf(listingId)).collect(Collectors.toList()));
      }
    }
    return null;
  }

  public boolean updateUserNotification(List<String> userIdList) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userIdList));

    int result = super.update(UPDATE_USER_NOTIFICATION, paramSourceMap);

    if (result > 0) {
      LOGGER.debug("Notification status updated for userIds {}", userIdList);
      return true;
    }

    return false;
  }
}
