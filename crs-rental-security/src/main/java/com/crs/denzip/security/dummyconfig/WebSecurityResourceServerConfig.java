package com.crs.denzip.security.dummyconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

//TODO - remove this class as this is only for testing without spring security, may be we want to use this for test
@EnableResourceServer
@Configuration
@ConditionalOnProperty(value = "app.security.basic.disabled", havingValue = "true")
public class WebSecurityResourceServerConfig extends ResourceServerConfigurerAdapter {
  private static final String RESOURCE_ID = "resource-server-rest-api";
  private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
  private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";
  private static final String SECURED_PATTERN = "/**";

  @Autowired
  @Qualifier("dataSource")
  private DataSource oauthDataSource;


  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {

    TokenStore tokenStore = new JdbcTokenStore(oauthDataSource);
    resources.resourceId(RESOURCE_ID)
             .tokenStore(tokenStore);

  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/**")
        .permitAll();
  }

}