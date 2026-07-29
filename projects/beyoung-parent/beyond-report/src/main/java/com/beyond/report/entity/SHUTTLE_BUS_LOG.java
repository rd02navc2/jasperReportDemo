package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "SHUTTLE_BUS_LOG")
public class SHUTTLE_BUS_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer sn;
	private String c_no;
	private String device_no;
	private String user_id;
	private String user_name;
	private String ride_mark;
	private Date access_date;

	@Transient
	private Integer up_cnt;
	@Transient
	private Integer down_cnt;
	@Transient
	private Integer rec_cnt;
	
	public Integer getUp_cnt() {
		return up_cnt;
	}
	public void setUp_cnt(Integer up_cnt) {
		this.up_cnt = up_cnt;
	}
	public Integer getDown_cnt() {
		return down_cnt;
	}
	public void setDown_cnt(Integer down_cnt) {
		this.down_cnt = down_cnt;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getRide_mark() {
		return ride_mark;
	}
	public void setRide_mark(String ride_mark) {
		this.ride_mark = ride_mark;
	}
	public String getDevice_no() {
		return device_no;
	}
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}

