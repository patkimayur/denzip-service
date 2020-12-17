package com.crs.denzip.model.entities;

import com.crs.denzip.model.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class User {
  private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

  private String userId;
  @NotNull
  @NotEmpty
  private String userName;
  private String userMobile;
  @ValidEmail
  private String userEmail;

  private String userAcceptanceDoc;
  private List<String> roles;
  private String userGovtIdNo;
  private String userGovtIdImage;
  private List<String> userPrefCommMode;
  private String userPrefCommFreq;
  private Collection<Authority> authorities;
  private UserPrefVisitSlot userPrefVisitSlot;
  private List<String> userSeenListingIds;
  private List<String> userShortlistedListingIds;
  private List<String> userScheduleRequestListingIds;
  private String otp;
  private byte[] secret;
  private String userFacebookId;
  private String userGoogleId;
  public static final String TOTP_ALGORITHM_HMAC_SHA512 = "HmacSHA512";


  public User(@JsonProperty("userId") String userId, @JsonProperty("userEmail") String userEmail, @JsonProperty("userName") String userName, @JsonProperty("secret") byte[] secret, @JsonProperty("otp") String otp) {
    this.userId = userId;
    this.userEmail = userEmail;
    this.userName = userName;
    this.otp = otp;

    if (null != secret) {
      this.secret = secret;
    }
    else {
      KeyGenerator keyGenerator = null;
      try {
        keyGenerator = KeyGenerator.getInstance(TOTP_ALGORITHM_HMAC_SHA512);
      } catch (NoSuchAlgorithmException e) {
        LOGGER.error("Error while generating secret key", e);
      }
      if (null != keyGenerator) {
        keyGenerator.init(1024);
        SecretKey secretKey = keyGenerator.generateKey();
        this.secret = secretKey.getEncoded();
      }
    }

  }

  @Override
  public String toString(){
    return String.format("Username: %s, userEmail: %s, userPhone: %s and userId: %s", this.userName, this.userEmail, this.userMobile, this.userId);
  }


}
