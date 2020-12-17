package com.crs.denzip.model.marshaller;

import com.crs.denzip.model.entities.Listing;

public class ListingMarshaller extends CRSMarshaller {

  public ListingMarshaller() {
    super();
  }

  public Listing unmarshall(String jsonString) {
    return super.unmarshall(jsonString, Listing.class);
  }

}
