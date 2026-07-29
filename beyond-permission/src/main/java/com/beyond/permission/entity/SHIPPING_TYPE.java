package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Shipping_Type")
@IdClass(SHIPPING_TYPE_ComposeKey.class)
public class SHIPPING_TYPE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	@Id
	private String shipping_type_id;
	
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
	public String getShipping_type_id() {
		return shipping_type_id;
	}
	public void setShipping_type_id(String shipping_type_id) {
		this.shipping_type_id = shipping_type_id;
	}
}

class SHIPPING_TYPE_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;
	private String shipping_type_id;

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

	public String getShipping_type_id() {
		return shipping_type_id;
	}

	public void setShipping_type_id(String shipping_type_id) {
		this.shipping_type_id = shipping_type_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SHIPPING_TYPE_ComposeKey) {
			final SHIPPING_TYPE_ComposeKey other = (SHIPPING_TYPE_ComposeKey) obj;
			if (c_no == other.c_no && CustomizedMainProductId == other.CustomizedMainProductId && shipping_type_id == other.shipping_type_id)
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
