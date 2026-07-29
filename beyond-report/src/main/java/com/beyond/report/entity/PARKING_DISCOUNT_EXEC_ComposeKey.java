package com.beyond.report.entity;

import java.io.Serializable;

class PARKING_DISCOUNT_EXEC_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer p_no;
	private String user_id;
	private String disc_id;
	
	public String getDisc_id() {
		return disc_id;
	}

	public void setDisc_id(String disc_id) {
		this.disc_id = disc_id;
	}

	public Integer getP_no() {
		return p_no;
	}

	public void setP_no(Integer p_no) {
		this.p_no = p_no;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PARKING_DISCOUNT_EXEC_ComposeKey) {
			final PARKING_DISCOUNT_EXEC_ComposeKey other = (PARKING_DISCOUNT_EXEC_ComposeKey) obj;
			if (p_no == other.p_no && user_id == other.user_id && disc_id == other.disc_id)
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
