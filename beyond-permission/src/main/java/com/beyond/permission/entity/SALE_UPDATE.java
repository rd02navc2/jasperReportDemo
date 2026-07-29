package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALE_UPDATE")
@IdClass(SALE_UPDATE_ComposeKey.class)
public class SALE_UPDATE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String floor;
	@Id
	private String dept_id;
	private String dept_name;
	@Id
	private String counter_id;
	private String counter_name;
	private String org_name;
	private Double old_sale_update_amt;
	private Double new_sale_update_amt;
	private Double diff_sale_update_amt;
	private Double new_gross_profit;
	private Date start_date;
	private String remark;
	
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
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getCounter_id() {
		return counter_id;
	}
	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public Double getOld_sale_update_amt() {
		return old_sale_update_amt;
	}
	public void setOld_sale_update_amt(Double old_sale_update_amt) {
		this.old_sale_update_amt = old_sale_update_amt;
	}
	public Double getNew_sale_update_amt() {
		return new_sale_update_amt;
	}
	public void setNew_sale_update_amt(Double new_sale_update_amt) {
		this.new_sale_update_amt = new_sale_update_amt;
	}
	public Double getDiff_sale_update_amt() {
		return diff_sale_update_amt;
	}
	public void setDiff_sale_update_amt(Double diff_sale_update_amt) {
		this.diff_sale_update_amt = diff_sale_update_amt;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getNew_gross_profit() {
		return new_gross_profit;
	}
	public void setNew_gross_profit(Double new_gross_profit) {
		this.new_gross_profit = new_gross_profit;
	}
}

class SALE_UPDATE_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String floor;
	private String dept_id;
	private String counter_id;

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
		if (obj instanceof SALE_UPDATE_ComposeKey) {
			final SALE_UPDATE_ComposeKey other = (SALE_UPDATE_ComposeKey) obj;
			if (dept_id == other.dept_id && counter_id == other.counter_id && floor == other.floor)
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