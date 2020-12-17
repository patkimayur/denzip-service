package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.constants.ItineraryStatusConstants;
import com.crs.denzip.model.constants.ScheduleStatusConstants;
import com.crs.denzip.model.entities.Authority;
import com.crs.denzip.model.entities.User;
import com.crs.denzip.model.entities.UserPrefVisitSlot;
import com.crs.denzip.model.entities.UserSchedule;
import com.crs.denzip.model.exception.InvalidAuthorityException;
import com.crs.denzip.model.marshaller.UserPrefVisitSlotMarshaller;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class UserDAO extends AbstractDAO {

  private static final String ADD_UPDATE_LISTING_USER_MAPPING =
      "insert into listing_user_schedule_mapping (listing_id, user_id, schedule_status) values (:listing_id, :user_id, :schedule_status) ON CONFLICT (listing_id, user_id) DO UPDATE SET schedule_status = :schedule_status";


  private static final String UPDATE_POTENTIAL_PREF_SLOT =
      "update listing_user_schedule_mapping set (schedule_itinerary_status, potential_itinerary) = (:schedule_itinerary_status, cast(:potential_itinerary as json)) where listing_id=:listing_id and user_id =:user_id";

  private static final String UPDATE_ITINERARY_STATUS =
      "update listing_user_schedule_mapping set schedule_itinerary_status = :schedule_itinerary_status where listing_id=:listing_id and user_id =:user_id";


  private static final String RETRIEVE_USER_SCHEDULE =
      "SELECT owner_id, listing_id, tenant_id, potential_itinerary, tenant_pref_visit_slot, owner_pref_visit_slot FROM tenant_listing_schedule where schedule_itinerary_status = :schedule_itinerary_status";

  private static final String RETRIEVE_USER_COUNT =
      "select count(1) from crs_user";


  private static final String REMOVE_LISTING_USER_MAPPING =
      "delete from listing_user_schedule_mapping where listing_id =  :listing_id and user_id = :user_id and schedule_status = :schedule_status";

  private static final String UPDATE_ALL_LISTING_FOR_USER =
      "update listing_user_schedule_mapping set schedule_status = :schedule_status_schedule_request where user_id = :user_id and schedule_status = :schedule_status_shortlisted";

  private static final String ADD_UPDATE_USER =
      "insert into crs_user (user_name, user_mobile, user_email, authorities, user_secret, user_facebook_id, user_google_id) values (:user_name, :user_mobile, :user_email, :authorities, :user_secret, :user_facebook_id, :user_google_id) "
          + "ON CONFLICT (user_mobile) DO UPDATE SET "
          + "user_name = COALESCE(:user_name, crs_user.user_name), "
          + "user_email  = COALESCE(:user_email, crs_user.user_email), "
          + "user_facebook_id  = COALESCE(:user_facebook_id, crs_user.user_facebook_id), "
          + "user_google_id  = COALESCE(:user_google_id, crs_user.user_google_id)";

  private static final String RETRIEVE_USER_WITH_PHONE =
      "select user_id, authorities, user_secret, user_name, user_mobile, user_email, user_acceptance_doc, user_govt_id_no, user_govt_id_image, user_pref_comm_mode, user_pref_comm_freq, user_pref_visit_slot, user_facebook_id, user_google_id from crs_user where user_mobile= :user_mobile;";

  private static final String RETRIEVE_USER_WITH_FACEBOOK_ID =
      "select user_id, authorities, user_secret, user_name, user_mobile, user_email, user_acceptance_doc, user_govt_id_no, user_govt_id_image, user_pref_comm_mode, user_pref_comm_freq, user_pref_visit_slot, user_facebook_id, user_google_id from crs_user where user_facebook_id= :user_facebook_id";

  private static final String RETRIEVE_USER_WITH_GOOGLE_ID =
      "select user_id, authorities, user_secret, user_name, user_mobile, user_email, user_acceptance_doc, user_govt_id_no, user_govt_id_image, user_pref_comm_mode, user_pref_comm_freq, user_pref_visit_slot, user_facebook_id, user_google_id from crs_user where user_google_id= :user_google_id";

  private static final String RETRIEVE_USER_WITH_USER_ID =
      "select user_id, authorities, user_secret, user_name, user_mobile, user_email, user_acceptance_doc, user_govt_id_no, user_govt_id_image, user_pref_comm_mode, user_pref_comm_freq, user_pref_visit_slot, user_facebook_id, user_google_id from crs_user where user_id= :user_id";

  private static final String RETRIEVE_AUTHORITY = "select id, name from authority where id= :id";

  private static final String UPDATE_AUTHORITIES =
      "update crs_user set authorities = :authorities where user_id = (select user_id from crs_user where user_mobile = :user_mobile)";

  private static final String UPDATE_USER_PREF_VISIT_SLOT =
      "update crs_user set user_pref_visit_slot = cast(:user_pref_visit_slot as json) where user_id = :user_id";

  private static final String REFRESH_USER_SCHEDULE_VIEW =
      "REFRESH MATERIALIZED VIEW tenant_listing_schedule";

  private static final String GET_USER_PREF_VISIT_SLOT = "select user_pref_visit_slot from crs_user where user_id = :user_id";

  private static final String ADD_USER_REQUEST_CALLBACK = "insert into user_request_callback (phone_no) values (:phone_no)";

  private static final String ADD_PROSPECTIVE_LISTING_USER_MAPPING =
      "insert into listing_user_schedule_mapping (listing_id, user_id, schedule_status) values (:listing_id, :user_id, :schedule_status) ON CONFLICT (listing_id, user_id) DO NOTHING";


  private static final String GET_LISTING_OWNER_DETAIL = "select crs_user.user_id, crs_user.user_mobile, crs_user.user_name from listing, crs_user "
                                                         + "where listing_id = :listing_id "
                                                         + "and crs_user.user_mobile like '%' || coalesce (broker_listing_owner_mobile, (select user_mobile from crs_user where crs_user.user_id = listing.user_id)) || '%' "
                                                         + "and crs_user.user_id not in (select distinct user_id from broker_org_user_mapping where user_id != '60f62b81-e43d-4aa8-8f9d-db292556e5de') "
                                                         + "and listing_active_ind = true "
                                                         + "and listing_virtual_presence = true";

  private static final String LISTING_ID_COLUMN = "listing_id";
  private static final String USER_ID_COLUMN = "user_id";
  private static final String SCHEDULE_STATUS_COLUMN = "schedule_status";

  private static final String USER_PREF_VISIT_SLOT_COLUMN = "user_pref_visit_slot";
  private static final String USER_NAME_COLUMN = "user_name";
  private static final String USER_MOBILE_COLUMN = "user_mobile";
  private static final String USER_FACEBOOK_COLUMN = "user_facebook_id";
  private static final String USER_GOOGLE_COLUMN = "user_google_id";
  private static final String USER_EMAIL_COLUMN = "user_email";
  private static final String USER_AUTHORITY_COLUMN = "authorities";
  private static final String USER_SECRET_COLUMN = "user_secret";
  private static final String AUTHORITY_ID_COLUMN = "id";
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);


  private final UserPrefVisitSlotMarshaller userPrefVisitSlotMarshaller;


  public int addUserListingMapping(String listingId, String userId, String scheduleStatus) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID_COLUMN, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue(SCHEDULE_STATUS_COLUMN, new SqlParameterValue(Types.OTHER, scheduleStatus));

    return super.update(ADD_UPDATE_LISTING_USER_MAPPING, paramSourceMap);
  }

  public int removeListingWithStatus(String listingId, String userId, String status) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID_COLUMN, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue(SCHEDULE_STATUS_COLUMN, new SqlParameterValue(Types.OTHER, status));

    return super.update(REMOVE_LISTING_USER_MAPPING, paramSourceMap);
  }

  public int updateShortlistedListingToScheduleRequest(String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue("schedule_status_schedule_request", new SqlParameterValue(Types.OTHER, ScheduleStatusConstants.SCHEDULING_REQUESTED));
    paramSourceMap.addValue("schedule_status_shortlisted", new SqlParameterValue(Types.OTHER, ScheduleStatusConstants.SHORTLISTED));

    return super.update(UPDATE_ALL_LISTING_FOR_USER, paramSourceMap);
  }

  public int getUserCount() {

    try {
      return super.queryForObject(RETRIEVE_USER_COUNT, null, new RowMapper<Integer>() {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
          return rs.getInt("count");
        }

      });
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return 0;
  }

  public boolean updateAuthorityToBroker(List<String> userPhoneNumbers) {
    if (CollectionUtils.isEmpty(userPhoneNumbers)) {
      return false;
    }

    long[] authorities = {3};

    List<Map<String, Object>> batchValues = new ArrayList<>(userPhoneNumbers.size());
    for (String brokerMobNum : userPhoneNumbers) {
      batchValues.add(
          new MapSqlParameterSource(USER_AUTHORITY_COLUMN, new SqlParameterValue(Types.ARRAY, authorities))
              .addValue("user_mobile", new SqlParameterValue(Types.OTHER, brokerMobNum))
              .getValues());
    }

    int[] updateCounts = super.batchUpdate(UPDATE_AUTHORITIES, batchValues.toArray(new Map[userPhoneNumbers.size()]));
    LOGGER.debug("Result of updating user role with broker: {}", updateCounts);

    if (ArrayUtils.isEmpty(updateCounts)) {
      return false;
    }

    if (userPhoneNumbers.size() == updateCounts.length) {
      return true;
    }

    return false;
  }

  public int updatePotentialPrefSlot(String userId, String listingId, UserPrefVisitSlot userPrefVisitSlot) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue(LISTING_ID_COLUMN, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("schedule_itinerary_status", new SqlParameterValue(Types.OTHER, ItineraryStatusConstants.ITINERARY_CREATION_IN_PROGRESS));
    paramSourceMap.addValue("potential_itinerary", new SqlParameterValue(Types.VARCHAR, userPrefVisitSlotMarshaller.marshall(userPrefVisitSlot)));

    return super.update(UPDATE_POTENTIAL_PREF_SLOT, paramSourceMap);
  }

  public int updateItineraryStatus(String userId, String listingId, String itineraryStatus) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue(LISTING_ID_COLUMN, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("schedule_itinerary_status", new SqlParameterValue(Types.OTHER, itineraryStatus));

    return super.update(UPDATE_ITINERARY_STATUS, paramSourceMap);
  }

  public int updateUserPrefVisitSlot(String userId, UserPrefVisitSlot userPrefVisitSlot) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue(USER_PREF_VISIT_SLOT_COLUMN, new SqlParameterValue(Types.VARCHAR, userPrefVisitSlotMarshaller.marshall(userPrefVisitSlot)));

    return super.update(UPDATE_USER_PREF_VISIT_SLOT, paramSourceMap);
  }

  public int refreshUserScheduleView() {
    return super.update(REFRESH_USER_SCHEDULE_VIEW, null);
  }

  public int saveUserDetails(User user) throws InvalidAuthorityException {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    //paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, user.getUserId()));
    paramSourceMap.addValue(USER_NAME_COLUMN, new SqlParameterValue(Types.VARCHAR, user.getUserName()));
    paramSourceMap.addValue(USER_MOBILE_COLUMN, new SqlParameterValue(Types.VARCHAR, user.getUserMobile()));
    paramSourceMap.addValue(USER_EMAIL_COLUMN, new SqlParameterValue(Types.VARCHAR, user.getUserEmail()));
    long[] authorities = enrichAuthoritiesForInsert(user.getAuthorities());
    paramSourceMap.addValue(USER_AUTHORITY_COLUMN, new SqlParameterValue(Types.ARRAY, authorities));
    paramSourceMap.addValue(USER_SECRET_COLUMN, new SqlParameterValue(Types.BINARY, user.getSecret()));
    paramSourceMap.addValue(USER_FACEBOOK_COLUMN, new SqlParameterValue(Types.VARCHAR, user.getUserFacebookId()));
    paramSourceMap.addValue(USER_GOOGLE_COLUMN, new SqlParameterValue(Types.VARCHAR, user.getUserGoogleId()));


    return super.update(ADD_UPDATE_USER, paramSourceMap);
  }

  private long[] enrichAuthoritiesForInsert(Collection<Authority> authorities) throws InvalidAuthorityException {
    List<Long> authoritiesList = new ArrayList<>();
    authorities.forEach(e -> {
      if (null != e.getId() && 0 != e.getId()) {
        authoritiesList.add(e.getId());
      }
    });

    if (authoritiesList.size() != 0) {
      return ArrayUtils.toPrimitive(authoritiesList.toArray(new Long[authoritiesList.size()]));
    }

    throw new InvalidAuthorityException("No authority assigned for this user");
  }

  public UserPrefVisitSlot getUserPrefVisitSlot(String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));

    return super.queryForObject(GET_USER_PREF_VISIT_SLOT, paramSourceMap, new RowMapper<UserPrefVisitSlot>() {
      @Override
      public UserPrefVisitSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
        String userPrefVisitSlot = rs.getString(USER_PREF_VISIT_SLOT_COLUMN);
        return !StringUtils.isEmpty(userPrefVisitSlot) ? userPrefVisitSlotMarshaller.unmarshall(userPrefVisitSlot) : null;
      }
    });
  }

  public User findByPhoneNo(String phoneNo) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_MOBILE_COLUMN, new SqlParameterValue(Types.VARCHAR, phoneNo));

    List<User> userList = retrieveUserDetails(paramSourceMap, RETRIEVE_USER_WITH_PHONE);

    if (null != userList && userList.size() != 0) {
      return userList.get(0);
    }

    return null;
  }

  public List<UserSchedule> retrieveUserSchedule() {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("schedule_itinerary_status", new SqlParameterValue(Types.VARCHAR, ItineraryStatusConstants.ITINERARY_NOT_CREATED));

    List<UserSchedule> userSchedules = retrieveUserSchedule(paramSourceMap, RETRIEVE_USER_SCHEDULE);

    if (null != userSchedules && userSchedules.size() != 0) {
      return userSchedules;
    }

    return null;
  }

  public User findByFacebookId(String facebookId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_FACEBOOK_COLUMN, new SqlParameterValue(Types.VARCHAR, facebookId));

    List<User> userList = retrieveUserDetails(paramSourceMap, RETRIEVE_USER_WITH_FACEBOOK_ID);

    if (null != userList && userList.size() != 0) {
      return userList.get(0);
    }

    return null;
  }

  public User findByGoogleId(String googleId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_GOOGLE_COLUMN, new SqlParameterValue(Types.VARCHAR, googleId));

    List<User> userList = retrieveUserDetails(paramSourceMap, RETRIEVE_USER_WITH_GOOGLE_ID);

    if (null != userList && userList.size() != 0) {
      return userList.get(0);
    }

    return null;
  }

  public User findByUserId(String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(USER_ID_COLUMN, new SqlParameterValue(Types.OTHER, userId));

    List<User> userList = retrieveUserDetails(paramSourceMap, RETRIEVE_USER_WITH_USER_ID);

    if (null != userList && userList.size() != 0) {
      return userList.get(0);
    }

    return null;
  }

  private List<User> retrieveUserDetails(MapSqlParameterSource paramSourceMap, String sql) {
    return super.query(sql, paramSourceMap, new RowMapper<User>() {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = null;
        try {
          user = User.builder()
              .userId(String.valueOf(rs.getObject(USER_ID_COLUMN)))
              .userName(rs.getString(USER_NAME_COLUMN))
              .userFacebookId(rs.getString(USER_FACEBOOK_COLUMN))
              .userGoogleId(rs.getString(USER_GOOGLE_COLUMN))
              .userMobile(rs.getString(USER_MOBILE_COLUMN))
              .userEmail(rs.getString(USER_EMAIL_COLUMN))
              .userAcceptanceDoc(rs.getString("user_acceptance_doc"))
              .userGovtIdNo(rs.getString("user_govt_id_no"))
              .userGovtIdImage(rs.getString("user_govt_id_image"))
              .authorities(enrichAuthoritiesForSelect(rs))
              .userPrefCommFreq(rs.getString("user_pref_comm_freq"))
              .secret(rs.getBytes(USER_SECRET_COLUMN))
              .userPrefVisitSlot(null
                  != rs.getString(USER_PREF_VISIT_SLOT_COLUMN) ? userPrefVisitSlotMarshaller.unmarshall(rs.getString("user_pref_visit_slot")) : null)
              .userPrefCommMode(enrichUserPrefCommMode(rs.getString("user_pref_comm_mode")))
              .build();
        } catch (InvalidAuthorityException e) {
          //for now log it , lets see if we want to stop execution on this
          LOGGER.error(e.getMessage(), e);
        }

        return user;
      }
    });
  }

  public List<UserSchedule> retrieveUserSchedule(MapSqlParameterSource paramSourceMap, String sql) {
    return super.query(sql, paramSourceMap, new RowMapper<UserSchedule>() {
      @Override
      public UserSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserSchedule userSchedule = null;

        userSchedule = UserSchedule.builder()
            .ownerId(String.valueOf(rs.getObject("owner_id")))
            .tenantId(String.valueOf(rs.getObject("tenant_id")))
            .listingId(String.valueOf(rs.getObject("listing_id")))
            .potentialItinerary(StringUtils.isNotBlank(rs.getString("potential_itinerary")) ? userPrefVisitSlotMarshaller.unmarshall(rs.getString("potential_itinerary")) : null)
            .tenantPrefVisitSlot(StringUtils.isNotBlank(rs.getString("tenant_pref_visit_slot")) ? userPrefVisitSlotMarshaller.unmarshall(rs.getString("tenant_pref_visit_slot")) : null)
            .ownerPrefVisitSlot(StringUtils.isNotBlank(rs.getString("owner_pref_visit_slot")) ? userPrefVisitSlotMarshaller.unmarshall(rs.getString("owner_pref_visit_slot")) : null)
            .build();

        return userSchedule;
      }
    });
  }

  public int addUserRequestCallback(String phoneNo) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("phone_no", new SqlParameterValue(Types.VARCHAR, phoneNo));

    return super.update(ADD_USER_REQUEST_CALLBACK, paramSourceMap);
  }

  private Collection<Authority> enrichAuthoritiesForSelect(ResultSet rs) throws SQLException, InvalidAuthorityException {
    Array authorityIds = rs.getArray(USER_AUTHORITY_COLUMN);
    List<Authority> authorities = new ArrayList<>();

    if (null != authorityIds) {

      Integer[] ids = (Integer[]) authorityIds.getArray();

      for (Integer id : ids) {
        MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
        paramSourceMap.addValue(AUTHORITY_ID_COLUMN, new SqlParameterValue(Types.INTEGER, id));
        Authority authority = super.queryForObject(RETRIEVE_AUTHORITY, paramSourceMap, new RowMapper<Authority>() {
          @Override
          public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {

            return Authority.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
          }
        });
        authorities.add(authority);
      }
      return authorities;
    }

    throw new InvalidAuthorityException("No authority retrievied for the user");
  }

  private List<String> enrichUserPrefCommMode(String user_pref_comm_mode) {
    if (StringUtils.isNotBlank(user_pref_comm_mode)) {
      return new ArrayList<>(Arrays.asList(user_pref_comm_mode.split(",")));
    }
    return null;
  }

  public int[] addUserProspectiveListingMapping(String userId, List<String> listingIdList) {
    List<Map<String, Object>> batchValues = new ArrayList<>(listingIdList.size());
    for (String listingId : listingIdList) {
      batchValues.add(
          new MapSqlParameterSource("user_id", new SqlParameterValue(Types.OTHER, userId))
              .addValue("listing_id", new SqlParameterValue(Types.OTHER, listingId))
              .addValue("schedule_status", new SqlParameterValue(Types.OTHER, ScheduleStatusConstants.PROSPECTIVE))
              .getValues());
    }

    int[] updateCounts = super.batchUpdate(ADD_PROSPECTIVE_LISTING_USER_MAPPING, batchValues.toArray(new Map[listingIdList.size()]));
    LOGGER.debug("Result of updating listing_user_schedule_mapping with userId {} and listingIds: {} is {}", userId, listingIdList, updateCounts);
    return updateCounts;
  }

  public User getOwnerDetails(String listingId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID_COLUMN, new SqlParameterValue(Types.OTHER, listingId));

    return super.queryForObject(GET_LISTING_OWNER_DETAIL, paramSourceMap, new RowMapper<User>() {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user =  User.builder()
            .userId(String.valueOf(rs.getObject(USER_ID_COLUMN)))
            .userName(rs.getString(USER_NAME_COLUMN))
            .userMobile(rs.getString(USER_MOBILE_COLUMN))
            .build();
        LOGGER.debug("ListingId: {} owner details retrieved as {}", listingId, user);
        return user;
      }
    });

  }
}