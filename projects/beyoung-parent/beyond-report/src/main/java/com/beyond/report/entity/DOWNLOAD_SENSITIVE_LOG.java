package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "DOWNLOAD_SENSITIVE_LOG")
public class DOWNLOAD_SENSITIVE_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer sn;
	private Date access_date;
	private String user_id;
	private String system;
	private Integer rec_count;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public Integer getRec_count() {
		return rec_count;
	}
	public void setRec_count(Integer rec_count) {
		this.rec_count = rec_count;
	}
}

