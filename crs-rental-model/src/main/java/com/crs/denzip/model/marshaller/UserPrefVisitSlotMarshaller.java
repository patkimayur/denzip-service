package com.crs.denzip.model.marshaller;

import com.crs.denzip.model.entities.UserPrefVisitSlot;

public class UserPrefVisitSlotMarshaller extends CRSMarshaller {

  public UserPrefVisitSlotMarshaller() {
    super();
  }

  public UserPrefVisitSlot unmarshall(String jsonString) {
    return super.unmarshall(jsonString, UserPrefVisitSlot.class);
  }

}
