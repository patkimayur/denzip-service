package com.crs.denzip.model.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CRSMarshaller {

  private static final Logger LOGGER = LoggerFactory.getLogger(CRSMarshaller.class);
  private final ObjectMapper mapper;

  public CRSMarshaller() {
    this.mapper = new ObjectMapper();
    this.mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
  }

  public <T> T unmarshall(String jsonString, Class<T> valueType) {
    try {
      return this.mapper.readValue(jsonString, valueType);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return null;
  }

  public String marshall(Object value) {
    try {
      return this.mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return null;
  }

}
