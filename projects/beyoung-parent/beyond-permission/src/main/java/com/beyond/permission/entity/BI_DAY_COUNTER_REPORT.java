package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BI_DAY_COUNTER_REPORT")
@IdClass(BI_DAY_COUNTER_REPORT_ComposeKey.class)
public class BI_DAY_COUNTER_REPORT implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String report_date;
	@Id
	private String floor;
	@Id
	private String dept_no;
	private String dept_name;
	@Id
	private String counter_no;
	private String counter_name;
	private Double day_tc_psa09a;
	private Double day_gross_profit;
	private Double day_ly_tc_psa09a;
	private Double day_tc_psa07;
	private Date access_date;
	
	public String getCounter_no() {
		return counter_no;
	}
	public void setCounter_no(String counter_no) {
		this.counter_no = counter_no;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public Double getDay_tc_psa09a() {
		return day_tc_psa09a;
	}
	public void setDay_tc_psa09a(Double day_tc_psa09a) {
		this.day_tc_psa09a = day_tc_psa09a;
	}
	public Double getDay_gross_profit() {
		return day_gross_profit;
	}
	public void setDay_gross_profit(Double day_gross_profit) {
		this.day_gross_profit = day_gross_profit;
	}
	public Double getDay_ly_tc_psa09a() {
		return day_ly_tc_psa09a;
	}
	public void setDay_ly_tc_psa09a(Double day_ly_tc_psa09a) {
		this.day_ly_tc_psa09a = day_ly_tc_psa09a;
	}
	public Double getDay_tc_psa07() {
		return day_tc_psa07;
	}
	public void setDay_tc_psa07(Double day_tc_psa07) {
		this.day_tc_psa07 = day_tc_psa07;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}

class BI_DAY_COUNTER_REPORT_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String report_date;
	private String floor;
	private String dept_no;
	private String counter_no;

	public String getReport_date() {
		return report_date;
	}

	public String getCounter_no() {
		return counter_no;
	}

	public void setCounter_no(String counter_no) {
		this.counter_no = counter_no;
	}

	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDept_no() {
		return dept_no;
	}

	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BI_DAY_COUNTER_REPORT_ComposeKey) {
			final BI_DAY_COUNTER_REPORT_ComposeKey other = (BI_DAY_COUNTER_REPORT_ComposeKey) obj;
			if (report_date == other.report_date && floor == other.floor && dept_no == other.dept_no && counter_no == other.counter_no)
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
