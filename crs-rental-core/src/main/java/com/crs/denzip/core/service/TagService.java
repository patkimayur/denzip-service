package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.Listing;
import com.crs.denzip.model.entities.Tag;
import com.crs.denzip.persistence.dao.TagDAO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class TagService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TagService.class);
  private static final String TAG_FORMAT = "#%1$s, %2$s for %3$s";

  private final GoogleService googleService;
  private final TagDAO tagDAO;

  public String addListingTag(Listing listing) {
    String subLocality = this.googleService.getSubLocality(listing.getListingLatitude(), listing.getListingLongitude());
    String tagName = getTagName(listing.getListingBedroomCount(), subLocality, listing.getListingType());
    LOGGER.debug("ListingId {} derived tagName is {}", listing.getListingId(), tagName);
    return tagDAO.updateListingTag(listing.getListingId(), tagName);

  }

  private String getTagName(String bedroomCount, String subLocality, String listingType) {
    return String.format(TAG_FORMAT, bedroomCount, subLocality, listingType);
  }

  public boolean updateUserTags(String userId, List<String> tagIdList) {
    return tagDAO.updateUserTags(userId, tagIdList);
  }

  public List<Tag> getAllTags() {
    return tagDAO.getAllTags();
  }

  public List<Tag> getUserTags(String userId) {
    return tagDAO.getUserTags(userId);
  }

}
