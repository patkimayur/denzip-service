package com.crs.denzip.model.marshaller;

import com.crs.denzip.model.entities.CRSImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CRSImageMarshaller extends CRSMarshaller {

  public CRSImageMarshaller() {
    super();
  }

  public List<CRSImage> unmarshall(String jsonString) {
    return new ArrayList<>(Arrays.asList(super.unmarshall(jsonString, CRSImage[].class)));
  }

}
