package com.crs.denzip.security.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import com.crs.denzip.security.service.UserSecurityService;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    if (this.getUserDetailsService() instanceof UserSecurityService) {
      if (authentication.getCredentials() == null && authentication.getPrincipal() == null) {
        logger.debug("Authentication failed: no credentials provided");

        throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
      }
    }
    else {
      super.additionalAuthenticationChecks(userDetails, authentication);
    }
  }
}
