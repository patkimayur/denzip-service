package com.crs.denzip.model.marshaller;

import com.crs.denzip.model.entities.LocalityData;

public class LocalityDataMarshaller extends CRSMarshaller {

  public LocalityDataMarshaller() {
    super();
  }

  public LocalityData unmarshall(String jsonString) {
    return super.unmarshall(jsonString, LocalityData.class);
  }

}
