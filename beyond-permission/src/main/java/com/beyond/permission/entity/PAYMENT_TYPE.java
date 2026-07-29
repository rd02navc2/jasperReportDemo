package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payment_Type")
@IdClass(PAYMENT_TYPE_ComposeKey.class)
public class PAYMENT_TYPE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	@Id
	private String payment_type_id;
	
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getCustomizedMainProductId() {
		return CustomizedMainProductId;
	}
	public void setCustomizedMainProductId(String customizedMainProductId) {
		CustomizedMainProductId = customizedMainProductId;
	}
	public String getPayment_type_id() {
		return payment_type_id;
	}
	public void setPayment_type_id(String payment_type_id) {
		this.payment_type_id = payment_type_id;
	}
}

class PAYMENT_TYPE_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;
	private String payment_type_id;

	public String getC_no() {
		return c_no;
	}

	public void setC_no(String c_no) {
		this.c_no = c_no;
	}

	public String getCustomizedMainProductId() {
		return CustomizedMainProductId;
	}

	public void setCustomizedMainProductId(String customizedMainProductId) {
		CustomizedMainProductId = customizedMainProductId;
	}

	public String getPayment_type_id() {
		return payment_type_id;
	}

	public void setPayment_type_id(String payment_type_id) {
		this.payment_type_id = payment_type_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PAYMENT_TYPE_ComposeKey) {
			final PAYMENT_TYPE_ComposeKey other = (PAYMENT_TYPE_ComposeKey) obj;
			if (c_no == other.c_no && CustomizedMainProductId == other.CustomizedMainProductId && payment_type_id == other.payment_type_id)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
