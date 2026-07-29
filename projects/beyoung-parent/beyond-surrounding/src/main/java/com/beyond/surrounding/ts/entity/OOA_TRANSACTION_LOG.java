package com.beyond.surrounding.ts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "OOA_TRANSACTION_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OOA_TRANSACTION_LOG implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String order_id;
	private String pos_center;
	private String pos_counter_id;
	private String pos_product_name;
	private Integer pos_amount;
	private String pos_id;
	private Date pos_date_time;
	private Date access_date;
	private String transaction_type;
	private String transaction_id;
	private Date transaction_date;
	private String one_time_key;
 	private Integer refund_transaction_id;
 	private Date refund_transaction_date;
 	private String invoice_no;
 	private String wallet_provider;
 	
 	@Transient
	protected String Code;
 	@Transient
	protected String Message;
 	@Transient
	protected String TransTime;
 	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getPos_center() {
		return pos_center;
	}
	public void setPos_center(String pos_center) {
		this.pos_center = pos_center;
	}
	public String getPos_counter_id() {
		return pos_counter_id;
	}
	public void setPos_counter_id(String pos_counter_id) {
		this.pos_counter_id = pos_counter_id;
	}
	public String getPos_product_name() {
		return pos_product_name;
	}
	public void setPos_product_name(String pos_product_name) {
		this.pos_product_name = pos_product_name;
	}
	public Integer getPos_amount() {
		return pos_amount;
	}
	public void setPos_amount(Integer pos_amount) {
		this.pos_amount = pos_amount;
	}
	public String getPos_id() {
		return pos_id;
	}
	public void setPos_id(String pos_id) {
		this.pos_id = pos_id;
	}
	public Date getPos_date_time() {
		return pos_date_time;
	}
	public void setPos_date_time(Date pos_date_time) {
		this.pos_date_time = pos_date_time;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public Date getTransaction_date() {
		return transaction_date;
	}
	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}
	public String getOne_time_key() {
		return one_time_key;
	}
	public void setOne_time_key(String one_time_key) {
		this.one_time_key = one_time_key;
	}
	public Integer getRefund_transaction_id() {
		return refund_transaction_id;
	}
	public void setRefund_transaction_id(Integer refund_transaction_id) {
		this.refund_transaction_id = refund_transaction_id;
	}
	public Date getRefund_transaction_date() {
		return refund_transaction_date;
	}
	public void setRefund_transaction_date(Date refund_transaction_date) {
		this.refund_transaction_date = refund_transaction_date;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
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
	public String getTransTime() {
		return TransTime;
	}
	public void setTransTime(String transTime) {
		TransTime = transTime;
	}
	public String getWallet_provider() {
		return wallet_provider;
	}
	public void setWallet_provider(String wallet_provider) {
		this.wallet_provider = wallet_provider;
	}
}
