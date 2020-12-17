package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.Apartment;
import com.crs.denzip.model.entities.CRSImage;
import com.crs.denzip.model.entities.DeactivateListingDetails;
import com.crs.denzip.model.entities.Listing;
import com.crs.denzip.model.entities.LocalityData;
import com.crs.denzip.model.marshaller.AmenitiesMarshaller;
import com.crs.denzip.model.marshaller.CRSImageMarshaller;
import com.crs.denzip.model.marshaller.LocalityDataMarshaller;
import com.crs.denzip.persistence.annotation.audit.Auditable;
import com.crs.denzip.persistence.annotation.audit.AuditingActionType;
import com.crs.denzip.persistence.annotation.audit.ResultStorageType;
import com.crs.denzip.persistence.annotation.monitor.MonitorTime;
import com.crs.denzip.persistence.communication.FilterRequest;
import com.crs.denzip.persistence.filters.Filter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class ListingDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListingDAO.class);

  private static final String SPACE = " ";

  private static final String LOCALITY_DATA_QUERY = "select locality_data from listing_locality_data where listing_id = :listing_id";

  private static final String LISTING_COUNT_QUERY = "select count(1) from listing";

  private static final String LISTING_LAT_LONG_QUERY = "select listing_latitude, listing_longitude from listing where listing_id = :listing_id";

  private static final String LISTING_ID = "listing_id";

  private static final String LISTING_ACTIVE_IND = "listing_active_ind";

  private static final String LISTING_LATITUDE = "listing_latitude";

  private static final String LISTING_LONGITUDE = "listing_longitude";

  private static final String APARTMENT_ID = "apartment_id";

  private static final String PENDING = "PENDING";

  private static final String LISTING_QUERY_SELECT =
      "select l.listing_id, l.user_id, l.listing_title, l.listing_address, listing_type.listing_type_name, listing_property_type.property_type_name, "
          + "l.listing_images, l.listing_latitude, l.listing_longitude, l.listing_area, l.listing_value, l.listing_deposit, l.listing_active_ind, l.created_time, l.created_by_user_id, l.listing_possession_by, l.listing_virtual_presence, "
          + "listing_furnishing_type.furnishing_type_name, listing_transaction_mode.transaction_mode_name, l.non_veg_allowed, l.bachelor_allowed, listing_bedroom_count.bedroom_count_name, "
          + "row_to_json(la.*) as listing_amenties, a.apartment_id, a.apartment_name, a.apartment_images, row_to_json(aa.*) as apartment_amenities,"
          + "(select count(lusm.user_id) from listing_user_schedule_mapping lusm where l.listing_id = lusm.listing_id and lusm.schedule_status != 'PROSPECTIVE' ) as user_interested_count, "
          + "(select ARRAY(SELECT authority.name FROM authority, crs_user where l.created_by_user_id = crs_user.user_id and authority.id = ANY(crs_user.authorities) )) as created_user_authorities";

  private static final String LISTING_QUERY_TABLES = "from listing l " + "LEFT JOIN apartment a ON l.apartment_id = a.apartment_id "
      + "LEFT JOIN apartment_amenity aa on a.apartment_id = aa.apartment_id,"
      + "listing_amenity la, listing_type, listing_property_type, listing_furnishing_type, listing_transaction_mode, listing_bedroom_count";

  private static final String LISTING_QUERY_JOINS = "l.listing_type_id = listing_type.listing_type_id "
      + "and l.property_type_id = listing_property_type.property_type_id and l.furnishing_type_id = listing_furnishing_type.furnishing_type_id "
      + "and l.transaction_mode_id = listing_transaction_mode.transaction_mode_id "
      + "and l.bedroom_count_id = listing_bedroom_count.bedroom_count_id "
      + "and l.listing_id = la.listing_id " + "order by l.created_time desc";

  private static final String SELECT_STMT =
      LISTING_QUERY_SELECT + SPACE + "%1$s" + SPACE + LISTING_QUERY_TABLES + SPACE + "%2$s" + SPACE + "where" + SPACE + "%3$s" + SPACE
          + LISTING_QUERY_JOINS;

  private static final String GET_DEFAULT_LISTING_AMENITIES =
      "SELECT json_object_agg (column_name,false) as listing_amenities  FROM information_schema.columns WHERE  table_name  = 'listing_amenity'";

  private static final String ADD_DEACTIVATED_LISTING =
      "insert into deactivated_owner_listing_details (owner_id, cashback_status, listing_id, tenant_user_id, deactivation_type) values (:owner_id, :cashback_status, :listing_id, (select user_id from crs_user where user_mobile = :tenant_mobile_number), :deactivation_type) ON CONFLICT DO NOTHING";


  private static final String ADD_LISTING =
      "with inserted as (insert into listing (user_id, " + "listing_title, listing_address, " + "listing_type_id, "
          + "property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit, "
          + "apartment_id, furnishing_type_id, bedroom_count_id, transaction_mode_id, non_veg_allowed, bachelor_allowed, listing_active_ind, created_by_user_id, listing_possession_by, broker_listing_owner_mobile) "
          + "values ((select user_id from crs_user where user_mobile = :user_mobile), " + ":listing_title, :listing_address, "
          + "(select listing_type_id from listing_type where listing_type_name = :listing_type_name), "
          + "(select property_type_id from listing_property_type where property_type_name = :property_type_name), "
          + ":listing_latitude, :listing_longitude, :listing_area, :listing_value, :listing_deposit, " + ":apartment_id, "
          + "(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = :furnishing_type_name), "
          + "(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = :bedroom_count_name), "
          + "(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = :transaction_mode_name), "
          + ":non_veg_allowed, :bachelor_allowed, false, :created_by_user_id, :listing_possession_by, :broker_listing_owner_mobile) returning listing_id ) "
          + "insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed) "
          + "select inserted.listing_id, :fans, :geyser, :cupboards, :chimney, :parking, :airconditioner, :sofa, :diningtable, :centertable, :television, :internet, :dth, :refrigerator, :washingmachine, :bed from inserted "
          + "returning listing_id";

  private static final String UPDATE_LISTING =
      "with updated as (update listing set (listing_title, listing_address, " + "listing_type_id, "
      + "property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit, "
      + "apartment_id, furnishing_type_id, bedroom_count_id, transaction_mode_id, non_veg_allowed, bachelor_allowed, listing_active_ind, last_modified_time, listing_possession_by) "
      + "= (:listing_title, :listing_address, "
      + "(select listing_type_id from listing_type where listing_type_name = :listing_type_name), "
      + "(select property_type_id from listing_property_type where property_type_name = :property_type_name), "
      + ":listing_latitude, :listing_longitude, :listing_area, :listing_value, :listing_deposit, " + ":apartment_id, "
      + "(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = :furnishing_type_name), "
      + "(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = :bedroom_count_name), "
      + "(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = :transaction_mode_name), "
      + ":non_veg_allowed, :bachelor_allowed, false, now(), :listing_possession_by) where listing_id = :listing_id returning listing_id )  "
      + "update listing_amenity set (fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed, last_modified_time) "
      + "= (select :fans, :geyser, :cupboards, :chimney, :parking, :airconditioner, :sofa, :diningtable, :centertable, :television, :internet, :dth, :refrigerator, :washingmachine, :bed, now() from updated) where listing_id = (select updated.listing_id  from updated) "
      + "returning listing_id";

  private static final String UPDATE_LISTING_IMAGES = "update listing set listing_images = :listing_images where listing_id = :listing_id";
  private static final String UPDATE_LISTING_ACTIVE_IND =
      "update listing set listing_active_ind = :listing_active_ind where listing_id = :listing_id";

  private static final String UPDATE_LISTING_ACTIVE_IND_ACTIVATION_TIME =
      "update listing set listing_active_ind = :listing_active_ind, last_activation_time = now() where listing_id = :listing_id";

  private static final String UPDATE_LISTING_LOCALITY_DATA =
      "insert into listing_locality_data (listing_id, locality_data) values (:listing_id, :locality_data) ON CONFLICT (listing_id) DO UPDATE SET locality_data = :locality_data";


  private final AmenitiesMarshaller amenitiesMarshaller;
  private final LocalityDataMarshaller localityDataMarshaller;
  private final CRSImageMarshaller crsImageMarshaller;
  private final String zoneId;

  @MonitorTime(category = "DAO", subCategory = "getListing")
  @Auditable(actionType = AuditingActionType.SEARCH_FILTER_LIST, resultStorage = ResultStorageType.COUNT)
  public List<Listing> getListing(FilterRequest filterRequest) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();

    StringBuilder selectColumnsBuilder = new StringBuilder();
    StringBuilder tableBuilder = new StringBuilder();
    StringBuilder joinBuilder = new StringBuilder();
    List<Filter> selectColumnFilterList = new ArrayList<>();
    List<Filter> filters = new ArrayList<>(filterRequest.getFilterList());


    for (Filter filter : filters) {

      if (filter.isValid()) {

        if (!StringUtils.isEmpty(filter.getSelectColumn())) {
          selectColumnFilterList.add(filter);
          selectColumnsBuilder.append(", ")
              .append(filter.getSelectColumnQuery());
        }

        if (!StringUtils.isEmpty(filter.getSelectTable())) {
          tableBuilder.append(", ")
              .append(filter.getSelectTable());
        }

        if (!StringUtils.isEmpty(filter.getJoinArguments())) {
          filter.addParams(paramSourceMap);
          joinBuilder.append(filter.getJoinArguments());
          joinBuilder.append(" and ");
        }
      }
    }

    String query = String.format(SELECT_STMT, selectColumnsBuilder.toString(), tableBuilder.toString(), joinBuilder.toString());

    return super.query(query, paramSourceMap, new RowMapper<Listing>() {
      @Override
      public Listing mapRow(ResultSet rs, int rowNum) throws SQLException {

        Listing listing = Listing.builder()
            .userId(String.valueOf(rs.getObject("user_id")))
            .listingId(String.valueOf(rs.getObject(LISTING_ID)))
            .listingTitle(rs.getString("listing_title"))
            .listingAddress(rs.getString("listing_address"))
            .listingType(rs.getString("listing_type_name"))
            .propertyType(rs.getString("property_type_name"))
            .listingLatitude(rs.getDouble(LISTING_LATITUDE))
            .listingLongitude(rs.getDouble(LISTING_LONGITUDE))
            .listingArea(rs.getLong("listing_area"))
            .listingValue(rs.getDouble("listing_value"))
            .listingDeposit(rs.getDouble("listing_deposit"))
            .furnishingType(rs.getString("furnishing_type_name"))
            .listingBedroomCount(rs.getString("bedroom_count_name"))
            .transactionMode(rs.getString("transaction_mode_name"))
            .nonVegAllowed(rs.getBoolean("non_veg_allowed"))
            .bachelorAllowed(rs.getBoolean("bachelor_allowed"))
            .listingActiveInd(rs.getBoolean("listing_active_ind"))
            .listingAmenities(filterAmenitiesMap(amenitiesMarshaller.unmarshall(rs.getString("listing_amenties"))))
            .usersInterestedCount(rs.getInt("user_interested_count"))
            //.created_time(ZonedDateTime.parse(rs.getString("created_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSZZZZZ")))
            .createdTime(rs.getObject("created_time", OffsetDateTime.class).atZoneSameInstant(ZoneId.of(zoneId)))
            .createdByUserId(String.valueOf(rs.getObject("created_by_user_id")))
            .listingPossessionBy(rs.getString("listing_possession_by"))
            .listingVirtualPresence(rs.getBoolean("listing_virtual_presence"))
            .createdUserAuthorityNames(buildCreatedUserAuthorityNames(rs))
            .apartment(buildApartment(rs))
            .build();

        String images = rs.getString("listing_images");
        if (!StringUtils.isEmpty(images)) {
          listing.setCrsImages(crsImageMarshaller.unmarshall(images));
        }

        if(listing.getApartment() != null && !CollectionUtils.isEmpty(listing.getApartment().getCrsImages())) {
          List<CRSImage> imageList = listing.getCrsImages();
          if (imageList == null) {
            imageList = new ArrayList<>(listing.getApartment().getCrsImages().size());
            listing.setCrsImages(imageList);
          }

          // The below is required so that when we sort images on ui side using name, apartment images always come at end
          listing.getApartment().getCrsImages().stream().forEach(crsImage -> crsImage.setImageName("zzzz_" + crsImage.getImageName()));
          imageList.addAll(listing.getApartment().getCrsImages());
        }

        for (Filter filter : selectColumnFilterList) {
          filter.enrichData(listing, rs.getString(filter.getSelectColumn()));
        }

        return listing;
      }
    });
  }

  private List<String> buildCreatedUserAuthorityNames(ResultSet rs) throws SQLException {
    if(rs.getArray("created_user_authorities") != null) {
      Object names = rs.getArray("created_user_authorities").getArray();
      if(Objects.nonNull(names)) {
        return new ArrayList<>(Arrays.asList((String[])names));
      }
    }
    return null;
  }

  private Apartment buildApartment(ResultSet rs) throws SQLException {
    String apartmentId = rs.getString(APARTMENT_ID);
    if (apartmentId != null) {
      Apartment apartment = Apartment.builder()
          .apartmentId(apartmentId)
          .apartmentName(rs.getString("apartment_name"))
          .apartmentAmenities(filterAmenitiesMap(amenitiesMarshaller.unmarshall(rs.getString("apartment_amenities"))))
          .build();

      String images = rs.getString("apartment_images");
      if (!StringUtils.isEmpty(images)) {
        apartment.setCrsImages(crsImageMarshaller.unmarshall(images));
      }

      return apartment;
    }
    return null;
  }

  public String addListing(Listing listing) {

    MapSqlParameterSource paramSourceMap = mapListingToSqlParams(listing);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(ADD_LISTING, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1) {
      return String.valueOf(keyHolder.getKeys()
          .get(LISTING_ID));
    }

    return null;
  }

  public String updateListing(Listing listing) {

    if(StringUtils.isEmpty(listing.getListingId())) {
      return null;
    }

    MapSqlParameterSource paramSourceMap = mapListingToSqlParams(listing);
    paramSourceMap.addValue(LISTING_ID, listing.getListingId(), Types.OTHER);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(UPDATE_LISTING, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1) {
      return String.valueOf(keyHolder.getKeys()
                                     .get(LISTING_ID));
    }

    return null;
  }

  private MapSqlParameterSource mapListingToSqlParams(Listing listing) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_mobile", listing.getUserId(), Types.VARCHAR);
    paramSourceMap.addValue("listing_title", listing.getListingTitle(), Types.VARCHAR);
    paramSourceMap.addValue("listing_address", listing.getListingAddress(), Types.VARCHAR);
    paramSourceMap.addValue("listing_type_name", listing.getListingType(), Types.VARCHAR);
    paramSourceMap.addValue("property_type_name", listing.getPropertyType(), Types.VARCHAR);
    // paramSourceMap.addValue("listing_images", listing.getListingImageLocation(), Types.VARCHAR);
    paramSourceMap.addValue(LISTING_LATITUDE, listing.getListingLatitude(), Types.DOUBLE);
    paramSourceMap.addValue(LISTING_LONGITUDE, listing.getListingLongitude(), Types.DOUBLE);
    paramSourceMap.addValue("listing_area", listing.getListingArea(), Types.INTEGER);
    paramSourceMap.addValue("listing_value", listing.getListingValue(), Types.DOUBLE);
    paramSourceMap.addValue("listing_deposit", listing.getListingDeposit(), Types.DOUBLE);
    paramSourceMap.addValue("furnishing_type_name", listing.getFurnishingType(), Types.VARCHAR);
    paramSourceMap.addValue("bedroom_count_name", listing.getListingBedroomCount(), Types.VARCHAR);
    paramSourceMap.addValue("transaction_mode_name", listing.getTransactionMode(), Types.VARCHAR);
    paramSourceMap.addValue("non_veg_allowed", listing.isNonVegAllowed(), Types.BOOLEAN);
    paramSourceMap.addValue("bachelor_allowed", listing.isBachelorAllowed(), Types.BOOLEAN);

    if(!StringUtils.isEmpty(listing.getCreatedByUserId())) {
      paramSourceMap.addValue("created_by_user_id", listing.getCreatedByUserId(), Types.OTHER);
    }

    if(!StringUtils.isEmpty(listing.getListingPossessionBy())) {
      paramSourceMap.addValue("listing_possession_by", listing.getListingPossessionBy(), Types.VARCHAR);
    }

    paramSourceMap.addValue("broker_listing_owner_mobile", listing.getBrokerListingOwnerMobile(), Types.VARCHAR);


    if (listing.getApartment() != null && !StringUtils.isEmpty(listing.getApartment()
                                                                      .getApartmentId())) {
      paramSourceMap.addValue(APARTMENT_ID, listing.getApartment()
          .getApartmentId(), Types.OTHER);
    } else {
      paramSourceMap.addValue(APARTMENT_ID, null, Types.OTHER);
    }

    // Listing Amenities
    addListingAmenityToParamMap("fans", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("geyser", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("cupboards", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("chimney", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("parking", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("airconditioner", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("sofa", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("diningtable", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("centertable", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("television", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("internet", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("dth", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("refrigerator", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("washingmachine", listing.getListingAmenities(), paramSourceMap);
    addListingAmenityToParamMap("bed", listing.getListingAmenities(), paramSourceMap);
    return paramSourceMap;
  }

  public boolean addDeactivatedListing(DeactivateListingDetails deactivateListingDetails) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("owner_id", deactivateListingDetails.getOwnerId(), Types.OTHER);
    paramSourceMap.addValue("listing_id", deactivateListingDetails.getListingId(), Types.OTHER);
    paramSourceMap.addValue("tenant_mobile_number", deactivateListingDetails.getTenantUserId(), Types.VARCHAR);
    paramSourceMap.addValue("cashback_status", deactivateListingDetails.getDeactivationStatus(), Types.VARCHAR);
    paramSourceMap.addValue("deactivation_type", deactivateListingDetails.getDeactivationType(), Types.VARCHAR);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(ADD_DEACTIVATED_LISTING, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    return result == 1;
  }

  private void addListingAmenityToParamMap(String amenityKey, Map<String, Boolean> amenities, MapSqlParameterSource paramSourceMap) {
    boolean isPresent = false;
    if (amenities != null && amenities.containsKey(amenityKey)) {
      isPresent = amenities.get(amenityKey);
    }
    paramSourceMap.addValue(amenityKey, isPresent, Types.BOOLEAN);
  }

  public LocalityData getLocalityData(String listingId) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID, new SqlParameterValue(Types.OTHER, listingId));

    try {
      return super.queryForObject(LOCALITY_DATA_QUERY, paramSourceMap, new RowMapper<LocalityData>() {

        @Override
        public LocalityData mapRow(ResultSet rs, int rowNum) throws SQLException {
          String localityDataJson = rs.getString("locality_data");
          if (!StringUtils.isEmpty(localityDataJson)) {
            return localityDataMarshaller.unmarshall(localityDataJson);
          }
          return null;
        }

      });
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return null;
  }

  public int getListingCount() {

    try {
      return super.queryForObject(LISTING_COUNT_QUERY, null, new RowMapper<Integer>() {

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

  public Map<String, Double> getListingLatLong(String listingId) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID, new SqlParameterValue(Types.OTHER, listingId));

    return super.queryForObject(LISTING_LAT_LONG_QUERY, paramSourceMap, new RowMapper<Map<String, Double>>() {

      @Override
      public Map<String, Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Double latitude = rs.getDouble(LISTING_LATITUDE);
        Double longitude = rs.getDouble(LISTING_LONGITUDE);

        Map<String, Double> latLongMap = new HashMap<>();
        latLongMap.put("LATITUDE", latitude);
        latLongMap.put("LONGITUDE", longitude);
        return latLongMap;
      }

    });

  }

  public int addLocalityData(String listingId, LocalityData localityData) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("locality_data", new SqlParameterValue(Types.VARCHAR, localityDataMarshaller.marshall(localityData)));

    return super.update(UPDATE_LISTING_LOCALITY_DATA, paramSourceMap);
  }

  public int updateListingImages(String listingId, List<CRSImage> crsImages) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("listing_images", new SqlParameterValue(Types.VARCHAR, crsImageMarshaller.marshall(crsImages)));

    return super.update(UPDATE_LISTING_IMAGES, paramSourceMap);
  }

  public int updateListingStatus(String listingId, boolean status) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue(LISTING_ID, new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue(LISTING_ACTIVE_IND, new SqlParameterValue(Types.BOOLEAN, status));

    if(status) {
      return super.update(UPDATE_LISTING_ACTIVE_IND_ACTIVATION_TIME, paramSourceMap);
    } else {
      return super.update(UPDATE_LISTING_ACTIVE_IND, paramSourceMap);
    }
  }

  public Map<String, Boolean> getDefaultListingAmenities() {

    return super.queryForObject(GET_DEFAULT_LISTING_AMENITIES, new MapSqlParameterSource(), new RowMapper<Map<String, Boolean>>() {

      @Override
      public Map<String, Boolean> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Boolean> listingAmenities = amenitiesMarshaller.unmarshall(rs.getString("listing_amenities"));
        filterAmenitiesMap(listingAmenities).keySet()
            .forEach(key -> listingAmenities.put(key, false));
        return listingAmenities;
      }

    });

  }

  private Map<String, Boolean> filterAmenitiesMap(Map<String, Boolean> amenitiesMap) {
    amenitiesMap.remove(LISTING_ID);
    amenitiesMap.remove(APARTMENT_ID);
    amenitiesMap.remove("created_time");
    amenitiesMap.remove("last_modified_time");

    return amenitiesMap;
  }

}
