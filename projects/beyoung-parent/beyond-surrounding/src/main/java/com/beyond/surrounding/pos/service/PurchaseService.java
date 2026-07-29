package com.beyond.surrounding.pos.service;

import org.springframework.stereotype.Service;

@Service
public interface PurchaseService {

    // --- LinePay ---
    String linePayment(String orderId, String center, String counterID, String productName, Integer amt, String oneTimeKey) throws Exception;
    String linePaymentDetail(String transactionId) throws Exception;
    String linePayRefund(String orderId) throws Exception;

    // --- PXPay ---
    String pxpayPayment(String orderId, String center, String counterID, String posID, String posDateTime, String productName, Integer amt, String oneTimeKey) throws Exception;
    String pxpayQuery(String orderId) throws Exception;
    String pxpayRefund(String transactionId, String orderId, String center, String posID, String productName, Integer amt, String newOrderId, String posDateTime) throws Exception;

    // --- TaiwanPay ---
    String taiwanpayPayment(String orderId, String posID, Integer amt, String oneTimeKey) throws Exception;
    String taiwanpayQuery(String orderId, String posID) throws Exception;
    String taiwanpayRefund(Integer amt, String orderId) throws Exception;

    // --- OnePay ---
    String onepayPayment(String orderId, String posID, String posDateTime, Integer amt, String oneTimeKey) throws Exception;
    String onepayQuery(String orderId, String posID, String posDateTime, String oneTimeKey, String tradeType) throws Exception;
    String onepayRefund(String newOrderId, String posID, String posDateTime, Integer amt, String oneTimeKey, String transNo) throws Exception;
	
}