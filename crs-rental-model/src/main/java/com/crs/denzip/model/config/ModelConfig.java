package com.crs.denzip.model.config;

import com.crs.denzip.model.validation.EmailValidator;
import com.crs.denzip.model.marshaller.AmenitiesMarshaller;
import com.crs.denzip.model.marshaller.CRSImageMarshaller;
import com.crs.denzip.model.marshaller.CRSMarshaller;
import com.crs.denzip.model.marshaller.ListingMarshaller;
import com.crs.denzip.model.marshaller.LocalityDataMarshaller;
import com.crs.denzip.model.marshaller.UserPrefVisitSlotMarshaller;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class ModelConfig {

  @Bean
  public LocalityDataMarshaller localityDataMarshaller() {
    return new LocalityDataMarshaller();
  }

  @Bean
  public ListingMarshaller listingMarshaller() {
    return new ListingMarshaller();
  }

  @Bean
  public UserPrefVisitSlotMarshaller userPrefVisitSlotMarshaller() {
    return new UserPrefVisitSlotMarshaller();
  }

  @Bean
  public AmenitiesMarshaller amenitiesMarshaller() {
    return new AmenitiesMarshaller();
  }

  @Bean
  public CRSImageMarshaller imageMarshaller() {
    return new CRSImageMarshaller();
  }

  @Bean
  public CRSMarshaller crsMarshaller() {
    return new CRSMarshaller();
  }

  @Bean
  public EmailValidator emailValidator() {
    return new EmailValidator();
  }

}
