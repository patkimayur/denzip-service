package com.crs.denzip.security.config;

import com.crs.denzip.security.service.UserSecurityService;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@ConditionalOnProperty(value = "app.security.basic.disabled", havingValue = "false")
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@Import({Encoders.class})
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${spring.security.allowedOrigins}")
  private String allowedOrigins;

  @Value("${swagger.allowedOrigins}")
  private String swaggerAllowedOrigins;

  @Autowired
  private UserSecurityService userSecurityService;


  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  @Override
  public UserDetailsService userDetailsServiceBean() {
    return userSecurityService;
  }

  @Bean
  public CustomAuthenticationProvider customAuthenticationProvider() {
    CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
    customAuthenticationProvider.setUserDetailsService(userSecurityService);
    return customAuthenticationProvider;
  }

  @Bean
  public FilterRegistrationBean corsFilter() {
    //based on https://github.com/spring-projects/spring-boot/issues/5834
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    // config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
    config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
    config.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowCredentials(true);
    config.setAllowedHeaders(Arrays.asList("x-requested-with", "authorization", "Content-Type", "Authorization", "credential", "X-XSRF-TOKEN"));
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers("/webjars/**");

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
       // .csrfTokenRepository(getCsrfTokenRepository())
       // .and()
        .authorizeRequests()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/csrf")
        .hasIpAddress(swaggerAllowedOrigins)
        .antMatchers("/validateOtp", "/validateOtpByPhone", "/generateOtp", "/generateOtpByPhone", "/findByPhone", "/findByFacebookId", "/findByGoogleId", "/getLocalityData", "/createOrUpdateUser", "/getBedroomCountFilter", "/getFilters", "/getListingDetail", "/getActiveListings", "/getAllListings", "/getDefaultListingAmenities", "/getAllApartments", "/images/**", "/getDefaultApartmentAmenities", "/addUserRequestCallback", "/getApartment", "/updateUserTags", "/getAllTags", "/getUserTags", "/getListingTypeFilter", "/getRecentlyAddedListings", "/getActiveListingCount", "/getMarathahalliListingCount", "/getBTMListingCount", "/getKoramangalaListingCount","/getPropertyAndUserCount")
        .permitAll()
        .antMatchers("/uploadListingImage", "/uploadApartmentImage", "/addAndGetListing", "/updateAndGetListing", "/addApartment", "/updateApartment", "/userExistsByPhone", "/getBrokerOrgByUserId")
        .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_BROKER')")
        .antMatchers("/registerBrokerOrgDetails", "/registerBrokerOrgMapping")
        .access("hasRole('ROLE_ADMIN')")
        .antMatchers("/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BROKER')")
        .and()
        .userDetailsService(userDetailsServiceBean());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(customAuthenticationProvider());
  }

  private CsrfTokenRepository getCsrfTokenRepository() {
    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    tokenRepository.setCookiePath("/");
    tokenRepository.setCookieHttpOnly(false);
    return tokenRepository;
  }

}