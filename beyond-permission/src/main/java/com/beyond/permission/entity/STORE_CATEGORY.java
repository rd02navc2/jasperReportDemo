package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Store_Category")
@IdClass(STORE_CATEGORY_ComposeKey.class)
public class STORE_CATEGORY implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	@Id
	private String store_category_id;
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
	public String getStore_category_id() {
		return store_category_id;
	}
	public void setStore_category_id(String store_category_id) {
		this.store_category_id = store_category_id;
	}
}

class STORE_CATEGORY_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;
	private String store_category_id;

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

	public String getStore_category_id() {
		return store_category_id;
	}

	public void setStore_category_id(String store_category_id) {
		this.store_category_id = store_category_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof STORE_CATEGORY_ComposeKey) {
			final STORE_CATEGORY_ComposeKey other = (STORE_CATEGORY_ComposeKey) obj;
			if (c_no == other.c_no && CustomizedMainProductId == other.CustomizedMainProductId && store_category_id == other.store_category_id)
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
