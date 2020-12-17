package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

  private String listingId;
  private String userId;

  private String listingTitle;
  private String listingAddress;
  private String listingType; //Rental, Lease, Sale
  private String propertyType; //Individual House, Apartment, Row House
  private double listingLatitude;
  private double listingLongitude;
  private long listingArea;
  private double listingValue;
  private double listingDeposit;
  private String furnishingType;
  private String listingBedroomCount;
  private String transactionMode;
  private boolean nonVegAllowed;
  private boolean bachelorAllowed;
  private boolean listingActiveInd;
  private int usersInterestedCount;
  private String currentLoggedInUserStatus;
  private ZonedDateTime createdTime;
  private String createdByUserId;
  private List<String> createdUserAuthorityNames;

  private String listingPossessionBy;
  private String brokerListingOwnerMobile;
  private boolean listingVirtualPresence;



  private List<CRSImage> crsImages;

  private Map<String, Boolean> listingAmenities;

  private Apartment apartment;


}
