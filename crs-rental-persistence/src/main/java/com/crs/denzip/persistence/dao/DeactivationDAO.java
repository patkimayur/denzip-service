package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.DeactivateListingDetails;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@AllArgsConstructor
public class DeactivationDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeactivationDAO.class);


  private static final String SYSTEM_DEACTIVATE_LISTINGS = "with updated as ("
      + "update listing set listing_active_ind = false where listing_active_ind =true "
      + "and last_activation_time < now() - interval '%s' returning listing_id, user_id) "
      + "update listing_user_schedule_mapping set schedule_status = 'SYSTEM_DEACTIVATED' " + "from updated, crs_user "
      + "where listing_user_schedule_mapping.listing_id = updated.listing_id "
      + "and listing_user_schedule_mapping.user_id = updated.user_id "
      + "and listing_user_schedule_mapping.user_id = crs_user.user_id "
      + "returning listing_user_schedule_mapping.listing_id, listing_user_schedule_mapping.user_id, crs_user.user_mobile, crs_user.user_email";

  private static final String SYSTEM_MARK_FOR_DEACTIVATION = "with selected as ("
      + "select listing_id, user_id from listing where listing_active_ind =true "
      + "and last_activation_time < now() - interval '%s') "
      + "update listing_user_schedule_mapping set schedule_status = 'SYSTEM_PENDING_DEACTIVATION' "
      + "from selected, crs_user "
      + "where listing_user_schedule_mapping.listing_id = selected.listing_id "
      + "and listing_user_schedule_mapping.user_id = selected.user_id "
      + "and listing_user_schedule_mapping.user_id = crs_user.user_id "
      + "returning listing_user_schedule_mapping.listing_id, listing_user_schedule_mapping.user_id, crs_user.user_mobile, crs_user.user_email";

  private final String deactivationInterval;
  private final String deactivationWarningInterval;



  public List<DeactivateListingDetails> systemDeactivateOldListings() {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    String query = String.format(SYSTEM_DEACTIVATE_LISTINGS, deactivationInterval);

    List<DeactivateListingDetails> deactivatedListings = super.query(query, paramSourceMap, new RowMapper<DeactivateListingDetails>() {
      @Override
      public DeactivateListingDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DeactivateListingDetails.builder()
            .listingId(rs.getString("listing_id"))
            .ownerId(rs.getString("user_id"))
            .ownerMobile(rs.getString("user_mobile"))
            .ownerEmail(rs.getString("user_email"))
            .build();
      }
    });

    LOGGER.debug("System DeActivated Listings: {}", deactivatedListings);
    return deactivatedListings;
  }

  public List<DeactivateListingDetails> systemMarkForDeactivation() {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();

    String query = String.format(SYSTEM_MARK_FOR_DEACTIVATION, deactivationWarningInterval);
    List<DeactivateListingDetails> deactivatedListings = super.query(query, paramSourceMap, new RowMapper<DeactivateListingDetails>() {
      @Override
      public DeactivateListingDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DeactivateListingDetails.builder()
            .listingId(rs.getString("listing_id"))
            .ownerId(rs.getString("user_id"))
            .ownerMobile(rs.getString("user_mobile"))
            .ownerEmail(rs.getString("user_email"))
            .build();
      }
    });

    LOGGER.debug("System Marked For Deactivation Listings: {}", deactivatedListings);
    return deactivatedListings;
  }


}



