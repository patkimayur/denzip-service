package com.crs.denzip.core.service;

import com.crs.denzip.core.pool.AsyncExecutorPool;
import com.crs.denzip.model.constants.ItineraryStatusConstants;
import com.crs.denzip.model.constants.ScheduleStatusConstants;
import com.crs.denzip.model.entities.Authority;
import com.crs.denzip.model.entities.DaySchedule;
import com.crs.denzip.model.entities.DeactivateListingDetails;
import com.crs.denzip.model.entities.DeactivationType;
import com.crs.denzip.model.entities.Listing;
import com.crs.denzip.model.entities.User;
import com.crs.denzip.model.entities.UserPrefVisitSlot;
import com.crs.denzip.model.entities.UserSchedule;
import com.crs.denzip.model.entities.VisitSlot;
import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.model.exception.InvalidAuthorityException;
import com.crs.denzip.persistence.communication.FilterRequest;
import com.crs.denzip.persistence.communication.UserRequest;
import com.crs.denzip.persistence.dao.UserDAO;
import com.crs.denzip.persistence.filters.OwnerIdFilter;
import com.crs.denzip.persistence.filters.UserStatusFilter;
import com.crs.denzip.security.otpgen.TimeBasedOneTimePasswordGenerator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Transactional(rollbackFor = {CRSException.class})
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  private static final String INDIA_PHONE_COUNTRY_CODE = "91";


  private final UserDAO userDAO;
  private final ListingService listingService;
  private final AsyncExecutorPool asyncExecutorPool;
  private final String smsAPIKey;
  private final String smsMessage;
  private final String smsMessageForSchedulingConfirmation;
  private final String smsMessageForRequestCallback;
  private final String smsMessageForDenzipAdmin;
  private final String smsMessageForDenzipAdminForSchedule;
  private final String mailSubjectForScheduleVisit;
  private final String mailTextForScheduleVisit;
  private final List<String> denzipAdminPhones;
  private final List<String> denzipAdminEmails;
  private final boolean isSMSTest;
  private final TimeBasedOneTimePasswordGenerator timeBasedOneTimePasswordGenerator;
  private final JavaMailSender emailSender;
  private final String mailSubjectForScheduling;
  private final String mailTextForScheduling;
  private final String mailSubjectForRequestCallback;
  private final String mailTextForRequestCallback;

  public boolean addShortlistedListing(String listingId, String userId) {
    return addUserListingMapping(listingId, userId, "SHORTLISTED");
  }

  public boolean addUserListingMapping(String listingId, String userId, String scheduleStatus) {
    int result = userDAO.addUserListingMapping(listingId, userId, scheduleStatus);
    return result == 1;
  }

  public boolean removeShortlistedListing(String listingId, String userId) {
    int result = userDAO.removeListingWithStatus(listingId, userId, ScheduleStatusConstants.SHORTLISTED);
    return result == 1;
  }

  public boolean removeProspectiveListing(String listingId, String userId) {
    int result = userDAO.removeListingWithStatus(listingId, userId, ScheduleStatusConstants.PROSPECTIVE);
    return result == 1;
  }

  public boolean updateShortlistedListingToScheduleRequest(String userId) {
    int result = userDAO.updateShortlistedListingToScheduleRequest(userId);
    return result >= 1;
  }

  public boolean updateAuthorityToBroker(List<String> userPhoneNumbers) {
    return userDAO.updateAuthorityToBroker(userPhoneNumbers);
  }

  public int getUserCount() {
    return userDAO.getUserCount();
  }

  public boolean updateUserPrefVisitSlot(UserRequest userRequest) {
    int result = userDAO.updateUserPrefVisitSlot(userRequest.getUserId(), userRequest.getUserPrefVisitSlot());
    return result == 1;
  }

  public List<Listing> getShortlistedListings(String userId) {
    List<Listing> listings =
        listingService.getAllListings(new FilterRequest(Collections.singletonList(new UserStatusFilter(userId, new ArrayList<>(Arrays.asList("SHORTLISTED"))))));

    return listings;
  }

  public List<Listing> getProspectiveListings(String userId) {
    List<Listing> listings =
        listingService.getAllListings(new FilterRequest(Collections.singletonList(new UserStatusFilter(userId, new ArrayList<>(Arrays.asList("PROSPECTIVE"))))));

    return listings;
  }

  public List<Listing> getUserRelatedListings(String userId) {
    List<Listing> listings =
        listingService.getAllListings(new FilterRequest(Collections.singletonList(new UserStatusFilter(userId, new ArrayList<>(Arrays.asList("SCHEDULING_REQUESTED", "SCHEDULED", "VISITED", "ACCEPTED", "REJECTED"))))));

    return listings;
  }

  public List<Listing> getOwnerRelatedListings(String ownerId) {
    List<Listing> listings =
        listingService.getAllListings(new FilterRequest(new ArrayList<>(Arrays.asList(new OwnerIdFilter(Collections.singletonList(ownerId)), new UserStatusFilter(ownerId, Arrays.asList("ACTIVATED", "DEACTIVATED", "PENDING_ACTIVATION", "SYSTEM_PENDING_DEACTIVATION", "SYSTEM_DEACTIVATED"))))));

    return listings;
  }

  public UserPrefVisitSlot getUserPrefVisitSlot(String userId) {
    return userDAO.getUserPrefVisitSlot(userId);
  }

  public int registerNewUserAccount(User user) throws InvalidAuthorityException {

    // the role is not currently used but can be used along with authorities to define finer user control
    user.setRoles(Collections.singletonList("ROLE_USER"));

    Authority a = new Authority(1L, "ROLE_USER");
    List<Authority> authorities = new ArrayList<>();
    authorities.add(a);
    user.setAuthorities(authorities);
    return userDAO.saveUserDetails(user);
  }

  public boolean deactivateListing(DeactivateListingDetails deactivateListingDetails) throws CRSException {
    boolean status = listingService.updateListingStatus(deactivateListingDetails.getListingId(), false);

    if (!status) {
      throw new CRSException("Unable to deactivate listing");
    }

    boolean isUserListingMappingAdded =
        addUserListingMapping(deactivateListingDetails.getListingId(), deactivateListingDetails.getOwnerId(), "DEACTIVATED");

    if (!isUserListingMappingAdded) {
      throw new CRSException("Unable to add user listing mapping for deactivated schedule status");
    }

    if (StringUtils.isBlank(deactivateListingDetails.getTenantUserId())) {
      deactivateListingDetails.setDeactivationType(DeactivationType.OTHER_TENANT);
    } else {
      deactivateListingDetails.setDeactivationType(DeactivationType.DENZIP_USER);
    }

    LOGGER.debug("Listing deactivated with listing id {}", deactivateListingDetails.getListingId());
    String message =
        "Listing Deactivated for listing id - " + deactivateListingDetails.getListingId() + " by owner id - "
        + deactivateListingDetails.getOwnerId();
    this.emailDenzipAdmins(message, message);


    return listingService.addDeactivatedListing(deactivateListingDetails);
  }

  public boolean activateListing(String listingId, String scheduleStatus) throws CRSException {
    boolean status = listingService.updateListingStatus(listingId, true);

    if (!status) {
      throw new CRSException("Unable to activate listing");
    }

    Listing listing = listingService.getListing(listingId);

    if (null != listing) {
      boolean isUserListingMappingAdded = addUserListingMapping(listingId, listing.getUserId(), scheduleStatus);

      if (!isUserListingMappingAdded) {
        throw new CRSException("Unable to add user listing mapping for activate schedule status");
      }

      return true;
    }
    return false;
  }

  public User findByFacebookId(String facebookId) {
    return userDAO.findByFacebookId(facebookId);
  }

  public User findByGoogleId(String googleId) {
    return userDAO.findByGoogleId(googleId);
  }

  public User findByPhone(String phoneNo) {
    return userDAO.findByPhoneNo(phoneNo);
  }

  public boolean generateOtpAndSendSms(User user) {
    String otp = generateOtp(user);
    if (StringUtils.isNotBlank(otp)) {
      if (StringUtils.isNotEmpty(user.getUserMobile()) && user.getUserMobile().startsWith(INDIA_PHONE_COUNTRY_CODE)) {
        String smsStatus = sendSms(user.getUserMobile(), this.smsMessage + " " + otp);
        LOGGER.info("The otp generated for user mobile:{} is {} and status is {}", user.getUserMobile(), otp, smsStatus);
        return true;
      } else {
        // International Mobile, send otp through email
        sendEmail(user.getUserEmail(), "Denzip OTP", this.smsMessage + " " + otp);
        LOGGER.info("The otp generated for user mobile:{} is {} and email has been sent to: {}", user.getUserMobile(), otp, user.getUserEmail());
        return true;
      }
    }
    return false;
  }

  public boolean generateOtpByPhoneAndSendSms(User user) {
    String otp = generateOtpByPhone(user.getUserMobile());
    if (StringUtils.isNotBlank(otp)) {
      if (StringUtils.isNotEmpty(user.getUserMobile()) && user.getUserMobile().startsWith(INDIA_PHONE_COUNTRY_CODE)) {
        String smsStatus = sendSms(user.getUserMobile(), this.smsMessage + " " + otp);
        LOGGER.info("The otp generated for user mobile:{} is {} and status is {}", user.getUserMobile(), otp, smsStatus);
        return true;
      } else {
        // International Mobile, send otp through email
        sendEmail(user.getUserEmail(), "Denzip OTP", this.smsMessage + " " + otp);
        LOGGER.info("The otp generated for user mobile:{} is {} and email has been sent to: {}", user.getUserMobile(), otp, user.getUserEmail());
        return true;
      }
    }
    return false;
  }

  public boolean generateSmsAndMailForScheduling(User user) {
    String smsMessage = String.format(this.smsMessageForSchedulingConfirmation, user.getUserName());
    String smsStatus = sendSms(user.getUserMobile(), this.smsMessageForSchedulingConfirmation);
    LOGGER.info("The scheduling sms for user mobile:{} is {} and smsStatus is {}", user.getUserMobile(), smsMessage, smsStatus);

    if (StringUtils.isNotBlank(user.getUserEmail())) {
      sendEmail(user.getUserEmail(), this.mailSubjectForScheduling, this.mailTextForScheduling);
    }

    informDenzipAdmins(this.smsMessageForDenzipAdminForSchedule + " "
                       + user.getUserMobile(), this.mailSubjectForScheduleVisit, String.format(this.mailTextForScheduleVisit, user.getUserMobile()));

    return true;
  }

  public void sendEmail(String email, String mailSubject, String mailText) {
    sendEmail(Collections.singletonList(email), mailSubject, mailText);
  }

  public void sendEmail(List<String> email, String mailSubject, String mailText) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email.toArray(new String[0]));
    message.setFrom("Denzip Support<support@denzip.com>");
    message.setSubject(mailSubject);
    message.setText(mailText);
    LOGGER.debug("Queing up email for user {}, with subject {}", email, mailSubject);
    asyncExecutorPool.submit(() -> {
      try {
        LOGGER.debug("Sending email now to {}", message.getTo());
        emailSender.send(message);
        LOGGER.debug("Email sent to {}", message.getTo());
      } catch (Throwable t) {
        LOGGER.error("Error occured while trying to send email to {}", email);
        LOGGER.error(t.getMessage(), t);
      }
    });
  }

  public boolean validateOtp(User user) {
    String otp = generateOtp(user);
    LOGGER.debug("OTP generated during validation : " + otp);

    boolean result = Objects.equals(otp, user.getOtp());
    if (!result) {
      otp = generateLastOtp(user);
      LOGGER.debug("Last OTP generated during validation : " + otp);
      result = Objects.equals(otp, user.getOtp());
    }
    LOGGER.debug(result ? String.format("OTP validation passed for %s", user.getUserId()) : String.format("OTP validation failed for %s", user.getUserId()));
    return result;
  }

  public boolean validateOtpByPhone(User user) {
    String otp = generateOtpByPhone(user.getUserMobile());
    LOGGER.debug("OTP generated during validation : " + otp);

    boolean result = Objects.equals(otp, user.getOtp());
    if (!result) {
      otp = generateLastOtpByPhone(user.getUserMobile());
      LOGGER.debug("Last OTP generated during validation : " + otp);
      result = Objects.equals(otp, user.getOtp());
    }
    LOGGER.debug(result ? String.format("OTP validation passed for %s", user.getUserId()) : String.format("OTP validation failed for %s", user.getUserId()));
    return result;
  }

  public boolean addUserRequestCallback(String phoneNo) {
    int result = userDAO.addUserRequestCallback(phoneNo);
    String smsStatus = sendSms(phoneNo, this.smsMessageForRequestCallback);
    LOGGER.info("The scheduling sms for user mobile:{} is {} and smsStatus is {}", phoneNo, this.smsMessageForRequestCallback, smsStatus);

    informDenzipAdmins(this.smsMessageForDenzipAdmin + " "
                       + phoneNo, this.mailSubjectForRequestCallback, String.format(this.mailTextForRequestCallback, phoneNo));

    return result == 1;
  }

  private void informDenzipAdmins(String smsMessage, String mailSubject, String mailText) {
    // convey to denzip admins as well - SMS
    this.denzipAdminPhones.stream()
        .forEach(ph -> sendSms(ph, smsMessage));

    // send email
    sendEmail(this.denzipAdminEmails, mailSubject,
        mailText);
  }

  public void smsDenzipAdmins(String text) {
    this.denzipAdminPhones.stream()
        .forEach(ph -> sendSms(ph, text));
  }

  public void emailDenzipAdmins(String mailSubject, String mailText) {
    // send email
    sendEmail(this.denzipAdminEmails, mailSubject,
        mailText);
  }

  public boolean retrieveUserSchedule() {
    // refresh the view first thing so latest data is up to date in view
    userDAO.refreshUserScheduleView();

    List<UserSchedule> userSchedules = userDAO.retrieveUserSchedule();

    if (!CollectionUtils.isEmpty(userSchedules)) {
      userSchedules.stream().forEach(us -> {
        UserPrefVisitSlot ownerPref = us.getOwnerPrefVisitSlot();
        UserPrefVisitSlot tenantPref = us.getTenantPrefVisitSlot();
        UserPrefVisitSlot potentialPrefVisitSlot = null;

        if (null == ownerPref && null == tenantPref) {
          LOGGER.error("Owner and/or Tenant user preferences not set, no records will be updated for user id {} listing id {}", us.getTenantId(), us.getListingId());
        } else if (null == ownerPref && null != tenantPref) {
          potentialPrefVisitSlot = filterPref(tenantPref);
        } else if (null != ownerPref && null == tenantPref) {
          potentialPrefVisitSlot = filterPref(ownerPref);
        } else {
          potentialPrefVisitSlot = findIntersection(ownerPref, tenantPref);
        }

        if (null == potentialPrefVisitSlot || CollectionUtils.isEmpty(potentialPrefVisitSlot.getDayScheduleList())) {
          LOGGER.info("No user preference to be set for user id {} listing id {}", us.getTenantId(), us.getListingId());
          LOGGER.info("Updating the record saying system cannot match, use manual method");
          userDAO.updateItineraryStatus(us.getTenantId(), us.getListingId(), ItineraryStatusConstants.NO_MATCH_FOUND);
        } else {
          // have to store the potential pref slot and change the status to creation in progress
          userDAO.updatePotentialPrefSlot(us.getTenantId(), us.getListingId(), potentialPrefVisitSlot);
        }
      });
    }
    // refresh the view here so latest data is up to date in view
    userDAO.refreshUserScheduleView();
    return true;
  }

  private UserPrefVisitSlot filterPref(UserPrefVisitSlot pref) {
    List<DaySchedule> daySchedules = pref.getDayScheduleList();
    List<DaySchedule> potentialScheduleList = new ArrayList<>();

    if (!CollectionUtils.isEmpty(daySchedules)) {
      daySchedules.stream().forEach(daySchedule -> {
        List<VisitSlot> potentialVisitSlots = new ArrayList<>();
        if (!CollectionUtils.isEmpty(daySchedule.getVisitSlots())) {
          daySchedule.getVisitSlots().stream().forEach(visitSlot -> {
            if (visitSlot.isApplied()) {
              potentialVisitSlots.add(visitSlot);
            }
          });
          if (!CollectionUtils.isEmpty(potentialVisitSlots)) {
            DaySchedule potentialDaySchedule = DaySchedule.builder().endTime(daySchedule.getEndTime()).name(daySchedule.getName())
                .selected(daySchedule.isSelected()).startTime(daySchedule.getStartTime()).visitSlots(potentialVisitSlots).build();
            potentialScheduleList.add(potentialDaySchedule);
          }
        }
      });
    }

    return UserPrefVisitSlot.builder().dayScheduleList(potentialScheduleList).build();
  }

  private UserPrefVisitSlot findIntersection(UserPrefVisitSlot ownerPref, UserPrefVisitSlot tenantPref) {
    List<DaySchedule> ownerDaySchedules = ownerPref.getDayScheduleList();
    List<DaySchedule> tenantDaySchedules = tenantPref.getDayScheduleList();
    List<DaySchedule> potentialScheduleList = new ArrayList<>();

    if (!CollectionUtils.isEmpty(ownerDaySchedules) && !CollectionUtils.isEmpty(tenantDaySchedules)) {
      ownerDaySchedules.stream().forEach(ownerDaySchedule -> {
        tenantDaySchedules.stream().forEach(tenantDaySchedule -> {
          if (StringUtils.isNotBlank(tenantDaySchedule.getName())
              && StringUtils.isNotBlank(ownerDaySchedule.getName())
              && ownerDaySchedule.getName().equals(tenantDaySchedule.getName())) {
            List<VisitSlot> potentialVisitSlots = new ArrayList<>();
            if (!CollectionUtils.isEmpty(tenantDaySchedule.getVisitSlots())
                && !CollectionUtils.isEmpty(ownerDaySchedule.getVisitSlots())) {
              ownerDaySchedule.getVisitSlots().stream().forEach(ownerVisitSlot -> {
                tenantDaySchedule.getVisitSlots().stream().forEach(tenantVisitSlot -> {
                  if (ownerVisitSlot.isApplied() && tenantVisitSlot.isApplied()
                      && StringUtils.isNotBlank(ownerVisitSlot.getName())
                      && StringUtils.isNotBlank(tenantVisitSlot.getName())
                      && ownerVisitSlot.getName().equals(tenantVisitSlot.getName())) {
                    potentialVisitSlots.add(ownerVisitSlot);
                  }
                });
              });
              if (!CollectionUtils.isEmpty(potentialVisitSlots)) {
                DaySchedule potentialDaySchedule = DaySchedule.builder().endTime(ownerDaySchedule.getEndTime()).name(ownerDaySchedule.getName()).selected(ownerDaySchedule.isSelected()).startTime(ownerDaySchedule.getStartTime()).visitSlots(potentialVisitSlots).build();
                potentialScheduleList.add(potentialDaySchedule);
              }
            } else {
              LOGGER.error("Unable to find visit slots for owner and/or tenant ");
            }
          }
        });
      });
    } else {
      LOGGER.error("Unable to find day schedule for owner and/or tenant");
    }

    return UserPrefVisitSlot.builder().dayScheduleList(potentialScheduleList).build();

  }

  private String generateOtp(User user) {
    try {
      Date now = new Date();
      byte[] encodedSecret = user.getSecret();
      SecretKey originalKey = new SecretKeySpec(encodedSecret, TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512);
      return this.timeBasedOneTimePasswordGenerator.generateOneTimePassword(originalKey, now);
    } catch (InvalidKeyException e) {
      LOGGER.error("Error while generating otp - Invalid secret key", e);
    }
    return null;
  }

  private String generateOtpByPhone(String userMobile) {
    try {
      Date now = new Date();
      SecretKey originalKey = new SecretKeySpec(getMobileSecretKey(userMobile), TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512);
      return this.timeBasedOneTimePasswordGenerator.generateOneTimePassword(originalKey, now);
    } catch (InvalidKeyException e) {
      LOGGER.error("Error while generating otp - Invalid secret key", e);
    }
    return null;
  }

  private String generateLastOtp(User user) {
    try {
      Date now = new Date();
      byte[] encodedSecret = user.getSecret();
      SecretKey originalKey = new SecretKeySpec(encodedSecret, TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512);
      return this.timeBasedOneTimePasswordGenerator.generateLastValidPassword(originalKey, now);
    } catch (InvalidKeyException e) {
      LOGGER.error("Error while generating otp - Invalid secret key", e);
    }
    return null;
  }

  private String generateLastOtpByPhone(String userMobile) {
    try {
      Date now = new Date();
      SecretKey originalKey = new SecretKeySpec(getMobileSecretKey(userMobile), TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512);
      return this.timeBasedOneTimePasswordGenerator.generateLastValidPassword(originalKey, now);
    } catch (InvalidKeyException e) {
      LOGGER.error("Error while generating otp - Invalid secret key", e);
    }
    return null;
  }

  public String sendSms(String phoneNumber, String smsMessage) {
    try {
      // Construct data
      String apiKey = "apikey=" + this.smsAPIKey;
      String message = "&message=" + smsMessage;
      String sender = "&sender=" + "DENZIP";
      String numbers = "&numbers=" + phoneNumber;
      //when we want msges to be sent to mobile set the test flag to false
      String test = "&test=" + isSMSTest;


      // Send data
      HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
      String data = apiKey + numbers + message + sender + test;
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
      conn.getOutputStream()
          .write(data.getBytes("UTF-8"));
      final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      final StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
        stringBuilder.append(line);
      }
      rd.close();

      String status = stringBuilder.toString();
      LOGGER.debug("Sending SMS to {} with message {} has status {}", phoneNumber, smsMessage, status);
      return status;
    } catch (Exception e) {
      System.out.println("Error SMS " + e);
      return "Error " + e;
    }
  }

  private byte[] getMobileSecretKey(String userMobile) {
    StringBuilder secretMobile = new StringBuilder();
    for (char c : userMobile.toCharArray()) {
      char i = (char) (c % 2 == 0 ? c + 10 : c - 15);
      secretMobile.append(i);
    }
    String secretMobileString = secretMobile.reverse().toString();
    return secretMobileString.getBytes();
  }

  public int[] addUserProspectiveListingMapping(String userId, List<String> listingIdList) {
    return userDAO.addUserProspectiveListingMapping(userId, listingIdList);
  }

  public User findByUserId(String userId) {
    return userDAO.findByUserId(userId);
  }

  public User getOwnerDetails(String listingId, String userId) {
    User owner = null;
    try {
      owner = userDAO.getOwnerDetails(listingId);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    User tenant = findByUserId(userId);

    LOGGER.debug("Customer: {} retrieved ListingId: {} owner details as {}", tenant, listingId, owner);
    if(StringUtils.isNotEmpty(tenant.getUserEmail())) {
      sendEmail(tenant.getUserEmail(), "Denzip - Owner Details Retrieved", String.format("Hello,\n\nOwner details for property https://www.denzip.com/listing-detail/%s are Owner Name: %s, Owner Phone: %s.\n\nRequest you to contact owner within next 24 hours to avoid property getting rented out by someone else.\n\nRegards,\nTeam Denzip", listingId, owner.getUserName(), owner.getUserMobile()));
    }
    String url = null;
    try {
      url = URLEncoder.encode("www.denzip.com/listing-detail/" + listingId,"UTF-8");
    } catch (UnsupportedEncodingException e) {
      url = listingId;
      LOGGER.error(e.getMessage(),e);
    }
    sendSms(tenant.getUserMobile(), String.format("Owner details for property %s are Phone: %s, Name: %s", url, owner.getUserMobile(), owner.getUserName()));

    sendSms(owner.getUserMobile(),String.format("Denzip user is interested in your property and has retrieved your details. Phone: %s, Name: %s", tenant.getUserMobile(), tenant.getUserName()));
    emailDenzipAdmins("Owner Details Retrieved", String.format("Tenant: %s retrieved owner details of Listing: %s as Owner : %s", tenant, listingId, owner));
    return owner;
  }

}
