package com.crs.denzip.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

public class MyBasicAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

  protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
  protected static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
  protected static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
  protected static final String RESPONSE_TOKEN_NAME = "XSRF-TOKEN";

  @Value("${spring.security.allowedOrigins}")
  private String allowedOrigins;


  @Override
  protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> response, Exception exception) {
    ResponseEntity responseEntity = super.enhanceResponse(response, exception);

    HttpHeaders update = new HttpHeaders();
    update.add("Access-Control-Allow-Methods", "HEAD, GET, POST, PUT, DELETE, OPTIONS");
    update.add("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
    update.add("Access-Control-Allow-Origin", allowedOrigins);
    update.add("Access-Control-Allow-Credentials", "true");
    update.putAll(responseEntity.getHeaders());

    return new ResponseEntity<Object>(responseEntity.getBody(), update, responseEntity.getStatusCode());
  }

}