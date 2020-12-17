package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {

  private String apartmentId;
  private String apartmentName;
  private double apartmentLatitude;
  private double apartmentLongitude;

  private List<CRSImage> crsImages;

  private Map<String, Boolean> apartmentAmenities;

}
