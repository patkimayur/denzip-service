package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
public class DeactivateListingDetails {
  private String ownerId;
  private String ownerMobile;
  private String ownerEmail;
  private String tenantUserId;
  private String listingId;
  private String deactivationStatus;
  private DeactivationType deactivationType;
}
