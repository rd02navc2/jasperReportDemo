package com.beyond.report.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BUDGET_DAY_APPLY")
@IdClass(BUDGET_DAY_APPLY_ComposeKey.class)
public class BUDGET_DAY_APPLY implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String b_month;
	@Id
	private String apply_id;
	private Date apply_date;
	
	@Transient
	private String email;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getB_month() {
		return b_month;
	}
	public void setB_month(String b_month) {
		this.b_month = b_month;
	}
	public String getApply_id() {
		return apply_id;
	}
	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
	}
	public Date getApply_date() {
		return apply_date;
	}
	public void setApply_date(Date apply_date) {
		this.apply_date = apply_date;
	}
}