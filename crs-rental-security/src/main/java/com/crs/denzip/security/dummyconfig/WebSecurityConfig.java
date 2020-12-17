package com.crs.denzip.security.dummyconfig;

import com.crs.denzip.security.config.CustomAuthenticationProvider;
import com.crs.denzip.security.config.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.crs.denzip.security.service.UserSecurityService;


//TODO - remove this class before production as this is only for testing without spring security, may be we want to use this for test
@Configuration
@ConditionalOnProperty(value = "app.security.basic.disabled", havingValue = "true")
@Import({Encoders.class})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserSecurityService userSecurityService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/**")
        .permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(customAuthenticationProvider());
  }

  @Bean
  public CustomAuthenticationProvider customAuthenticationProvider() {
    CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
    customAuthenticationProvider.setUserDetailsService(userSecurityService);
    return customAuthenticationProvider;
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}