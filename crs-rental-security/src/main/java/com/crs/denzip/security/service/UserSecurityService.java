package com.crs.denzip.security.service;

import com.crs.denzip.persistence.dao.UserDAO;
import com.crs.denzip.model.entities.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class UserSecurityService implements UserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityService.class);
  private final UserDAO userDAO;

  @Override
  public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
    User user = userDAO.findByPhoneNo(phoneNo);
    if (user == null) {
      throw new UsernameNotFoundException("No user found with phoneNo: " + phoneNo);
    }
    boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;
    return new org.springframework.security.core.userdetails.User(user.getUserMobile(), "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, user.getAuthorities());
  }

}
