package com.beyond.surrounding.bean;

public class TransactionAdditionalPointBean {
	private String point_code;
	private Integer point_quantity;
	private String point_expired_datetime;
	public String getPoint_code() {
		return point_code;
	}
	public void setPoint_code(String point_code) {
		this.point_code = point_code;
	}
	public Integer getPoint_quantity() {
		return point_quantity;
	}
	public void setPoint_quantity(Integer point_quantity) {
		this.point_quantity = point_quantity;
	}
	public String getPoint_expired_datetime() {
		return point_expired_datetime;
	}
	public void setPoint_expired_datetime(String point_expired_datetime) {
		this.point_expired_datetime = point_expired_datetime;
	}
}
