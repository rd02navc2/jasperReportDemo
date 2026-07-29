package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BI_MAIL_SET")
public class BI_MAIL_SET implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String set_id;
	private String set_name;
	private String mail_from;
	private String mail_from_name;
	private String mail_to;
	private String mail_cc;
	private String mail_bcc;
	private String access_id;
	private Date access_date;
	
	public String getSet_id() {
		return set_id;
	}
	public void setSet_id(String set_id) {
		this.set_id = set_id;
	}
	public String getSet_name() {
		return set_name;
	}
	public void setSet_name(String set_name) {
		this.set_name = set_name;
	}
	public String getMail_from() {
		return mail_from;
	}
	public void setMail_from(String mail_from) {
		this.mail_from = mail_from;
	}
	public String getMail_from_name() {
		return mail_from_name;
	}
	public void setMail_from_name(String mail_from_name) {
		this.mail_from_name = mail_from_name;
	}
	public String getMail_to() {
		return mail_to;
	}
	public void setMail_to(String mail_to) {
		this.mail_to = mail_to;
	}
	public String getMail_cc() {
		return mail_cc;
	}
	public void setMail_cc(String mail_cc) {
		this.mail_cc = mail_cc;
	}
	public String getMail_bcc() {
		return mail_bcc;
	}
	public void setMail_bcc(String mail_bcc) {
		this.mail_bcc = mail_bcc;
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
