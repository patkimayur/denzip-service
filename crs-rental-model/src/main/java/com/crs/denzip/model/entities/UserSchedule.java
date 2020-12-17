package com.crs.denzip.model.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSchedule {
  private String ownerId;
  private String listingId;
  private String tenantId;
  private String scheduleStatus;
  private String scheduleItineraryStatus;
  private UserPrefVisitSlot potentialItinerary;
  private UserPrefVisitSlot finalizedItinerary;
  private UserPrefVisitSlot tenantPrefVisitSlot;
  private UserPrefVisitSlot ownerPrefVisitSlot;
}
