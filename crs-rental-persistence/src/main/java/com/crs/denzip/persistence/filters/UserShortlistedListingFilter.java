package com.crs.denzip.persistence.filters;

public class UserShortlistedListingFilter extends AbstractUserListingRelationFilter {

  public UserShortlistedListingFilter(String userId) {
    super("UserShortlistedListingFilter", "listing_user_shortlist_mapping", userId);
  }
}
