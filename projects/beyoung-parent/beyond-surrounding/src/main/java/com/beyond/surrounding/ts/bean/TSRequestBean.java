package com.beyond.surrounding.ts.bean;

public class TSRequestBean {
	private String order_no;
	private String ec_order_no;
	private Integer pay_type;
	private Integer tx_type;
	private String layout;
	private Integer amt;
	private Integer amt_refund;
	private String order_desc;
	private String card_no;
	private String use_redeem;
	private Integer install_period;
	
	private String card_type;
	
	public Integer getAmt_refund() {
		return amt_refund;
	}
	public void setAmt_refund(Integer amt_refund) {
		this.amt_refund = amt_refund;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public Integer getTx_type() {
		return tx_type;
	}
	public void setTx_type(Integer tx_type) {
		this.tx_type = tx_type;
	}
	public String getUse_redeem() {
		return use_redeem;
	}
	public void setUse_redeem(String use_redeem) {
		this.use_redeem = use_redeem;
	}
	public Integer getPay_type() {
		return pay_type;
	}
	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public Integer getAmt() {
		return amt;
	}
	public void setAmt(Integer amt) {
		this.amt = amt;
	}
	public String getOrder_desc() {
		return order_desc;
	}
	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public Integer getInstall_period() {
		return install_period;
	}
	public void setInstall_period(Integer install_period) {
		this.install_period = install_period;
	}
	public String getEc_order_no() {
		return ec_order_no;
	}
	public void setEc_order_no(String ec_order_no) {
		this.ec_order_no = ec_order_no;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
}
