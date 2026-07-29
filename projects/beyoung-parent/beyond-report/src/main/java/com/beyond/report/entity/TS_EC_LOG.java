package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TS_EC_LOG")
public class TS_EC_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String order_no;
	private String ec_order_no;
	private String type;
	private Double amt;
	private Double amt_refund;
	private String order_desc;
	private String card_no;
	private int install_period;
	private String use_redeem;
	private String ret_code;
	private String ret_msg;
	private Date access_date;
	private Date auth_date;
	private Date auth_cancel_date;
	private Date refund_date;
	
	@Transient
	private int rec_cnt;
	
	public Date getAuth_date() {
		return auth_date;
	}
	public void setAuth_date(Date auth_date) {
		this.auth_date = auth_date;
	}
	public Date getAuth_cancel_date() {
		return auth_cancel_date;
	}
	public void setAuth_cancel_date(Date auth_cancel_date) {
		this.auth_cancel_date = auth_cancel_date;
	}
	public Date getRefund_date() {
		return refund_date;
	}
	public void setRefund_date(Date refund_date) {
		this.refund_date = refund_date;
	}
	public Double getAmt_refund() {
		return amt_refund;
	}
	public void setAmt_refund(Double amt_refund) {
		this.amt_refund = amt_refund;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
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
	public int getInstall_period() {
		return install_period;
	}
	public void setInstall_period(int install_period) {
		this.install_period = install_period;
	}
	public String getUse_redeem() {
		return use_redeem;
	}
	public void setUse_redeem(String use_redeem) {
		this.use_redeem = use_redeem;
	}
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public int getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(int rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getEc_order_no() {
		return ec_order_no;
	}
	public void setEc_order_no(String ec_order_no) {
		this.ec_order_no = ec_order_no;
	}
}
