package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

public class BUDGET_DAY_DETAIL_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date b_month;
	private String floor;
	private String dept_id;
	private String counter_id;

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public Date getB_month() {
		return b_month;
	}

	public void setB_month(Date b_month) {
		this.b_month = b_month;
	}

	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getCounter_id() {
		return counter_id;
	}

	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BUDGET_DAY_DETAIL_ComposeKey) {
			final BUDGET_DAY_DETAIL_ComposeKey other = (BUDGET_DAY_DETAIL_ComposeKey) obj;
			if (b_month == other.b_month && dept_id == other.dept_id && counter_id == other.counter_id && floor == other.floor)
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
