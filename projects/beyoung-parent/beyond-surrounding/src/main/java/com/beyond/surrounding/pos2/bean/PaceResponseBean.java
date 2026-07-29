package com.beyond.surrounding.pos2.bean;

public class PaceResponseBean {
	private String transactionId;   // transactionID ➔ transactionId
	private String referenceId;     // referenceID ➔ referenceId
	private String merchantId;      // merchantID ➔ merchantId
	private MoneyBean amount;
	private String creationDate;
	private String expiryDate;
	private String updateDate;
	private String status;
	private String token;
	private String paymentLink;
	private String success;

	// webhookUrl回傳狀態
	private String event;

	// 退款用
	private String refundId;        // refundID ➔ refundId
	private String justification;
	private String rejectionReason;
	private String type;
	private String refundType;

	protected String code;          // Code ➔ code
	protected String message;       // Message ➔ message

	// 無參數建構子 (Spring & JSON 序列化必備)
	public PaceResponseBean() {
	}

	// 全參數建構子
	public PaceResponseBean(String transactionId, String referenceId, String merchantId, MoneyBean amount,
			String creationDate, String expiryDate, String updateDate, String status, String token, String paymentLink,
			String success, String event, String refundId, String justification, String rejectionReason, String type,
			String refundType, String code, String message) {
		this.transactionId = transactionId;
		this.referenceId = referenceId;
		this.merchantId = merchantId;
		this.amount = amount;
		this.creationDate = creationDate;
		this.expiryDate = expiryDate;
		this.updateDate = updateDate;
		this.status = status;
		this.token = token;
		this.paymentLink = paymentLink;
		this.success = success;
		this.event = event;
		this.refundId = refundId;
		this.justification = justification;
		this.rejectionReason = rejectionReason;
		this.type = type;
		this.refundType = refundType;
		this.code = code;
		this.message = message;
	}

	// ==========================================
	// 完整 Getter & Setter 區塊 (皆符合標準駝峰)
	// ==========================================

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public MoneyBean getAmount() {
		return amount;
	}

	public void setAmount(MoneyBean amount) {
		this.amount = amount;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
