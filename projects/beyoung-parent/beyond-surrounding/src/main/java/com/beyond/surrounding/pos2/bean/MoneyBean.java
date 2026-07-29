package com.beyond.surrounding.pos2.bean;

public class MoneyBean {
	private Integer value;
	private Float actualValue;
	private String currency;

	// 無參數建構子 (JSON 序列化與框架必備)
	public MoneyBean() {
	}

	// 全參數建構子
	public MoneyBean(Integer value, Float actualValue, String currency) {
		this.value = value;
		this.actualValue = actualValue;
		this.currency = currency;
	}

	// ==========================================
	// 完整 Getter & Setter 區塊
	// ==========================================

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Float getActualValue() {
		return actualValue;
	}

	public void setActualValue(Float actualValue) {
		this.actualValue = actualValue;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}