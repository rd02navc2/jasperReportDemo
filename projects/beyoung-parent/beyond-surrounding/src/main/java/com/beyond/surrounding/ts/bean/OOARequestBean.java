package com.beyond.surrounding.ts.bean;

public class OOARequestBean {
	private String order_no;
	private String card_no;
	private Integer amt;
	private String payment_type;
	private String new_order_no;
	private Integer refund_amt;
	
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public Integer getAmt() {
		return amt;
	}
	public void setAmt(Integer amt) {
		this.amt = amt;
	}
	public String getNew_order_no() {
		return new_order_no;
	}
	public void setNew_order_no(String new_order_no) {
		this.new_order_no = new_order_no;
	}
	public Integer getRefund_amt() {
		return refund_amt;
	}
	public void setRefund_amt(Integer refund_amt) {
		this.refund_amt = refund_amt;
	}

}
