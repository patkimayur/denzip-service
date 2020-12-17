package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocalityResource {
  private String name;
  private double latitude;
  private double longitude;
  private String distance;
  private String duration;
}