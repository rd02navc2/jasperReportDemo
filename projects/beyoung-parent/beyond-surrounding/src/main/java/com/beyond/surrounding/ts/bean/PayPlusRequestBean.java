package com.beyond.surrounding.ts.bean;

public class PayPlusRequestBean {
	public String order_no;
	public String member_id;
	public String payment_type;
	public String card_token;
	public RequestParams RequestParams;
	
	public String trans_no;
	
	
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getTrans_no() {
		return trans_no;
	}
	public void setTrans_no(String trans_no) {
		this.trans_no = trans_no;
	}
	public String getCard_token() {
		return card_token;
	}
	public void setCard_token(String card_token) {
		this.card_token = card_token;
	}
	public RequestParams getRequestParams() {
		return RequestParams;
	}
	public void setRequestParams(RequestParams requestParams) {
		RequestParams = requestParams;
	}
	
}
