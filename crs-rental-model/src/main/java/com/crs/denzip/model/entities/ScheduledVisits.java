package com.crs.denzip.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduledVisits {

  private User ownerUser;
  private User tenantUser;

  private Listing listing;

  private String scheduledSlot;
  private boolean visited;
  private String ownerFeedback;
  private String tenantFeedback;
  private String probableOutcome;

}
