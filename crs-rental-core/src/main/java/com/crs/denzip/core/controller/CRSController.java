package com.crs.denzip.core.controller;

import com.crs.denzip.core.service.ApartmentService;
import com.crs.denzip.core.service.BrokerOrgService;
import com.crs.denzip.core.service.FileHandlingService;
import com.crs.denzip.core.service.FilterService;
import com.crs.denzip.core.service.ListingService;
import com.crs.denzip.core.service.NotificationService;
import com.crs.denzip.core.service.RazorpayService;
import com.crs.denzip.core.service.TagService;
import com.crs.denzip.core.service.UserService;
import com.crs.denzip.model.entities.Apartment;
import com.crs.denzip.model.entities.BrokerOrg;
import com.crs.denzip.model.entities.CRSImage;
import com.crs.denzip.model.entities.CountEntity;
import com.crs.denzip.model.entities.DeactivateListingDetails;
import com.crs.denzip.model.entities.Listing;
import com.crs.denzip.model.entities.LocalityData;
import com.crs.denzip.model.entities.RazorpayOrder;
import com.crs.denzip.model.entities.Tag;
import com.crs.denzip.model.entities.User;
import com.crs.denzip.model.entities.UserPrefVisitSlot;
import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.model.exception.InvalidAuthorityException;
import com.crs.denzip.model.marshaller.CRSMarshaller;
import com.crs.denzip.persistence.annotation.monitor.MonitorTime;
import com.crs.denzip.persistence.communication.FilterRequest;
import com.crs.denzip.persistence.communication.UserRequest;
import com.crs.denzip.persistence.filters.AreaFilter;
import com.crs.denzip.persistence.filters.Filter;
import com.crs.denzip.persistence.filters.FilterCondition;
import com.crs.denzip.persistence.filters.ListingIdFilter;
import com.crs.denzip.persistence.filters.ListingTypeFilter;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = {"${spring.security.allowedOrigins}"}, maxAge = 3600)
public class CRSController {

  public static final String ACTIVATED = "ACTIVATED";
  private static final Logger LOGGER = LoggerFactory.getLogger(CRSController.class);

  private final ListingService listingService;
  private final FilterService filterService;
  private final UserService userService;
  private final ApartmentService apartmentService;
  private final FileHandlingService fileHandlingService;
  private final TagService tagService;
  private final NotificationService notificationService;
  private final BrokerOrgService brokerOrgService;
  private final RazorpayService razorpayService;
  private final CRSMarshaller crsMarshaller;

  @MonitorTime(category = "CRSController", subCategory = "getListingDetail")
  @RequestMapping(value = "/getListingDetail", method = RequestMethod.POST)
  @ResponseBody
  public Listing getListingDetail(@RequestParam("listingId") String listingId) {
    try {
      LOGGER.debug("Retrieving listing detail for listingId :{} ", listingId);
      Listing listing = listingService.getListing(listingId);
      LOGGER.debug("Listing retrieved successfully");
      return listing;
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return null;
    }
  }

  @MonitorTime(category = "CRSController", subCategory = "getActiveListings")
  @RequestMapping(value = "/getActiveListings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public List<Listing> getActiveListings(@RequestBody FilterRequest filterRequest) {
    LOGGER.debug("Retrieving listing detail for filterRequest :{} ", crsMarshaller.marshall(filterRequest));

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return Collections.EMPTY_LIST;
    }

    List<Listing> listings = listingService.getActiveListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved {}", listings.size());
    }

    return listings;
  }

  @MonitorTime(category = "CRSController", subCategory = "getAllListings")
  @RequestMapping(value = "/getAllListings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public List<Listing> getAllListings(@RequestBody FilterRequest filterRequest) {
    LOGGER.debug("Retrieving listing detail for filterRequest :{} ", crsMarshaller.marshall(filterRequest));

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return Collections.EMPTY_LIST;
    }

    List<Listing> listings = listingService.getAllListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved {}", listings.size());
    }

    return listings;
  }

  private boolean isFilterRequestValid(FilterRequest filterRequest) {
    if (filterRequest == null || CollectionUtils.isEmpty(filterRequest.getFilterList())) {
      return false;
    }

    Filter listingIdFilter = filterRequest.getFilterList()
        .stream()
        .filter(filter -> (filter instanceof ListingIdFilter))
        .filter(Filter::isValid)
        .findFirst()
        .orElse(null);

    // If listingIdFilter is there, we are good and can return right away
    if (listingIdFilter != null) {
      return true;
    }

    // If listingIdFilter is not there, we should have both AreaFilter and ListingTypeFilter
    // This is to ensure we only return Sale or Rental properties at one time
    List<Filter> mandatoryFilterList = filterRequest.getFilterList()
        .stream()
        .filter(filter -> (filter instanceof AreaFilter || filter instanceof ListingTypeFilter))
        .filter(Filter::isValid)
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(mandatoryFilterList) && mandatoryFilterList.size() == 2) {
      return true;
    }

    return false;

  }

  @RequestMapping(value = "/deactivateOwnerRelatedListing", method = RequestMethod.POST)
  @ResponseBody
  public boolean deactivateOwnerRelatedListing(@RequestBody DeactivateListingDetails deactivateListingDetails) {
    LOGGER.debug("Adding deactivated listing details:{} ", crsMarshaller.marshall(deactivateListingDetails));
    try {
      return userService.deactivateListing(deactivateListingDetails);
    } catch (CRSException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.debug("Couldn't deactivate owner related listing");
    return false;
  }

  @RequestMapping(value = "/listingValidForDeactivation", method = RequestMethod.POST)
  @ResponseBody
  public boolean listingValidForDeactivation(@RequestParam("listingId") String listingId) {
    LOGGER.debug("Checking if listing valid for deactivation for listing id:{} ", listingId);
    try {
      return listingService.listingValidForDeactivation(listingId);
    } catch (CRSException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.debug("Couldn't check if listing valid for deactivation");
    return false;
  }


  @RequestMapping(value = "/addApartment", method = RequestMethod.POST)
  @ResponseBody
  public Apartment addApartment(@RequestBody Apartment apartment) {
    LOGGER.debug("Adding apartment :{} ", crsMarshaller.marshall(apartment));
    String apartmentId = apartmentService.addApartment(apartment);
    if (StringUtils.isNotBlank(apartmentId)) {
      LOGGER.debug("Apartment created with apartment id {}", apartmentId);
      apartment.setApartmentId(apartmentId);
      return apartment;
    }
    LOGGER.debug("Couldnt create apartment");
    return null;
  }

  @MonitorTime(category = "CRSController", subCategory = "registerBrokerOrgDetails")
  @RequestMapping(value = "/registerBrokerOrgDetails", method = RequestMethod.POST)
  @ResponseBody
  public boolean registerBrokerOrgDetails(@RequestBody BrokerOrg brokerOrg) {
    try {
      LOGGER.debug("Adding brokerOrg :{} ", crsMarshaller.marshall(brokerOrg));
      boolean isRegistered = brokerOrgService.registerBrokerOrg(brokerOrg);
      if (isRegistered) {
        LOGGER.debug("Broker Org created ");
        return true;
      }
      LOGGER.debug("Couldnt create Broker Org");
      return false;
    } catch (Exception ex) {
      LOGGER.error("Encountered exception", ex);
      return false;
    }
  }


  @MonitorTime(category = "CRSController", subCategory = "registerBrokerOrgMapping")
  @RequestMapping(value = "/registerBrokerOrgMapping", method = RequestMethod.POST)
  @ResponseBody
  public boolean registerBrokerOrgMapping(@RequestBody BrokerOrg brokerOrg) {
    try {
      LOGGER.debug("Adding brokerOrgMapping :{} ", crsMarshaller.marshall(brokerOrg));
      boolean isRegistered = brokerOrgService.registerBrokerOrgMapping(brokerOrg);
      if (isRegistered) {
        LOGGER.debug("Broker Org Mapping created ");
        return true;
      }
      LOGGER.debug("Couldnt create Broker Org Mapping");
      return false;
    } catch (Exception ex) {
      LOGGER.error("Encountered exception", ex);
      return false;
    }
  }


  @RequestMapping(value = "/updateApartment", method = RequestMethod.POST)
  @ResponseBody
  public Apartment updateApartment(@RequestBody Apartment apartment) {
    LOGGER.debug("Updating apartment :{} ", crsMarshaller.marshall(apartment));
    String apartmentId = apartmentService.updateApartment(apartment);
    if (StringUtils.isNotBlank(apartmentId)) {
      LOGGER.debug("Apartment updated with apartment id {}", apartmentId);
      apartment.setApartmentId(apartmentId);
      return apartment;
    }
    LOGGER.debug("Couldnt update apartment");
    return null;
  }

  @RequestMapping(value = "/addAndGetListing", method = RequestMethod.POST)
  @ResponseBody
  public Listing addAndGetListing(@RequestBody Listing listing) {
    LOGGER.debug("Adding listing :{} ", crsMarshaller.marshall(listing));
    try {
      Listing l = listingService.addAndGetListing(listing);
      if (l != null) {
        LOGGER.debug("Listing added and retrieved with listing id {}", l.getListingId());
        String message = "Listing Added - " + l.getListingId();
        userService.emailDenzipAdmins(message, message);
      }
      return l;
    } catch (CRSException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.debug("Couldnt add and get Listing");
    return null;
  }

  @RequestMapping(value = "/updateAndGetListing", method = RequestMethod.POST)
  @ResponseBody
  public Listing updateAndGetListing(@RequestBody Listing listing) {
    LOGGER.debug("Updating listing :{} ", crsMarshaller.marshall(listing));
    try {
      Listing l = listingService.updateAndGetListing(listing);
      if (l != null) {
        LOGGER.debug("Listing updated and retrieved with listing id {}", l.getListingId());
      }
      return l;
    } catch (CRSException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.debug("Couldnt update and get Listing");
    return null;
  }

  @MonitorTime(category = "CRSController", subCategory = "getFilters")
  @RequestMapping(value = "/getFilters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public FilterRequest getFilters() {
    LOGGER.debug("Retrieving all filters");
    return new FilterRequest(filterService.getFilters());
  }

  @MonitorTime(category = "CRSController", subCategory = "getBedroomCountFilter")
  @RequestMapping(value = "/getBedroomCountFilter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Filter getBedroomCountFilter() {
    return filterService.getBedroomCountFilter();
  }

  @MonitorTime(category = "CRSController", subCategory = "getListingTypeFilter")
  @RequestMapping(value = "/getListingTypeFilter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Filter getListingTypeFilter() {
    return filterService.getListingTypeFilter();
  }

  @MonitorTime(category = "CRSController", subCategory = "addShortlistedListing")
  @RequestMapping(value = "/addShortlistedListing", method = RequestMethod.POST)
  @ResponseBody
  public boolean addShortlistedListing(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId) {
    LOGGER.debug("Add shortlisted listing with listing id :{} and user id :{}", listingId, userId);
    return userService.addShortlistedListing(listingId, userId);
  }

  @RequestMapping(value = "/addUserListingMapping", method = RequestMethod.POST)
  @ResponseBody
  public boolean addUserListingMapping(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId, @RequestParam("scheduleStatus") String scheduleStatus) {
    LOGGER.debug("Add user listing with listing id :{} and user id :{} and schedule status :{}", listingId, userId, scheduleStatus);
    return userService.addUserListingMapping(listingId, userId, scheduleStatus);
  }

  @RequestMapping(value = "/activateListing", method = RequestMethod.POST)
  @ResponseBody
  public boolean activateListing(@RequestParam("listingId") String listingId) {
    LOGGER.debug("Activate listing with listing id :{}", listingId);
    try {
      boolean listingActivated = userService.activateListing(listingId, ACTIVATED);
      if (listingActivated) {
        LOGGER.debug("listing with listing id :{} activated sucessfully", listingId);
        notificationService.addListingNotification(listingId);
      }
      return listingActivated;
    } catch (CRSException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.debug("Unable to activate listing");
    return false;
  }


  @RequestMapping(value = "/removeShortlistedListing", method = RequestMethod.POST)
  @ResponseBody
  public boolean removeShortlistedListing(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId) {
    LOGGER.debug("Removing listingId: {} from shortlisted for userId: {}", listingId, userId);
    return userService.removeShortlistedListing(listingId, userId);
  }

  @RequestMapping(value = "/removeProspectiveListing", method = RequestMethod.POST)
  @ResponseBody
  public boolean removeProspectiveListing(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId) {
    LOGGER.debug("Removing listingId: {} from prospective for userId: {}", listingId, userId);
    return userService.removeProspectiveListing(listingId, userId);
  }

  @RequestMapping(value = "/getShortlistedListings", method = RequestMethod.POST)
  @ResponseBody
  public List<Listing> getShortlistedListings(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting shortlisetd listings for userId: {}", userId);
    return userService.getShortlistedListings(userId);
  }

  @RequestMapping(value = "/getProspectiveListings", method = RequestMethod.POST)
  @ResponseBody
  public List<Listing> getProspectiveListings(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting prospective listings for userId: {}", userId);
    return userService.getProspectiveListings(userId);
  }


  @RequestMapping(value = "/getUserRelatedListings", method = RequestMethod.POST)
  @ResponseBody
  public List<Listing> getUserRelatedListings(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting user related listings for userId: {}", userId);
    return userService.getUserRelatedListings(userId);
  }

  @RequestMapping(value = "/getOwnerRelatedListings", method = RequestMethod.POST)
  @ResponseBody
  public List<Listing> getOwnerRelatedListings(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting owner related listings for userId: {}", userId);
    return userService.getOwnerRelatedListings(userId);
  }

  @RequestMapping(value = "/updateShortlistedListingToScheduleRequest", method = RequestMethod.POST)
  @ResponseBody
  public boolean updateShortlistedListingToScheduleRequest(@RequestParam("userId") String userId) {
    LOGGER.debug("Update shortlisted listings to schedule request for userId: {}", userId);
    return userService.updateShortlistedListingToScheduleRequest(userId);
  }

  @RequestMapping(value = "/updateUserPrefVisitSlot", method = RequestMethod.POST)
  @ResponseBody
  public boolean updateUserPrefVisitSlot(@RequestBody UserRequest userRequest) {
    LOGGER.debug("Updating userPrefVisitSlot with userRequest: {}", userRequest);
    return userService.updateUserPrefVisitSlot(userRequest);
  }

  @RequestMapping(value = "/getUserPrefVisitSlot", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public UserPrefVisitSlot getUserPrefVisitSlot(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting userPrefVisitSlot for userId: {}", userId);
    return userService.getUserPrefVisitSlot(userId);
  }

  @MonitorTime(category = "CRSController", subCategory = "getLocalityData")
  @RequestMapping(value = "/getLocalityData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public LocalityData getLocalityData(@RequestParam("listingId") String listingId) {
    LOGGER.info("Retrieving locality data for {} ", listingId);
    return listingService.getLocalityData(listingId);
  }

  @RequestMapping(value = "/generateOtp", method = RequestMethod.POST)
  @ResponseBody
  public boolean generateOtpAndSendSMS(@RequestBody User user) {
    LOGGER.debug("Generating OTP by user: {}", user);
    return userService.generateOtpAndSendSms(user);
  }

  @RequestMapping(value = "/generateOtpByPhone", method = RequestMethod.POST)
  @ResponseBody
  public boolean generateOtpByPhoneAndSendSMS(@RequestBody User user) throws UnsupportedEncodingException {
    LOGGER.debug("Generating OTP by phone for user: {}", user);
    return userService.generateOtpByPhoneAndSendSms(user);
  }

  @RequestMapping(value = "/generateSmsAndMailForScheduling", method = RequestMethod.POST)
  @ResponseBody
  public boolean generateSmsAndMailForScheduling(@RequestBody User user) {
    LOGGER.debug("Generating sms and mail for scheduling for user: {}", user);
    try {
      userService.generateSmsAndMailForScheduling(user);
    } catch (Exception ex) {
      // eat all exceptions as we dont want to fail on ui if we were not able to send confirmation, lets log it and send the info later
      LOGGER.error(String.format("Unable to send sms/email for user with mobile number %s", user.getUserMobile()), ex);
    }

    return true;
  }


  @RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
  @ResponseBody
  public boolean validateOtp(@RequestBody User user) {
    LOGGER.debug("Validating OTP for user: {}", user);
    return userService.validateOtp(user);
  }

  @RequestMapping(value = "/validateOtpByPhone", method = RequestMethod.POST)
  @ResponseBody
  public boolean validateOtpByPhone(@RequestBody User user) {
    LOGGER.debug("Validating OTP by phone for user: {}", user);
    return userService.validateOtpByPhone(user);
  }

  @RequestMapping(value = "/getDefaultListingAmenities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Map<String, Boolean> getDefaultListingAmenities() {
    return listingService.getDefaultListingAmenities();
  }

  @RequestMapping(value = "/getDefaultApartmentAmenities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Map<String, Boolean> getDefaultApartmentAmenities() {
    return apartmentService.getDefaultApartmentAmenities();
  }

  @RequestMapping(value = "/getAllApartments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public List<Apartment> getAllApartments() {
    return apartmentService.getAllApartments();
  }

  @RequestMapping(value = "/getApartment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Apartment getApartment(@RequestParam("apartmentId") String apartmentId) {
    LOGGER.debug("Getting apartment using apartmentId: {}", apartmentId);
    return apartmentService.getApartment(apartmentId);
  }

  @RequestMapping(value = "/findByPhone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public User findByPhone(@RequestBody User user, BindingResult result) {
    LOGGER.debug("Finding user by phone: {}", user.getUserMobile());
    try {
      if (!result.hasErrors()) {
        User regUser = userService.findByPhone(user.getUserMobile());

        if (null != regUser) {
          return regUser;
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return null;
  }

  @RequestMapping(value = "/userExistsByPhone", method = RequestMethod.POST)
  @ResponseBody
  public boolean userExistsByPhone(@RequestParam("userPhone") String userPhone) {
    LOGGER.debug("Checking if user exists by phone: {}", userPhone);
    try {
      if (null != userService.findByPhone(userPhone)) {
        return true;
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return false;
  }

  @RequestMapping(value = "/csrf", method = RequestMethod.GET)
  @ResponseBody
  public CsrfToken csrf(CsrfToken token) {
    return token;
  }

  @RequestMapping(value = "/createOrUpdateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public User createOrUpdateUser(@RequestBody @Valid User user, BindingResult result) {
    int registered = 0;
    User regUser = null;

    try {
      if (!result.hasErrors()) {
        LOGGER.debug("creating or updating user :{}", user);
        registered = userService.registerNewUserAccount(user);
        if (registered == 1) {
          regUser = userService.findByPhone(user.getUserMobile());
        }
      }
    } catch (InvalidAuthorityException e) {
      LOGGER.error("Unable to assign authority to user");
      return null;
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return null;
    }

    return regUser;
  }

  @RequestMapping(value = "/findByFacebookId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public User findByFacebookId(@RequestBody User user, BindingResult result) {

    try {
      if (!result.hasErrors() && StringUtils.isNotEmpty(user.getUserFacebookId())) {
        LOGGER.debug("Finding user by facebookId: {}", user.getUserFacebookId());
        User regUser = userService.findByFacebookId(user.getUserFacebookId());

        if (null != regUser) {
          return regUser;
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return null;
  }

  @RequestMapping(value = "/findByGoogleId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public User findByGoogleId(@RequestBody User user, BindingResult result) {
    try {
      if (!result.hasErrors() && StringUtils.isNotEmpty(user.getUserGoogleId())) {
        LOGGER.debug("Finding user by googleId: {}", user.getUserGoogleId());
        User regUser = userService.findByGoogleId(user.getUserGoogleId());

        if (null != regUser) {
          return regUser;
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return null;
  }

  @MonitorTime(category = "CRSController", subCategory = "uploadListingImage")
  @RequestMapping(value = "/uploadListingImage", method = RequestMethod.POST)
  @ResponseBody
  public List<CRSImage> uploadListingImage(@RequestParam("listingId") String listingId, @RequestParam("imageFile") MultipartFile[] imageFiles) {
    LOGGER.debug("Uploading images for listingId: {}", listingId);
    return fileHandlingService.uploadListingImages(listingId, imageFiles);
  }

  @MonitorTime(category = "CRSController", subCategory = "getListingImage")
  @RequestMapping(value = "/images/listing/{listingId}/{imageId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseBody
  public byte[] getListingImage(@PathVariable String listingId, @PathVariable String imageId) throws IOException {
    LOGGER.debug("Getting image for listing {}, image {}", listingId, imageId);
    Resource file = fileHandlingService.loadListingImage(listingId, imageId);
    if (file != null) {
      LOGGER.debug(file.getURI()
          .toString());
      return IOUtils.toByteArray(file.getURI());
    }
    return null;
  }

  @MonitorTime(category = "CRSController", subCategory = "uploadApartmentImage")
  @RequestMapping(value = "/uploadApartmentImage", method = RequestMethod.POST)
  @ResponseBody
  public List<CRSImage> uploadApartmentImage(@RequestParam("apartmentId") String apartmentId, @RequestParam("imageFile") MultipartFile[] imageFiles) {
    LOGGER.debug("Uploading images for apartmentId: {}", apartmentId);
    return fileHandlingService.uploadApartmentImages(apartmentId, imageFiles);
  }

  @MonitorTime(category = "CRSController", subCategory = "getApartmentImage")
  @RequestMapping(value = "/images/apartment/{apartmentId}/{imageId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseBody
  public byte[] getApartmentImage(@PathVariable String apartmentId, @PathVariable String imageId) throws IOException {
    LOGGER.debug("Getting image for apartment {}, image {}", apartmentId, imageId);
    Resource file = fileHandlingService.loadApartmentImage(apartmentId, imageId);
    if (file != null) {
      LOGGER.debug(file.getURI()
          .toString());
      return IOUtils.toByteArray(file.getURI());
    }
    return null;
  }

  @MonitorTime(category = "CRSController", subCategory = "addUserRequestCallback")
  @RequestMapping(value = "/addUserRequestCallback", method = RequestMethod.POST)
  @ResponseBody
  public boolean addUserRequestCallback(@RequestParam("phoneNo") String phoneNo) {
    LOGGER.debug("Adding requestCallBack for phone: {}", phoneNo);
    return userService.addUserRequestCallback(phoneNo);
  }

  @MonitorTime(category = "CRSController", subCategory = "retrieveUserScheduleDetails")
  @RequestMapping(value = "/retrieveUserScheduleDetails", method = RequestMethod.POST)
  @ResponseBody
  public boolean retrieveUserScheduleDetails() {
    if (userService.retrieveUserSchedule()) {
      return true;
    }
    return false;
  }

  @MonitorTime(category = "CRSController", subCategory = "updateUserTags")
  @RequestMapping(value = "/updateUserTags", method = RequestMethod.POST)
  @ResponseBody
  public boolean updateUserTags(@RequestParam("userId") String userId, @RequestParam("tagIdList") List<String> tagIdList) {
    LOGGER.debug("Updating userId: {}, userTags: {}", userId, tagIdList);
    if (tagService.updateUserTags(userId, tagIdList)) {
      notificationService.addUserNotification(userId);
      LOGGER.debug("Updated userId: {} tags", userId);
      return true;
    }
    return false;
  }

  @MonitorTime(category = "CRSController", subCategory = "getAllTags")
  @RequestMapping(value = "/getAllTags", method = RequestMethod.GET)
  @ResponseBody
  public List<Tag> getAllTags() {
    return tagService.getAllTags();
  }

  @MonitorTime(category = "CRSController", subCategory = "getUserTags")
  @RequestMapping(value = "/getUserTags", method = RequestMethod.POST)
  @ResponseBody
  public List<Tag> getUserTags(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting user tag for userId: {}", userId);
    List<Tag> userTags = tagService.getUserTags(userId);
    LOGGER.debug("UserId: {} , UserTags:{}", userId, userTags);
    return userTags;
  }

  @MonitorTime(category = "CRSController", subCategory = "getBrokerOrgByUserId")
  @RequestMapping(value = "/getBrokerOrgByUserId", method = RequestMethod.POST)
  @ResponseBody
  public BrokerOrg getBrokerOrgByUserId(@RequestParam("userId") String userId) {
    LOGGER.debug("Getting brokerOrg for userId: {}", userId);
    BrokerOrg brokerOrg = brokerOrgService.getBrokerOrgByUserId(userId);
    LOGGER.debug("UserId: {} , brokerOrg:{}", userId, brokerOrg);
    return brokerOrg;
  }

  @MonitorTime(category = "CRSController", subCategory = "getRecentlyAddedListings")
  @RequestMapping(value = "/getRecentlyAddedListings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public List<Listing> getRecentlyAddedListings() {
    LOGGER.debug("Getting recentlyAddedListings");
    return listingService.getRecentlyAddedListings();
  }

  @MonitorTime(category = "CRSController", subCategory = "getOwnerDetails")
  @RequestMapping(value = "/getOwnerDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public User getOwnerDetails(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId) {
    LOGGER.debug("Getting ownerDetails for listing: {}", listingId);
    return userService.getOwnerDetails(listingId, userId);
  }

  @MonitorTime(category = "CRSController", subCategory = "getPropertyAndUserCount")
  @RequestMapping(value = "/getPropertyAndUserCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public CountEntity getPropertyAndUserCount() {
    LOGGER.debug("Getting count for listing and user");
    int listingCount = listingService.getListingCount();

    int userCount = userService.getUserCount();

    return CountEntity.builder().userCount(userCount).listingCount(listingCount).build();
  }

  @MonitorTime(category = "CRSController", subCategory = "getMarathahalliListingCount")
  @RequestMapping(value = "/getMarathahalliListingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public int getMarathahalliListingCount() {
    LOGGER.debug("Retrieving listing count for Marathahalli");

   FilterRequest filterRequest = getAreaFilterRequest(12.9591722, 77.69741899999997);

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return 0;
    }

    List<Listing> listings = listingService.getActiveListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved for Marathahalli {}", listings.size());
    }

    return listings.size();
  }

  @MonitorTime(category = "CRSController", subCategory = "getBTMListingCount")
  @RequestMapping(value = "/getBTMListingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public int getBTMListingCount() {
    LOGGER.debug("Retrieving listing count for BTM");

    FilterRequest filterRequest = getAreaFilterRequest(12.9165757, 77.61011630000007);

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return 0;
    }

    List<Listing> listings = listingService.getActiveListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved for BTM{}", listings.size());
    }

    return listings.size();
  }

  @MonitorTime(category = "CRSController", subCategory = "getKoramangalaListingCount")
  @RequestMapping(value = "/getKoramangalaListingCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public int getKoramangalaListingCount() {
    LOGGER.debug("Retrieving listing count for Koramangala");

    FilterRequest filterRequest = getAreaFilterRequest(12.9352273, 77.62443310000003);

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return 0;
    }

    List<Listing> listings = listingService.getActiveListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved for Koramangala{}", listings.size());
    }

    return listings.size();
  }

  private FilterRequest getAreaFilterRequest(double lat, double lng) {

    List<Filter> filterList = new ArrayList<>(3);

    filterList.add(new AreaFilter(lat, lng, 5));

    filterList.add(getBedroomCountFilter());

    List<FilterCondition> listingTypeFilterConditions = new ArrayList<>(2);
    listingTypeFilterConditions.add(new FilterCondition("Rent", true));
    listingTypeFilterConditions.add(new FilterCondition("Sale", false));
    filterList.add(new ListingTypeFilter(listingTypeFilterConditions));

    return new FilterRequest(filterList);
  }

  @MonitorTime(category = "CRSController", subCategory = "getActiveListingCount")
  @RequestMapping(value = "/getActiveListingCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public int getActiveListingCount(@RequestBody FilterRequest filterRequest) {
    LOGGER.debug("Retrieving listing count for filterRequest :{} ", crsMarshaller.marshall(filterRequest));

    if (!isFilterRequestValid(filterRequest)) {
      LOGGER.debug("Invalid FilterRequest");
      return 0;
    }

    List<Listing> listings = listingService.getActiveListings(filterRequest);

    if (null != listings) {
      LOGGER.debug("Number of listings retrieved {}", listings.size());
    }

    return listings.size();
  }

  @MonitorTime(category = "CRSController", subCategory = "getRazorpayOrder")
  @RequestMapping(value = "/getRazorpayOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public RazorpayOrder getRazorpayOrder(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId, @RequestParam("amount") String amount) {
    LOGGER.debug("Getting razorpayOrderId for listingId: {}, userId: {}, amount: {}", listingId, userId, amount);
    return razorpayService.getRazorpayOrder(listingId, userId, Integer.parseInt(amount));
  }

  @MonitorTime(category = "CRSController", subCategory = "updateRazorpayOrderStatus")
  @RequestMapping(value = "/updateRazorpayOrderStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public boolean updateRazorpayOrderStatus(@RequestParam("razorpayOrderId") String razorpayOrderId, @RequestParam("razorpayPaymentId") String razorpayPaymentId) {
    LOGGER.debug("Updating razorpayOrderStatus for razorpayOrderId: {}, razorpayPaymentId: {}", razorpayOrderId, razorpayPaymentId);
    return razorpayService.updateRazorpayOrderStatus(razorpayOrderId, razorpayPaymentId);
  }

  @MonitorTime(category = "CRSController", subCategory = "reportRentedOut")
  @RequestMapping(value = "/reportRentedOut", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public boolean reportRentedOut(@RequestParam("listingId") String listingId, @RequestParam("userId") String userId) {
    LOGGER.debug("Reporting rented out for listingId: {}, userId: {}", listingId, userId);
    return razorpayService.reportRentedOut(listingId, userId);
  }

}
