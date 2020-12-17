package com.crs.denzip.model.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountEntity {

  private int listingCount;
  private int userCount;
}
