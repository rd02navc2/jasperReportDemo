package com.beyond.surrounding.pos.bean;

public class RefundBean {
	private String transactionId;
	private Integer amount;
	private String currency;
	private String justification;// 理由

	public RefundBean() {
	}

	public RefundBean(String transactionId, Integer amount, String currency, String justification) {
		this.transactionId = transactionId;
		this.amount = amount;
		this.currency = currency;
		this.justification = justification;
	}

	@Override
	public String toString() {
		return "RefundBean [transactionId=" + transactionId + ", amount=" + amount + ", currency=" + currency
				+ ", justification=" + justification + "]";
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

}
