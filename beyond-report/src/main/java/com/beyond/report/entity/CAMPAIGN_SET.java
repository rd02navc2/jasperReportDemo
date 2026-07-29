package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CAMPAIGN_SET")
public class CAMPAIGN_SET implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String sno;
	private String campaign_name;
	private String is_active;
	private String w_1;
	private String w_2;
	private String w_3;
	private String w_4;
	private String w_5;
	private String w_6;
	private String w_7;
	private String is_unlimited_unit_today;
	private Integer today_unit_max;
	private String is_unlimited_unit;
	private Integer month_unit_max;
	private Date access_date;
	private String access_id;
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getCampaign_name() {
		return campaign_name;
	}
	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getW_1() {
		return w_1;
	}
	public void setW_1(String w_1) {
		this.w_1 = w_1;
	}
	public String getW_2() {
		return w_2;
	}
	public void setW_2(String w_2) {
		this.w_2 = w_2;
	}
	public String getW_3() {
		return w_3;
	}
	public void setW_3(String w_3) {
		this.w_3 = w_3;
	}
	public String getW_4() {
		return w_4;
	}
	public void setW_4(String w_4) {
		this.w_4 = w_4;
	}
	public String getW_5() {
		return w_5;
	}
	public void setW_5(String w_5) {
		this.w_5 = w_5;
	}
	public String getW_6() {
		return w_6;
	}
	public void setW_6(String w_6) {
		this.w_6 = w_6;
	}
	public String getW_7() {
		return w_7;
	}
	public void setW_7(String w_7) {
		this.w_7 = w_7;
	}
	public Integer getMonth_unit_max() {
		return month_unit_max;
	}
	public void setMonth_unit_max(Integer month_unit_max) {
		this.month_unit_max = month_unit_max;
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
	public String getIs_unlimited_unit() {
		return is_unlimited_unit;
	}
	public void setIs_unlimited_unit(String is_unlimited_unit) {
		this.is_unlimited_unit = is_unlimited_unit;
	}
	public String getIs_unlimited_unit_today() {
		return is_unlimited_unit_today;
	}
	public void setIs_unlimited_unit_today(String is_unlimited_unit_today) {
		this.is_unlimited_unit_today = is_unlimited_unit_today;
	}
	public Integer getToday_unit_max() {
		return today_unit_max;
	}
	public void setToday_unit_max(Integer today_unit_max) {
		this.today_unit_max = today_unit_max;
	}
}
