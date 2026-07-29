package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class CAMPAIGN_REDEEM_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String center;
	private Date transaction_date;
	private String user_id;
	private String sno;
	
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
	
	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CAMPAIGN_REDEEM_ComposeKey) {
			final CAMPAIGN_REDEEM_ComposeKey other = (CAMPAIGN_REDEEM_ComposeKey) obj;
			if (center == other.center && transaction_date == other.transaction_date && user_id == other.user_id && sno == other.sno)
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
