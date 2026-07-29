package com.beyond.permission.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "USER_PERMISSION")
public class USER_PERMISSION {
	/** ���{��²��A�PDB Schema���X�J�C���]Composite PK�C */
	private String c_no;
//	@Id
	private String user_id;
	private String res_id;
	private String user_name;
	private String channel_id;
	private String channel_name;
	private String division_id;
	private String division_name;
	private String dept_id;
	private String dept_name;
	private String lv4_id;
	private String lv4_name;
	private String lv5_id;
	private String lv5_name;
	private String operation_id;
	private String create_user;
	private Date create_date;
	private String update_user;
	private Date update_date;

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

	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getDivision_id() {
		return division_id;
	}

	public void setDivision_id(String division_id) {
		this.division_id = division_id;
	}

	public String getDivision_name() {
		return division_name;
	}

	public void setDivision_name(String division_name) {
		this.division_name = division_name;
	}

	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getLv4_id() {
		return lv4_id;
	}

	public void setLv4_id(String lv4_id) {
		this.lv4_id = lv4_id;
	}

	public String getLv4_name() {
		return lv4_name;
	}

	public void setLv4_name(String lv4_name) {
		this.lv4_name = lv4_name;
	}

	public String getLv5_id() {
		return lv5_id;
	}

	public void setLv5_id(String lv5_id) {
		this.lv5_id = lv5_id;
	}

	public String getLv5_name() {
		return lv5_name;
	}

	public void setLv5_name(String lv5_name) {
		this.lv5_name = lv5_name;
	}

	public String getOperation_id() {
		return operation_id;
	}

	public void setOperation_id(String operation_id) {
		this.operation_id = operation_id;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	@Override
	public String toString() {
		return "USER_PERMISSION [" + (user_id != null ? "user_id=" + user_id + ", " : "")
				+ (channel_id != null ? "channel_id=" + channel_id + ", " : "")
				+ (division_id != null ? "division_id=" + division_id + ", " : "")
				+ (dept_id != null ? "dept_id=" + dept_id + ", " : "")
				+ (lv4_id != null ? "lv4_id=" + lv4_id + ", " : "") + (lv5_id != null ? "lv5_id=" + lv5_id + ", " : "")
				+ (operation_id != null ? "operation_id=" + operation_id : "") + "]";
	}
}
