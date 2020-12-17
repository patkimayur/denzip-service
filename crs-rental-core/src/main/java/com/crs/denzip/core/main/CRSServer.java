package com.crs.denzip.core.main;

import com.crs.denzip.core.config.CRSConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CRSServer {

  public static void main(String[] args) {
    SpringApplication.run(CRSConfig.class, args);
  }

}
