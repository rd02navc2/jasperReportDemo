package com.beyond.surrounding.ts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TS_PAYPLUS_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TS_PAYPLUS_LOG implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String order_no;
	private String member_id;
	private String barcode;
	private String payment_type;
	private String is_bind_success;
	private String is_bind_delete;
	private String bank_no;
	private String card_name;
	private String card_number;
	private String card_token;
	private String card_type;
	private String card_status;
	private String ret_code;
	private String ret_msg;
	private String hpp_url;
	private Date access_date;
	
	@Transient
	private String Code;
	@Transient
	private String Message;
	
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getIs_bind_success() {
		return is_bind_success;
	}
	public void setIs_bind_success(String is_bind_success) {
		this.is_bind_success = is_bind_success;
	}
	public String getIs_bind_delete() {
		return is_bind_delete;
	}
	public void setIs_bind_delete(String is_bind_delete) {
		this.is_bind_delete = is_bind_delete;
	}
	public String getBank_no() {
		return bank_no;
	}
	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getCard_token() {
		return card_token;
	}
	public void setCard_token(String card_token) {
		this.card_token = card_token;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_status() {
		return card_status;
	}
	public void setCard_status(String card_status) {
		this.card_status = card_status;
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
	public String getHpp_url() {
		return hpp_url;
	}
	public void setHpp_url(String hpp_url) {
		this.hpp_url = hpp_url;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
