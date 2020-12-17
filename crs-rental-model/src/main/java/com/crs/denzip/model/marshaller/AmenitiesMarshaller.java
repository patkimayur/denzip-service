package com.crs.denzip.model.marshaller;

import java.util.HashMap;

public class AmenitiesMarshaller extends CRSMarshaller {

  public AmenitiesMarshaller() {
    super();
  }

  public HashMap<String, Boolean> unmarshall(String jsonString) {
    return super.unmarshall(jsonString, HashMap.class);
  }

}
