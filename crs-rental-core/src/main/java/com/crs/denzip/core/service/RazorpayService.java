package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.RazorpayOrder;
import com.crs.denzip.model.entities.User;
import com.crs.denzip.persistence.dao.RazorpayOrderDAO;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class RazorpayService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RazorpayService.class);

  private final String razorpayKeyId;
  private final String razorpayKeySecret;
  private final RazorpayOrderDAO razorpayOrderDAO;
  private final UserService userService;
  private RazorpayClient razorpayClient;

  @PostConstruct
  public void init() {
    try {
      this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    } catch (RazorpayException e) {
      LOGGER.error(e.getMessage(), e);
    }

  }

  public RazorpayOrder getRazorpayOrder(String listingId, String userId, int amount) {
    // 1. Check if this exist in listing_user_razorpay_order and if yes, fetch razorpay_order_id
    String razorpayOrderId = this.razorpayOrderDAO.getRazorpayOrderId(listingId, userId);

    // 2. If not, generate the razorpay_order_id usin RazorPay Orders API and insert in listing_user_razorpay_order
    if (StringUtils.isEmpty(razorpayOrderId)) {
      LOGGER.debug("RazorpayOrderId not found for listingId: {}, userId: {}. Creating now..", listingId, userId);
      razorpayOrderId = createRazorpayOrder(listingId, userId, amount);
      LOGGER.debug("RazorpayOrderId created for listingId: {}, userId: {} as RazorpayOrderId: {}", listingId, userId, razorpayOrderId);
      if (StringUtils.isNotEmpty(razorpayOrderId)) {
        razorpayOrderId = razorpayOrderDAO.insertRazorpayOrderId(listingId, userId, razorpayOrderId);
        LOGGER.debug("RazorpayOrderId inserted in DB for listingId: {}, userId: {} as RazorpayOrderId: {}", listingId, userId, razorpayOrderId);
      }
    }

    if (StringUtils.isNotEmpty(razorpayOrderId)) {
      if (this.razorpayOrderDAO.isRazorpayOrderIdProcessed(razorpayOrderId)) {
        LOGGER.debug("RazorpayOrderId: {}, listingId: {}, userId: {} has been processed", razorpayOrderId, listingId, userId);
        return RazorpayOrder.builder().razorpayOrderId(razorpayOrderId).orderProcessed(true).build();
      }

      // In situation where payment was successful but our DB did not get updated
      try {
        Payment successfulPayment = this.razorpayClient.Orders.fetchPayments(razorpayOrderId).stream().filter(payment -> payment.get("captured")).findFirst().orElse(null);
        LOGGER.debug("OrderId: {}, Payment:{}", razorpayOrderId, successfulPayment);
        if(successfulPayment != null) {
          LOGGER.debug("RazorpayOrderId: {}, listingId: {}, userId: {} has been processed", razorpayOrderId, listingId, userId);
          updateRazorpayOrderStatus(razorpayOrderId, successfulPayment.get("id"));
          return RazorpayOrder.builder().razorpayOrderId(razorpayOrderId).orderProcessed(true).build();
        }
      } catch (RazorpayException e) {
        LOGGER.error(e.getMessage(), e);
      }

      LOGGER.debug("RazorpayOrderId: {}, listingId: {}, userId: {} is yet to be processed", razorpayOrderId, listingId, userId);
      return RazorpayOrder.builder().razorpayOrderId(razorpayOrderId).orderProcessed(false).build();
    }

    return null;

  }

  private String createRazorpayOrder(String listingId, String userId, int amount) {
    try {
      LOGGER.debug("Going to invoke Razorpay Order API for listingId: {}, userId: {} ", listingId, userId);
      JSONObject options = new JSONObject();
      options.put("amount", amount);
      options.put("currency", "INR");
      options.put("payment_capture", true);
      Order order = this.razorpayClient.Orders.create(options);
      LOGGER.debug("RazorpayOrder for listingId: {}, userId: {} as RazorpayOrder: {}", listingId, userId, order);
      if (order != null) {
        return order.get("id");
      }
    } catch (RazorpayException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return null;
  }

  public boolean updateRazorpayOrderStatus(String razorpayOrderId, String razorpayPaymentId) {
    try {
      Payment payment = this.razorpayClient.Payments.fetch(razorpayPaymentId);
      LOGGER.debug("OrderId: {}, PaymentId: {}, Payment:{}", razorpayOrderId, razorpayPaymentId, payment);
      if (payment.get("captured")) {
        this.razorpayOrderDAO.updateRazorpayOrderStatus(razorpayOrderId);
        return true;
      }
    } catch (RazorpayException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return false;
  }


  public boolean reportRentedOut(String listingId, String userId) {
    String razorpayOrderId = this.razorpayOrderDAO.getRazorpayOrderId(listingId, userId);
    LOGGER.debug("While trying to reportRentedOut, listingId: {}, userId: {}, retrieved razorpayOrderId: {}", listingId, userId, razorpayOrderId);

    if (StringUtils.isEmpty(razorpayOrderId)) {
      return false;
    }

    if (this.razorpayOrderDAO.isRazorpayOrderIdProcessed(razorpayOrderId)) {
      LOGGER.debug("While trying to reportRentedOut, listingId: {}, userId: {}, retrieved razorpayOrderId: {} was successfully processed", listingId, userId, razorpayOrderId);
      // We are purposefully not checking size as it is possible user is pressing thus button multiple times
      // In such cases we do not want to overwrite the first time user pressed it so that we can validate the 24 hours window
      // So the update only happens first time in table and not subsequent times
      User tenant = userService.findByUserId(userId);
      userService.emailDenzipAdmins("Reported Property Rented Out", String.format("Customer %s reported listing: %s as rented out. Contact in next 24 hours", tenant, listingId));
      userService.smsDenzipAdmins(String.format("Customer %s, %s reported listing: %s as rented out", tenant.getUserName(), tenant.getUserMobile(), listingId));
      this.razorpayOrderDAO.reportRentedOut(razorpayOrderId);

      return true;
    }

    LOGGER.debug("While trying to reportRentedOut, listingId: {}, userId: {}, retrieved razorpayOrderId: {} was not processed", listingId, userId, razorpayOrderId);
    return false;
  }

}
