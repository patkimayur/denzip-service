package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
  private Long id;
  private String name;

  @Override
  public String getAuthority() {
    return getName();
  }
}