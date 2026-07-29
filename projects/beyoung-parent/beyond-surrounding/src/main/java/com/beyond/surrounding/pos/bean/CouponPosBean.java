package com.beyond.surrounding.pos.bean;

import com.beyond.surrounding.bean.ResponseBean;

public class CouponPosBean extends ResponseBean{
	private String CouponNO;
	private Integer Price;
	private String IsUsed;
	private String IsAPP;
	
	public String getCouponNO() {
		return CouponNO;
	}
	public void setCouponNO(String couponNO) {
		CouponNO = couponNO;
	}
	public Integer getPrice() {
		return Price;
	}
	public void setPrice(Integer price) {
		Price = price;
	}
	public String getIsUsed() {
		return IsUsed;
	}
	public void setIsUsed(String isUsed) {
		IsUsed = isUsed;
	}
	public String getIsAPP() {
		return IsAPP;
	}
	public void setIsAPP(String isAPP) {
		IsAPP = isAPP;
	}
}
