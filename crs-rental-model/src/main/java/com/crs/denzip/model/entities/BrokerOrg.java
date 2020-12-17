package com.crs.denzip.model.entities;

import com.crs.denzip.model.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BrokerOrg {

  private String brokerOrgId;
  @NotNull
  @NotEmpty
  private String brokerOrgName;
  private String brokerOrgAddress;
  private String brokerOrgMobile;
  @ValidEmail
  private String brokerOrgEmail;

  private String tags;

  private User brokerOrgPrimaryUser;

  private String brokerPhoneNumbers;

}
