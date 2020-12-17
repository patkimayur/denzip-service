package com.crs.denzip.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


//TODO - This is for future use as we need to add all message  convertors here, because image loading stopped working when this was enable as byteArrayHTTPMessageConvertor is not added in this
@Configuration
@EnableWebMvc
public class WebappConfig implements WebMvcConfigurer {

  // this is need for security purposes as returned json should not be executable
  // angular removes the prefix and this has been added as mentioned in JSON Vulnerability Protection
  //Cross-site script inclusion (XSSI) - https://angular.io/guide/security
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setJsonPrefix(")]}',\n");

    ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
    byteArrayHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.IMAGE_JPEG));

    converters.add(converter);
    converters.add(byteArrayHttpMessageConverter);
  }
}