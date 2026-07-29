package com.beyond.surrounding.ts.bean;

import com.beyond.surrounding.bean.ResponseBean;

public class OOAResponseBean extends ResponseBean{
	private String sOrderNO;
	private String sWebUrl;
	private String sAndroidUrl;
	private String sIOSUrl;
	
	private String sPaymentStatus;
	private String sPaymentDateTime;
	private String sRefundStatus;
	private String sRefundDateTime;
	private String sRtnCode;
	private String sRtnMsg;
	
	public String getsOrderNO() {
		return sOrderNO;
	}
	public void setsOrderNO(String sOrderNO) {
		this.sOrderNO = sOrderNO;
	}
	public String getsWebUrl() {
		return sWebUrl;
	}
	public void setsWebUrl(String sWebUrl) {
		this.sWebUrl = sWebUrl;
	}
	public String getsAndroidUrl() {
		return sAndroidUrl;
	}
	public void setsAndroidUrl(String sAndroidUrl) {
		this.sAndroidUrl = sAndroidUrl;
	}
	public String getsIOSUrl() {
		return sIOSUrl;
	}
	public void setsIOSUrl(String sIOSUrl) {
		this.sIOSUrl = sIOSUrl;
	}
	public String getsRtnCode() {
		return sRtnCode;
	}
	public void setsRtnCode(String sRtnCode) {
		this.sRtnCode = sRtnCode;
	}
	public String getsRtnMsg() {
		return sRtnMsg;
	}
	public void setsRtnMsg(String sRtnMsg) {
		this.sRtnMsg = sRtnMsg;
	}
	public String getsPaymentStatus() {
		return sPaymentStatus;
	}
	public void setsPaymentStatus(String sPaymentStatus) {
		this.sPaymentStatus = sPaymentStatus;
	}
	public String getsPaymentDateTime() {
		return sPaymentDateTime;
	}
	public void setsPaymentDateTime(String sPaymentDateTime) {
		this.sPaymentDateTime = sPaymentDateTime;
	}
	public String getsRefundStatus() {
		return sRefundStatus;
	}
	public void setsRefundStatus(String sRefundStatus) {
		this.sRefundStatus = sRefundStatus;
	}
	public String getsRefundDateTime() {
		return sRefundDateTime;
	}
	public void setsRefundDateTime(String sRefundDateTime) {
		this.sRefundDateTime = sRefundDateTime;
	}
	
}
