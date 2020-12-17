package com.crs.denzip.model.entities;


import com.crs.denzip.model.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrokerUserNotification {

  private String brokerOrgId;
  @NotNull
  @NotEmpty
  private String brokerOrgName;
  private String brokerOrgMobile;
  @ValidEmail
  private String brokerOrgEmail;

  private List<String> tagNames;

  private List<String> userIds;
}
