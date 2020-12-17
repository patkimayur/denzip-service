package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.DeactivateListingDetails;
import com.crs.denzip.persistence.dao.DeactivationDAO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeactivationService {

  private final DeactivationDAO deactivationDAO;
  private final UserService userService;
  private final String deactivationInterval;
  private final String deactivationSms;
  private final String deactivationSubject;
  private final String deactivationMessage;
  private final String deactivationWarningInterval;
  private final String deactivationWarningSms;
  private final String deactivationWarningSubject;
  private final String deactivationWarningMessage;

  public void runDeactivationJob() {
    runDeactivateListingsAndCommunicate();
    runMarkForDeactivationAndCommunicate();
  }

  private void runDeactivateListingsAndCommunicate(){
    List<DeactivateListingDetails> deactivateListingDetails = systemDeactivateOldListings();
    for (DeactivateListingDetails deactivateListingDetail : deactivateListingDetails) {
      String message = String.format(deactivationMessage, deactivateListingDetail.getListingId(), deactivationInterval);
      userService.sendSms(deactivateListingDetail.getOwnerMobile(), deactivationSms);
      userService.sendEmail(deactivateListingDetail.getOwnerEmail(), deactivationSubject, message);
    }
  }

  private void runMarkForDeactivationAndCommunicate(){
    List<DeactivateListingDetails> deactivateListingDetails = systemMarkForDeactivation();
    for (DeactivateListingDetails deactivateListingDetail : deactivateListingDetails) {
      String message = String.format(deactivationWarningMessage, deactivateListingDetail.getListingId(), deactivationWarningInterval);
      userService.sendSms(deactivateListingDetail.getOwnerMobile(), deactivationWarningSms);
      userService.sendEmail(deactivateListingDetail.getOwnerEmail(), deactivationWarningSubject, message);
    }
  }

  private List<DeactivateListingDetails> systemDeactivateOldListings() {
    return deactivationDAO.systemDeactivateOldListings();
  }

  private List<DeactivateListingDetails> systemMarkForDeactivation() {
    return deactivationDAO.systemMarkForDeactivation();
  }
}
