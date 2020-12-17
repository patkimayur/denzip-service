package com.crs.denzip.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crs.denzip.security.otpgen.TimeBasedOneTimePasswordGenerator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestOtp {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestOtp.class);

  public static final String TOTP_ALGORITHM_HMAC_SHA512 = "HmacSHA512";

  public static void main(String[] args) {
    KeyGenerator keyGenerator = null;
    try {
      keyGenerator = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error(e.getMessage(), e);
    }
    SecureRandom secureRandom = new SecureRandom();
    int keyBitSize = 256;
    keyGenerator.init(keyBitSize, secureRandom);
    SecretKey secretKey = keyGenerator.generateKey();
    byte[] encodedSecret = secretKey.getEncoded();
    SecretKey originalKey = new SecretKeySpec(encodedSecret, TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512);

    for (int i = 0; i < 10; i++) {
      try {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(5, TimeUnit.SECONDS);

        Date now = new Date();

        //check if this will be used later
        //      Totp otpGen = new Totp(user.getSecret());
        //      String otp = otpGen.now();

        //System.out.println("The time is" + now);
        System.out.println("the otp is" + totp.generateOneTimePassword(originalKey, now));
      } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
  }
}
