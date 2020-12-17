package com.crs.denzip.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfTokenResponseHeaderBindingFilter extends OncePerRequestFilter {

  protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
  protected static final String RESPONSE_TOKEN_NAME = "XSRF-TOKEN";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
       response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
       response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
      if(StringUtils.isNotBlank(request.getHeader("Origin"))) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
      }else if(StringUtils.isNotBlank(request.getHeader("Access-Control-Allow-Origin"))){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Access-Control-Allow-Origin"));
      }
         response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setStatus(HttpServletResponse.SC_OK);
    }else {
      response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
      response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
      if(StringUtils.isNotBlank(request.getHeader("Origin"))) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
      }else if(StringUtils.isNotBlank(request.getHeader("Access-Control-Allow-Origin"))){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Access-Control-Allow-Origin"));
      }
      response.setHeader("Access-Control-Allow-Credentials", "true");
      filterChain.doFilter(request, response);
    }
  }
}