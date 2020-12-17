package com.crs.denzip.persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class RazorpayOrderDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(RazorpayOrderDAO.class);

  private static final String GET_RAZORPAY_ORDER_ID = "select razorpay_order_id from listing_user_razorpay_order where listing_id = :listing_id and user_id = :user_id";

  private static final String INSERT_RAZORPAY_ORDER_ID =
      "insert into listing_user_razorpay_order (listing_id, user_id, razorpay_order_id)"
      + " values (:listing_id, :user_id, :razorpay_order_id)"
      + " ON CONFLICT (listing_id, user_id) DO NOTHING RETURNING razorpay_order_id";

  private static final String GET_RAZORPAY_ORDER_ID_STATUS = "select razorpay_order_id from razorpay_successful_order where razorpay_order_id = :razorpay_order_id";

  private static final String INSERT_RAZORPAY_ORDER_ID_STATUS = "insert into razorpay_successful_order (razorpay_order_id, created_time) values (:razorpay_order_id, now()) ON CONFLICT (razorpay_order_id) DO NOTHING";

  private static final String REPORT_RENTED_OUT = "update razorpay_successful_order set (refund_requested, refund_requested_time) = (true, now()) where razorpay_order_id = :razorpay_order_id and refund_requested = false";

  public String getRazorpayOrderId(String listingId, String userId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("listing_id", new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userId));

    List<String> razorpayOrderId = super.query(GET_RAZORPAY_ORDER_ID, paramSourceMap, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("razorpay_order_id");
      }
    });

    LOGGER.debug("ListingId: {} and UserId: {} - RazorpayOrderId: {}", listingId, userId, razorpayOrderId);

    if (CollectionUtils.isEmpty(razorpayOrderId) || razorpayOrderId.size() > 1) {
      return null;
    }

    return razorpayOrderId.get(0);
  }

  public boolean isRazorpayOrderIdProcessed(String razorpayOrderId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("razorpay_order_id", new SqlParameterValue(Types.VARCHAR, razorpayOrderId));

    List<String> razorpayOrderIdList = super.query(GET_RAZORPAY_ORDER_ID_STATUS, paramSourceMap, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("razorpay_order_id");
      }
    });

    LOGGER.debug("RazorpayOrderId: {} processed status: {}", razorpayOrderId, razorpayOrderIdList);

    if (CollectionUtils.isEmpty(razorpayOrderIdList) || razorpayOrderIdList.size() > 1) {
      return false;
    }

    return true;
  }


  public String insertRazorpayOrderId(String listingId, String userId, String razorpayOrderId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("listing_id", new SqlParameterValue(Types.OTHER, listingId));
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userId));
    paramSourceMap.addValue("razorpay_order_id", new SqlParameterValue(Types.VARCHAR, razorpayOrderId));

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result =  super.update(INSERT_RAZORPAY_ORDER_ID, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1 && null != keyHolder.getKeys()) {
      return String.valueOf(keyHolder.getKeys()
          .get("razorpay_order_id"));
    }

    return null;
  }

  public int updateRazorpayOrderStatus(String razorpayOrderId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("razorpay_order_id", new SqlParameterValue(Types.VARCHAR, razorpayOrderId));
    return super.update(INSERT_RAZORPAY_ORDER_ID_STATUS, paramSourceMap);
  }

  public int reportRentedOut(String razorpayOrderId) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("razorpay_order_id", new SqlParameterValue(Types.VARCHAR, razorpayOrderId));
    return super.update(REPORT_RENTED_OUT, paramSourceMap);
  }
}
