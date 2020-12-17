package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.BrokerUserNotification;
import com.crs.denzip.model.entities.ListingNotification;
import com.crs.denzip.persistence.dao.NotificationDAO;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

  private static final String LISTING_NOTIFICATION_SMS_MESSAGE = "%1$s new properties have been added in Denzip matching your requirements. Please check them out at Prospective Listings";
  private static final String LISTING_NOTIFICATION_EMAIL_MESSAGE = "Hello,\n\nGreetings from Denzip!!\n\nFollowing new properties might be of interest to you\n\n"
                                                                   + "%1$s"
                                                                   + "You can also check them out at Prospective Listings\n\n"
                                                                   + "Please reply to this email with STOP to stop these notifications\n\n"
                                                                   + "Regards,\n"
                                                                   + "Team Denzip\n"
                                                                   + "www.denzip.com";




  private final UserService userService;
  private final NotificationDAO notificationDAO;

  public boolean addListingNotification(String listingId) {
    return this.notificationDAO.addListingNotification(listingId);
  }

  public void sendListingNotifications() {
    // First get data for all listings which are pending Notifications -
    // 1. listingId
    // 2. tag associated with listingId
    // 3. userIds associated with these tags
    // 4. user name, phone & email
    // 5. Group them by userId

    List<ListingNotification> listingNotificationList =  notificationDAO.getPendingListingNotifications();
    LOGGER.debug("Pending ListingNotifications: {}", listingNotificationList);

    Set<String> listingIdSet = new HashSet<>();

    StringBuilder adminEmailData = new StringBuilder();

    for(ListingNotification listingNotification: listingNotificationList) {
      //Add to prospective listing before sending out notification
      if (StringUtils.isNotEmpty(listingNotification.getUserId())) {
        int[] prospectiveListingsCountArr = userService.addUserProspectiveListingMapping(listingNotification.getUserId(), listingNotification.getListingIds());
        long prospectiveListingsCount = Arrays.stream(prospectiveListingsCountArr).filter(i -> i > 0).count();
        LOGGER.debug("Prospective Listings Count: {}", prospectiveListingsCount);
        if (prospectiveListingsCount > 0) {
          String message = String.format(LISTING_NOTIFICATION_SMS_MESSAGE, listingNotification.getListingIds().size());
          userService.sendSms(listingNotification.getUserMobile(), message);

          StringBuilder listingLinks = new StringBuilder();
          for(String listingId : listingNotification.getListingIds()) {
            String listingLink = "https://www.denzip.com/listing-detail/" + listingId;
            listingLinks.append(listingLink).append("\n\n");
          }


          userService.sendEmail(listingNotification.getUserEmail(), "Denzip New Properties For You", String.format(LISTING_NOTIFICATION_EMAIL_MESSAGE,listingLinks.toString()));
          adminEmailData.append("New User Notification ").append(listingNotification.getUserEmail()).append(" ").append(listingNotification.getUserMobile()).append("\n\n").append(listingLinks.toString()).append("\n\n");
        }
      }

      // update listing notification status even if no user is subscribed to its tag
      listingIdSet.addAll(listingNotification.getListingIds());
    }

    if(!CollectionUtils.isEmpty(listingIdSet)) {
      notificationDAO.updateListingNotification(new ArrayList<>(listingIdSet));
    }

    if(StringUtils.isNotEmpty(adminEmailData.toString())) {
      userService.emailDenzipAdmins("New Prospective Listings For All Users", adminEmailData.toString());
    }

  }

  public boolean addUserNotification(String userId) {
    return this.notificationDAO.addUserNotification(userId);
  }


  public void sendBrokerUserNotifications() {
    // First get data for all users which are pending Notifications to brokers -
    // 1. userId
    // 2. tag associated with userId
    // 3. brokerOrgIds associated with these tags
    // 4. brokerOrg name, phone & email
    // 5. Group them by brokerOrgId

    List<BrokerUserNotification> brokerUserNotificationList =  notificationDAO.getPendingBrokerUserNotifications();
    LOGGER.debug("Pending BrokerUserNotificationList: {}", brokerUserNotificationList);

    Set<String> userIdSet = new HashSet<>();


    for(BrokerUserNotification brokerUserNotification: brokerUserNotificationList) {

      Map<String, Long> tagCount = brokerUserNotification
          .getTagNames()
          .stream()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      LOGGER.debug("tagCount: {}", tagCount);

      String tagCountAsString = Joiner.on("\n").withKeyValueSeparator("=").join(tagCount);
      LOGGER.debug("tagCountAsString: {}", tagCountAsString);

      String message = "Denzip has some new prospective tenants searching for: \n" + tagCountAsString;

      userService.sendSms(brokerUserNotification.getBrokerOrgMobile(), message);
      userService.sendEmail(brokerUserNotification.getBrokerOrgEmail(), "Denzip New Prospective Tenants For You", message);
      userIdSet.addAll(brokerUserNotification.getUserIds());
    }

    if(!CollectionUtils.isEmpty(userIdSet)) {
      notificationDAO.updateUserNotification(new ArrayList<>(userIdSet));
    }
  }

}
