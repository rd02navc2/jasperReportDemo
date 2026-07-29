package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BI_DAY_FLOOR_REPORT")
@IdClass(BI_DAY_FLOOR_REPORT_ComposeKey.class)
public class BI_DAY_FLOOR_REPORT implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String report_date;
	@Id
	private String floor;
	private Double day_tc_psa09a;
	private Double day_gross_profit;
	private Double day_ly_tc_psa09a;
	private Double day_tc_psa07;
	private Double tc_psa09a_month_accu;
	private Date access_date;
	
	public Double getTc_psa09a_month_accu() {
		return tc_psa09a_month_accu;
	}
	public void setTc_psa09a_month_accu(Double tc_psa09a_month_accu) {
		this.tc_psa09a_month_accu = tc_psa09a_month_accu;
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

class BI_DAY_FLOOR_REPORT_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String report_date;
	private String floor;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BI_DAY_FLOOR_REPORT_ComposeKey) {
			final BI_DAY_FLOOR_REPORT_ComposeKey other = (BI_DAY_FLOOR_REPORT_ComposeKey) obj;
			if (report_date == other.report_date && floor == other.floor)
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
