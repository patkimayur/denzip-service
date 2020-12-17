package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.DeactivateListingDetails;
import com.crs.denzip.model.entities.Listing;
import com.crs.denzip.model.entities.LocalityCategory;
import com.crs.denzip.model.entities.LocalityData;
import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.persistence.communication.FilterRequest;
import com.crs.denzip.persistence.dao.ListingDAO;
import com.crs.denzip.persistence.filters.FilterCondition;
import com.crs.denzip.persistence.filters.ListingActiveStatusFilter;
import com.crs.denzip.persistence.filters.ListingIdFilter;
import com.crs.denzip.persistence.filters.RecentlyAddedListingFilter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Transactional(rollbackFor = {CRSException.class})
public class ListingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListingService.class);

  private final ListingDAO listingDAO;
  private final GoogleService googleService;
  private final TagService tagService;
  private final String deactivationValidDays;
  private final String zoneId;
  private final String recentlyAddedDays;


  public Map<String, Boolean> getDefaultListingAmenities() {
    return listingDAO.getDefaultListingAmenities();
  }

  private String addListing(Listing listing) {
    return listingDAO.addListing(listing);
  }

  private String updateListing(Listing listing) {
    return listingDAO.updateListing(listing);
  }

  public boolean addDeactivatedListing(DeactivateListingDetails deactivateListingDetails) {
    return listingDAO.addDeactivatedListing(deactivateListingDetails);
  }

  public boolean listingValidForDeactivation(String listingId) throws CRSException {
    Listing listing = getListing(listingId);

    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(zoneId));

    ZonedDateTime configuredDays = listing.getCreatedTime()
        .plusDays(Long.valueOf(deactivationValidDays));

    return now.toInstant()
        .isAfter(configuredDays.toInstant());
  }

  public Listing addAndGetListing(Listing listing) throws CRSException {
    String listingId = addListing(listing);
    if (!StringUtils.isEmpty(listingId)) {
      addAndGetLocalityData(listingId);
      Listing retrievedListing =  getListing(listingId);
      addAndGetListingTag(retrievedListing);
      return retrievedListing;
    }
    return null;
  }

  public Listing updateAndGetListing(Listing listing) throws CRSException {
    String listingId = updateListing(listing);
    if (!StringUtils.isEmpty(listingId)) {
      addAndGetLocalityData(listingId);
      Listing retrievedListing =  getListing(listingId);
      addAndGetListingTag(retrievedListing);
      return retrievedListing;
    }
    return null;
  }

  public Listing getListing(String id) throws CRSException {

    List<Listing> listingList = listingDAO.getListing(new FilterRequest(new ArrayList<>(Arrays.asList(new ListingIdFilter(Collections.singletonList(id))))));

    if (CollectionUtils.isEmpty(listingList)) {
      throw new CRSException(String.format("Unable to retrieve listing with listing id {}", id));
    }
    return listingList.get(0);
  }

  public int getListingCount() {
    return listingDAO.getListingCount();
  }



  public List<Listing> getAllListings(FilterRequest filterRequest) {
    LOGGER.debug("Retrieving listings for getAllListings: {}", filterRequest);
    return listingDAO.getListing(filterRequest);
  }

  public List<Listing> getActiveListings(FilterRequest filterRequest) {
    LOGGER.debug("Retrieving listings for getActiveListings: {}", filterRequest);
    if (null == filterRequest.getFilterList()) {
      filterRequest = new FilterRequest(new ArrayList<>());
    }

    filterRequest.getFilterList().add(new ListingActiveStatusFilter(Collections.singletonList(FilterCondition.builder()
        .condition("true")
        .applied(true)
        .build())));

    return listingDAO.getListing(filterRequest);
  }

  public List<Listing> getRecentlyAddedListings() {
    FilterRequest filterRequest = new FilterRequest(new ArrayList<>());
    filterRequest.getFilterList().add(new RecentlyAddedListingFilter( this.recentlyAddedDays));
    return this.getActiveListings(filterRequest);
  }


  public LocalityData getLocalityData(String listingId) {
    LocalityData localityData = listingDAO.getLocalityData(listingId);
    if (isLocalityDataValid(localityData)) {
      return localityData;
    }

    return addAndGetLocalityData(listingId);
  }

  private boolean isLocalityDataValid(LocalityData localityData) {
    if (localityData == null || CollectionUtils.isEmpty(localityData.getCategories())) {
      return false;
    }

    for (LocalityCategory localityCategory : localityData.getCategories()) {
      if (localityCategory == null || CollectionUtils.isEmpty(localityCategory.getResources())) {
        return false;
      }
    }

    return true;
  }

  public LocalityData addAndGetLocalityData(String listingId) {
    LocalityData localityData;
    Map<String, Double> latLongMap = listingDAO.getListingLatLong(listingId);
    localityData = googleService.getListingLocalityData(latLongMap.get("LATITUDE"), latLongMap.get("LONGITUDE"));
    listingDAO.addLocalityData(listingId, localityData);
    return localityData;
  }

  public String addAndGetListingTag(Listing listing) {
    return tagService.addListingTag(listing);
  }

  public boolean updateListingStatus(String listingId, boolean status) {

    return listingDAO.updateListingStatus(listingId, status) > 0;
  }

}
