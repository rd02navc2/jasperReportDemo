package com.beyond.permission.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Order_Detail_Receiver")
public class ORDER_DETAIL_RECEIVER {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	private String ReceiverName;
	private String ReceiverPhone;
	private String ReceiverMobile;
	private String ReceiverZipcode;
	private String ReceiverAddress;
	private Date create_date;
	private Date access_date;
	private String remark;	
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public String getReceiverPhone() {
		return ReceiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		ReceiverPhone = receiverPhone;
	}
	public String getReceiverMobile() {
		return ReceiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		ReceiverMobile = receiverMobile;
	}
	public String getReceiverZipcode() {
		return ReceiverZipcode;
	}
	public void setReceiverZipcode(String receiverZipcode) {
		ReceiverZipcode = receiverZipcode;
	}
	public String getReceiverAddress() {
		return ReceiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		ReceiverAddress = receiverAddress;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}
