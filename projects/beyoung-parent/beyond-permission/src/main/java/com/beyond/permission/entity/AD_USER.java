package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AD_USER")
public class AD_USER {
	@Id
	private String user_principal;
	private String common_name;
	private String department;
	private String distinguished_name;
	private String mail;
	private String mobile;
	private String telephone_number;
	private String title;
	
	public String getUser_principal() {
		return user_principal;
	}
	public void setUser_principal(String user_principal) {
		this.user_principal = user_principal;
	}
	public String getCommon_name() {
		return common_name;
	}
	public void setCommon_name(String common_name) {
		this.common_name = common_name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDistinguished_name() {
		return distinguished_name;
	}
	public void setDistinguished_name(String distinguished_name) {
		this.distinguished_name = distinguished_name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTelephone_number() {
		return telephone_number;
	}
	public void setTelephone_number(String telephone_number) {
		this.telephone_number = telephone_number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
