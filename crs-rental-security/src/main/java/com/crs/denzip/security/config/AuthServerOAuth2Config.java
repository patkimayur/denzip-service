package com.crs.denzip.security.config;

import com.crs.denzip.security.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
@Import({ServerSecurityConfig.class})
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {


  @Autowired
  @Qualifier("dataSource")
  private DataSource oauthDataSource;

  @Autowired
  private UserSecurityService userSecurityService;

  @Autowired
  private PasswordEncoder oauthClientPasswordEncoder;

  @Autowired
  private AuthenticationManager authenticationManagerBean;

  @Bean
  public JdbcClientDetailsService clientDetailsService() {
    return new JdbcClientDetailsService(oauthDataSource);
  }


  @Bean
  public TokenStore tokenStore() {
    return new JdbcTokenStore(oauthDataSource);
  }

  @Bean
  public ApprovalStore approvalStore() {
    return new JdbcApprovalStore(oauthDataSource);
  }

  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new JdbcAuthorizationCodeServices(oauthDataSource);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailsService());
  }


  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    CsrfTokenResponseHeaderBindingFilter csrfFilter = csrfTokenResponseHeaderBindingFilter();

    oauthServer
        .allowFormAuthenticationForClients()
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .passwordEncoder(oauthClientPasswordEncoder)
        .authenticationEntryPoint(authenticationEntryPoint())
        .addTokenEndpointAuthenticationFilter(csrfFilter);
  }

  @Bean
  public CsrfTokenResponseHeaderBindingFilter csrfTokenResponseHeaderBindingFilter() {
    return new CsrfTokenResponseHeaderBindingFilter();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    MyBasicAuthenticationEntryPoint entryPoint = new MyBasicAuthenticationEntryPoint();
    entryPoint.setRealmName("crs/client");
    return entryPoint;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.approvalStore(approvalStore())
        .authorizationCodeServices(authorizationCodeServices())
        .tokenStore(tokenStore())
        .userDetailsService(userSecurityService)
        .authenticationManager(authenticationManagerBean);
  }

}