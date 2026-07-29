package com.beyond.permission.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SUSHI_UPLOAD")
public class SUSHI_UPLOAD {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String NOW_DATE;
	private Double Cust;
	private Double Food;
	private Date create_date;
	private Date update_date;
	
	@Transient
	private Integer rec_cnt;
	
	@Transient
	private Double day_cust;
	@Transient
	private Double day_food;

	@Transient
	private Double month_accu_cust;
	@Transient
	private Double month_accu_food;

	@Transient
	private Double month_cust;
	@Transient
	private Double month_food;

	@Transient
	private Double year_accu_cust;
	@Transient
	private Double year_accu_food;

	public Double getDay_cust() {
		return day_cust;
	}
	public void setDay_cust(Double day_cust) {
		this.day_cust = day_cust;
	}
	public Double getDay_food() {
		return day_food;
	}
	public void setDay_food(Double day_food) {
		this.day_food = day_food;
	}
	public Double getMonth_accu_cust() {
		return month_accu_cust;
	}
	public void setMonth_accu_cust(Double month_accu_cust) {
		this.month_accu_cust = month_accu_cust;
	}
	public Double getMonth_accu_food() {
		return month_accu_food;
	}
	public void setMonth_accu_food(Double month_accu_food) {
		this.month_accu_food = month_accu_food;
	}
	public Double getMonth_cust() {
		return month_cust;
	}
	public void setMonth_cust(Double month_cust) {
		this.month_cust = month_cust;
	}
	public Double getMonth_food() {
		return month_food;
	}
	public void setMonth_food(Double month_food) {
		this.month_food = month_food;
	}
	public Double getYear_accu_cust() {
		return year_accu_cust;
	}
	public void setYear_accu_cust(Double year_accu_cust) {
		this.year_accu_cust = year_accu_cust;
	}
	public Double getYear_accu_food() {
		return year_accu_food;
	}
	public void setYear_accu_food(Double year_accu_food) {
		this.year_accu_food = year_accu_food;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getNOW_DATE() {
		return NOW_DATE;
	}
	public void setNOW_DATE(String nOW_DATE) {
		NOW_DATE = nOW_DATE;
	}
	public Double getCust() {
		return Cust;
	}
	public void setCust(Double cust) {
		Cust = cust;
	}
	public Double getFood() {
		return Food;
	}
	public void setFood(Double food) {
		Food = food;
	}
}
