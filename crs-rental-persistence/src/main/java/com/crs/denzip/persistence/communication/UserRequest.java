package com.crs.denzip.persistence.communication;

import com.crs.denzip.model.entities.UserPrefVisitSlot;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequest {

  private final String userId;
  private final UserPrefVisitSlot userPrefVisitSlot;

  public UserRequest(@JsonProperty("userId") String userId, @JsonProperty("userPrefVisitSlot") UserPrefVisitSlot userPrefVisitSlot) {
    this.userId = userId;
    this.userPrefVisitSlot = userPrefVisitSlot;
  }


}
