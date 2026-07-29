package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class BUDGET_DAY_APPLY_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date b_month;
	private String apply_id;

	public Date getB_month() {
		return b_month;
	}

	public void setB_month(Date b_month) {
		this.b_month = b_month;
	}

	public String getApply_id() {
		return apply_id;
	}

	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BUDGET_DAY_APPLY_ComposeKey) {
			final BUDGET_DAY_APPLY_ComposeKey other = (BUDGET_DAY_APPLY_ComposeKey) obj;
			if (b_month == other.b_month && apply_id == other.apply_id)
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
