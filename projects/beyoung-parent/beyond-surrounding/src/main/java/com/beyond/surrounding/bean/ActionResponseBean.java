package com.beyond.surrounding.bean;

public class ActionResponseBean extends ResponseBean{
	private String sActionType;
	private String sMoPayType;
	private String sCouponType;
	private Integer iPrice;
	private String sPayType;
	
	public String getsActionType() {
		return sActionType;
	}
	public void setsActionType(String sActionType) {
		this.sActionType = sActionType;
	}
	public String getsMoPayType() {
		return sMoPayType;
	}
	public void setsMoPayType(String sMoPayType) {
		this.sMoPayType = sMoPayType;
	}
	public String getsCouponType() {
		return sCouponType;
	}
	public void setsCouponType(String sCouponType) {
		this.sCouponType = sCouponType;
	}
	public Integer getiPrice() {
		return iPrice;
	}
	public void setiPrice(Integer iPrice) {
		this.iPrice = iPrice;
	}
	public String getsPayType() {
		return sPayType;
	}
	public void setsPayType(String sPayType) {
		this.sPayType = sPayType;
	}
	
}
