package com.crs.denzip.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@EnableResourceServer
@Configuration
@ConditionalOnProperty(value = "app.security.basic.disabled", havingValue = "false")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  private static final String RESOURCE_ID = "resource-server-rest-api";
  private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
  private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";
  private static final String SECURED_PATTERN = "/**";
  protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
  protected static final String RESPONSE_TOKEN_NAME = "XSRF-TOKEN";

  @Autowired
  @Qualifier("dataSource")
  private DataSource oauthDataSource;

  @Value("${swagger.allowedOrigins}")
  private String swaggerAllowedOrigins;


  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {

    TokenStore tokenStore = new JdbcTokenStore(oauthDataSource);
    resources.authenticationEntryPoint(new MyBasicAuthenticationEntryPoint());
    resources.resourceId(RESOURCE_ID)
             .tokenStore(tokenStore);

  }

  @Override
  public void configure(HttpSecurity http) throws Exception {

    http.csrf()
        // .csrfTokenRepository(getCsrfTokenRepository())
        .disable()
        .authorizeRequests()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/csrf")
        .hasIpAddress(swaggerAllowedOrigins)
        .antMatchers("/validateOtp", "/validateOtpByPhone", "/generateOtp", "/generateOtpByPhone", "/findByPhone", "/findByFacebookId", "/findByGoogleId", "/getLocalityData", "/createOrUpdateUser", "/getBedroomCountFilter", "/getFilters", "/getListingDetail", "/getActiveListings", "/getAllListings", "/getDefaultListingAmenities", "/getAllApartments", "/images/**", "/getDefaultApartmentAmenities", "/addUserRequestCallback", "/getApartment", "/updateUserTags", "/getAllTags", "/getUserTags", "/getListingTypeFilter", "/getRecentlyAddedListings", "/getActiveListingCount", "/getMarathahalliListingCount", "/getBTMListingCount", "/getKoramangalaListingCount", "/getPropertyAndUserCount")
        .permitAll()
        .antMatchers("/uploadListingImage", "/uploadApartmentImage", "/addAndGetListing", "/updateAndGetListing", "/addApartment", "/updateApartment", "/userExistsByPhone", "/getBrokerOrgByUserId")
        .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_BROKER')")
        .antMatchers("/registerBrokerOrgDetails", "/registerBrokerOrgMapping")
        .access("hasRole('ROLE_ADMIN')")
        .antMatchers("/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BROKER')")
        .antMatchers(HttpMethod.GET, "/**")
        .access(SECURED_READ_SCOPE)
        .antMatchers(HttpMethod.POST, "/**")
        .access(SECURED_WRITE_SCOPE)
        .antMatchers(HttpMethod.PATCH, "/**")
        .access(SECURED_WRITE_SCOPE)
        .antMatchers(HttpMethod.PUT, "/**")
        .access(SECURED_WRITE_SCOPE)
        .antMatchers(HttpMethod.DELETE, "/**")
        .access(SECURED_WRITE_SCOPE)
        .and()

        .headers()
        .addHeaderWriter((request, response) -> {
          if (request.getMethod()
                     .equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            if(StringUtils.isNotBlank(request.getHeader("Origin"))) {
              response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            }else if(StringUtils.isNotBlank(request.getHeader("Access-Control-Allow-Origin"))){
              response.setHeader("Access-Control-Allow-Origin", request.getHeader("Access-Control-Allow-Origin"));
            }
            response.setHeader("Access-Control-Allow-Credentials", "true");

            CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_NAME);

            if (token != null) {
              response.setHeader(RESPONSE_TOKEN_NAME, token.getToken());
              response.setHeader("X-XSRF-TOKEN", token.getToken());
              response.setHeader(REQUEST_ATTRIBUTE_NAME, token.getToken());
            }

            response.setStatus(HttpServletResponse.SC_OK);

          }
        });
  }

  private CsrfTokenRepository getCsrfTokenRepository() {
    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    tokenRepository.setCookiePath("/");
    tokenRepository.setCookieHttpOnly(false);
    return tokenRepository;
  }

}