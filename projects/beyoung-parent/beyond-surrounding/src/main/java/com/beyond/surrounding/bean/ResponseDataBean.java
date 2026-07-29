package com.beyond.surrounding.bean;

import java.util.List;

public class ResponseDataBean {
	private String code;
	private String message;
	private String title;
	private String transactionId;
	private String transactionType;
	private String identity;
	private String storeCode;
	private Integer quantity;
	private List<AvailableInfo> availableInfo;

	public ResponseDataBean() {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<AvailableInfo> getAvailableInfo() {
		return availableInfo;
	}

	public void setAvailableInfo(List<AvailableInfo> availableInfo) {
		this.availableInfo = availableInfo;
	}

}
