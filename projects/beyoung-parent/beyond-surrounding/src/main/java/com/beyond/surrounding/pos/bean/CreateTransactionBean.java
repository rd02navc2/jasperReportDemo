package com.beyond.surrounding.pos.bean;

public class CreateTransactionBean {
	private String referenceId;
	private Integer amount;
	private String currency;
	private String expiringAt;

	public CreateTransactionBean() {
	}

	public CreateTransactionBean(String referenceId, Integer amount, String currency, String expiringAt) {
		this.referenceId = referenceId;
		this.amount = amount;
		this.currency = currency;
		this.expiringAt = expiringAt;
	}

	@Override
	public String toString() {
		return "CreateTransactionBean [referenceId=" + referenceId + ", amount=" + amount + ", currency=" + currency
				+ ", expiringAt=" + expiringAt + "]";
	}

	public String getExpiringAt() {
		return expiringAt;
	}

	public void setExpiringAt(String expiringAt) {
		this.expiringAt = expiringAt;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
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

}
