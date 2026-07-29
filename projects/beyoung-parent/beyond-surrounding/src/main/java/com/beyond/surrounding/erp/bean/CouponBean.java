package com.beyond.surrounding.erp.bean;

public class CouponBean extends ResponseBean{
	private String status;
	private String saleno;
	private String coupon;
	private String lqe01;
	private String lqe20;//券有效起始日
	private String lqe21;//券有效截止日

	private String rxe04;//券起始編號
	private String rxe05;//券截止編號
	private String lrz02;//面額

	private String lqe01_b;
	private String lqe01_e;
	
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSaleno() {
		return saleno;
	}
	public void setSaleno(String saleno) {
		this.saleno = saleno;
	}
	public String getLqe01() {
		return lqe01;
	}
	public void setLqe01(String lqe01) {
		this.lqe01 = lqe01;
	}
	public String getLqe20() {
		return lqe20;
	}
	public void setLqe20(String lqe20) {
		this.lqe20 = lqe20;
	}
	public String getLqe21() {
		return lqe21;
	}
	public void setLqe21(String lqe21) {
		this.lqe21 = lqe21;
	}
	public String getRxe04() {
		return rxe04;
	}
	public void setRxe04(String rxe04) {
		this.rxe04 = rxe04;
	}
	public String getRxe05() {
		return rxe05;
	}
	public void setRxe05(String rxe05) {
		this.rxe05 = rxe05;
	}
	public String getLrz02() {
		return lrz02;
	}
	public void setLrz02(String lrz02) {
		this.lrz02 = lrz02;
	}
	public String getLqe01_b() {
		return lqe01_b;
	}
	public void setLqe01_b(String lqe01_b) {
		this.lqe01_b = lqe01_b;
	}
	public String getLqe01_e() {
		return lqe01_e;
	}
	public void setLqe01_e(String lqe01_e) {
		this.lqe01_e = lqe01_e;
	}
}
