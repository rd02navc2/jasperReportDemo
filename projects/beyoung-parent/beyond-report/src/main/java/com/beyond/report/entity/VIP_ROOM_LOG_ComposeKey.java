package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class VIP_ROOM_LOG_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String center;
	private Date transaction_date;
	private String user_id;
	
	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public Date getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VIP_ROOM_LOG_ComposeKey) {
			final VIP_ROOM_LOG_ComposeKey other = (VIP_ROOM_LOG_ComposeKey) obj;
			if (center == other.center && transaction_date == other.transaction_date && user_id == other.user_id)
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