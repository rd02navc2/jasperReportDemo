package com.beyond.permission.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTHER_SALE")
public class OTHER_SALE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer sn;
	private String sale_name;
	private Date sale_date;
	private Double sale_amt;
	private String remark;
	
	@Transient
	private Double day_other;

	@Transient
	private Double month_other;

	@Transient
	private Double month_accu_other;
	
	@Transient
	private Double year_accu_other;
	
	@Transient
	private Double day_ly_other;

	@Transient
	private Double month_accu_ly_other;
	
	@Transient
	private Double year_accu_ly_other;
	
	public Double getDay_ly_other() {
		return day_ly_other;
	}
	public void setDay_ly_other(Double day_ly_other) {
		this.day_ly_other = day_ly_other;
	}
	public Double getMonth_accu_ly_other() {
		return month_accu_ly_other;
	}
	public void setMonth_accu_ly_other(Double month_accu_ly_other) {
		this.month_accu_ly_other = month_accu_ly_other;
	}
	public Double getYear_accu_ly_other() {
		return year_accu_ly_other;
	}
	public void setYear_accu_ly_other(Double year_accu_ly_other) {
		this.year_accu_ly_other = year_accu_ly_other;
	}
	public Double getDay_other() {
		return day_other;
	}
	public void setDay_other(Double day_other) {
		this.day_other = day_other;
	}
	public Double getMonth_other() {
		return month_other;
	}
	public void setMonth_other(Double month_other) {
		this.month_other = month_other;
	}
	public Double getMonth_accu_other() {
		return month_accu_other;
	}
	public void setMonth_accu_other(Double month_accu_other) {
		this.month_accu_other = month_accu_other;
	}
	public Double getYear_accu_other() {
		return year_accu_other;
	}
	public void setYear_accu_other(Double year_accu_other) {
		this.year_accu_other = year_accu_other;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getSale_name() {
		return sale_name;
	}
	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}
	public Date getSale_date() {
		return sale_date;
	}
	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}
	public Double getSale_amt() {
		return sale_amt;
	}
	public void setSale_amt(Double sale_amt) {
		this.sale_amt = sale_amt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}