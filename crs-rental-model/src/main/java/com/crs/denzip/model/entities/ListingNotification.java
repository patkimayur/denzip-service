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
public class ListingNotification {

  private String userId;
  @NotNull
  @NotEmpty
  private String userName;
  private String userMobile;
  @ValidEmail
  private String userEmail;

  private List<String> listingIds;
}
