package com.crs.denzip.core.scheduler;

import com.crs.denzip.core.service.NotificationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class ListingNotificationScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListingNotificationScheduler.class);

  private final ScheduledExecutorService listingNotificationExecutor = Executors.newSingleThreadScheduledExecutor();

  private final NotificationService notificationService;
  private final int initialDelay;
  private final int listingNotificationFrequency;
  private final int brokerNotificationFrequency;

  @PostConstruct
  public void init() {
    scheduleListingNotifications();
    scheduleBrokerUserNotifications();
  }

  private void scheduleListingNotifications() {
    long delay = initialDelay * 1000;
    long period = listingNotificationFrequency * 60 * 60 * 1000;
    Runnable task = () -> {
      try {
        notificationService.sendListingNotifications();
        LOGGER.debug("Task from {} has executed successfully", this);
      } catch (Throwable t) {
        LOGGER.error("The scheduled task has encountered error", t);
        // We should NOT reschedule it in this scenario as initialDelay and frequency is too high. It will just keep on running after ever minute.
      }
    };
    LOGGER.debug("Task {} is scheduled to run after {} and recur after every {}", task, delay, period);
    listingNotificationExecutor.scheduleWithFixedDelay(task, delay, period, TimeUnit.MILLISECONDS);
  }

  private void scheduleBrokerUserNotifications() {
    long delay = initialDelay * 1000;
    long period = brokerNotificationFrequency * 60 * 60 * 1000;
    Runnable task = () -> {
      try {
        notificationService.sendBrokerUserNotifications();
        LOGGER.debug("Task from {} has executed successfully", this);
      } catch (Throwable t) {
        LOGGER.error("The scheduled task has encountered error", t);
        // We should NOT reschedule it in this scenario as initialDelay and frequency is too high. It will just keep on running after ever minute.
      }
    };
    LOGGER.debug("Task {} is scheduled to run after {} and recur after every {}", task, delay, period);
    listingNotificationExecutor.scheduleWithFixedDelay(task, delay, period, TimeUnit.MILLISECONDS);
  }

}
