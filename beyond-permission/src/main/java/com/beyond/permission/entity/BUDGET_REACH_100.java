package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BUDGET_REACH_100")
@IdClass(BUDGET_REACH_100_ComposeKey.class)
public class BUDGET_REACH_100 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Date start_date;
	@Id
	private Date end_date;
	@Id
	private String floor;
	@Id
	private String dept_id;
	private String dept_name;
	@Id
	private String counter_id;
	private String counter_name;
	private String org_name;
	private Double gross_profit_rate;
	private String remark;
	
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getGross_profit_rate() {
		return gross_profit_rate;
	}
	public void setGross_profit_rate(Double gross_profit_rate) {
		this.gross_profit_rate = gross_profit_rate;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
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
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
}

class BUDGET_REACH_100_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date start_date;
	private Date end_date;
	private String floor;
	private String dept_id;
	private String counter_id;

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
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
		if (obj instanceof BUDGET_REACH_100_ComposeKey) {
			final BUDGET_REACH_100_ComposeKey other = (BUDGET_REACH_100_ComposeKey) obj;
			if (start_date == other.start_date && end_date == other.end_date && dept_id == other.dept_id && counter_id == other.counter_id && floor == other.floor)
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