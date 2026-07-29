package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BI_CONTRAST_DAY")
public class BI_CONTRAST_DAY implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String report_date;
	private String contrast_date;
	private String access_id;
	private Date access_date;
	
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getContrast_date() {
		return contrast_date;
	}
	public void setContrast_date(String contrast_date) {
		this.contrast_date = contrast_date;
	}
	public String getAccess_id() {
		return access_id;
	}
	public void setAccess_id(String access_id) {
		this.access_id = access_id;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}

}
