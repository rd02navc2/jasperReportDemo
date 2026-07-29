package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BUDGET_DAY_HEADER")
public class BUDGET_DAY_HEADER implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String b_month;
	private String status;
	private String ReturnReason;
	private Date access_date;
	private String access_id;
	private Date approve_date;
	private String approve_id;
	private String reject_id;
	private Date reject_date;
	
	public String getReturnReason() {
		return ReturnReason;
	}
	public void setReturnReason(String returnReason) {
		ReturnReason = returnReason;
	}
	public String getReject_id() {
		return reject_id;
	}
	public void setReject_id(String reject_id) {
		this.reject_id = reject_id;
	}
	public Date getReject_date() {
		return reject_date;
	}
	public void setReject_date(Date reject_date) {
		this.reject_date = reject_date;
	}
	public Date getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}
	public String getApprove_id() {
		return approve_id;
	}
	public void setApprove_id(String approve_id) {
		this.approve_id = approve_id;
	}
	public String getB_month() {
		return b_month;
	}
	public void setB_month(String b_month) {
		this.b_month = b_month;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public String getAccess_id() {
		return access_id;
	}
	public void setAccess_id(String access_id) {
		this.access_id = access_id;
	}
	
	
}

