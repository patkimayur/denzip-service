package com.crs.denzip.core.scheduler;

import com.crs.denzip.core.service.DeactivationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ListingStatusUpdateScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListingStatusUpdateScheduler.class);

  private final ScheduledExecutorService listingValidityChecker = Executors.newSingleThreadScheduledExecutor();
  private final String zoneId;
  private final int recurringPeriodInHour;
  private final DeactivationService deactivationService;


  @PostConstruct
  public void init() {
    long delay = getDelay();
    long period = recurringPeriodInHour * 60 * 60 * 1000;
    Runnable task = () -> {
      try {
        deactivationService.runDeactivationJob();
        LOGGER.debug("Task from {} has executed successfully", this);
      } catch (Throwable t) {
        LOGGER.error("The scheduled task has encountered error", t);
        // The error might be due to code issue or env issue so lets reschedule it to run again in case it is env issue
        this.init();
      }
    };
    LOGGER.debug("Task {} is scheduled to run after {} and recur after every {}", task, delay, period);
    listingValidityChecker.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
  }

  private long getDelay() {
    LocalTime midnight = LocalTime.MIDNIGHT;
    LocalDate today = LocalDate.now(ZoneId.of(zoneId));
    LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
    LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
    return ZonedDateTime.of(tomorrowMidnight, ZoneId.of(zoneId)).toInstant().minusMillis(System.currentTimeMillis()).toEpochMilli();
  }
}
