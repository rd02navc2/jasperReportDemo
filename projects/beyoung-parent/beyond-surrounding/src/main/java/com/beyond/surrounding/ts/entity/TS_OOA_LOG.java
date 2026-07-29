package com.beyond.surrounding.ts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TS_OOA_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TS_OOA_LOG implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String order_no;
	private String payment_type;
	private String card_no;
	private String case_id;
	private String trade_no;
	private Double amt;
	private Double amt_refund;
	private String refund_order_no;
	private String refund_trade_no;
	private String refund_status;
	private String ret_code;
	private String ret_msg;
	private Date create_date;
	private Date confirm_date;
	private Date notify_date;
	private Date refund_date;
	
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
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Double getAmt_refund() {
		return amt_refund;
	}
	public void setAmt_refund(Double amt_refund) {
		this.amt_refund = amt_refund;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
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
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}
	public Date getNotify_date() {
		return notify_date;
	}
	public void setNotify_date(Date notify_date) {
		this.notify_date = notify_date;
	}
	public Date getRefund_date() {
		return refund_date;
	}
	public void setRefund_date(Date refund_date) {
		this.refund_date = refund_date;
	}
	public String getRefund_order_no() {
		return refund_order_no;
	}
	public void setRefund_order_no(String refund_order_no) {
		this.refund_order_no = refund_order_no;
	}
	public String getRefund_trade_no() {
		return refund_trade_no;
	}
	public void setRefund_trade_no(String refund_trade_no) {
		this.refund_trade_no = refund_trade_no;
	}
}
