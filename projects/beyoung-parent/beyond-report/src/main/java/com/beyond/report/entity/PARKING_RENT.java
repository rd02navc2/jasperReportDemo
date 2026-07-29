package com.beyond.report.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PARKING_RENT")
public class PARKING_RENT implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String car_no;
	private String user_name;
	private String is_unlimited_date;
	private Date start_date;
	private Date end_date;
	private Date access_date;
	private String access_id;
	
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getIs_unlimited_date() {
		return is_unlimited_date;
	}
	public void setIs_unlimited_date(String is_unlimited_date) {
		this.is_unlimited_date = is_unlimited_date;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
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