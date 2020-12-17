package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.BrokerOrg;
import com.crs.denzip.model.entities.User;
import com.crs.denzip.persistence.dao.BrokerOrgDAO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BrokerOrgService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BrokerOrgService.class);
  private final UserService userService;
  private final BrokerOrgDAO brokerOrgDAO;

  public BrokerOrg getBrokerOrgByUserId(String userId) {
    LOGGER.debug("Retrieving brokerOrg for brokerId: {}", userId);
    BrokerOrg brokerOrg = brokerOrgDAO.getBrokerOrgByUserId(userId);
    LOGGER.debug("Retrieved from DB brokerId: {}, brokerOrg: {}", userId, brokerOrg);

    // Get primary User for BrokerOrg
    User brokerOrgPrimaryUser = this.userService.findByPhone(brokerOrg.getBrokerOrgMobile());
    brokerOrg.setBrokerOrgPrimaryUser(brokerOrgPrimaryUser);
    LOGGER.debug("After getting brokerOrgPrimaryUser brokerId: {}, brokerOrg: {}", userId, brokerOrg);

    return brokerOrg;
  }

  public boolean registerBrokerOrg(BrokerOrg brokerOrg) {
    LOGGER.debug("Registering broker org");
    return brokerOrgDAO.addBrokerOrg(brokerOrg);
  }

  public boolean registerBrokerOrgMapping(BrokerOrg brokerOrg) {
    LOGGER.debug("Registering broker org mapping");

    if (StringUtils.isNotBlank(brokerOrg.getBrokerPhoneNumbers())) {
      List<String> brokerPhoneNumbers = Arrays.asList(brokerOrg.getBrokerPhoneNumbers().split(","));
      if (!CollectionUtils.isEmpty(brokerPhoneNumbers)) {
        brokerPhoneNumbers = brokerPhoneNumbers.stream().filter(ph -> StringUtils.isNotBlank(ph)).map(ph -> ph.trim()).collect(Collectors.toList());
      }

      boolean isMapped = brokerOrgDAO.addBrokerOrgMapping(brokerOrg.getBrokerOrgMobile(), brokerPhoneNumbers);

      if (isMapped) {
        LOGGER.debug("Broker org mapping completed successfully, now trying to update broker role");
        isMapped = userService.updateAuthorityToBroker(brokerPhoneNumbers);

        if(isMapped){
          LOGGER.debug("updating broker role completed successfully");
        }
        return isMapped;
      }
      LOGGER.debug("unable to map broker to org");
    }

    return false;
  }


}
