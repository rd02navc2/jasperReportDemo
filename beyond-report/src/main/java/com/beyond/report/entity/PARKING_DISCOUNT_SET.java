package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "PARKING_DISCOUNT_SET")
public class PARKING_DISCOUNT_SET implements Serializable {
	private static final long serialVersionUID = 1L;
	private String is_active;
	@Id
	private String disc_id;
	private String disc_name;
	private String disc_desc;
	private Double disc_hour;
	private int sort_order;
	private Date access_date;
	private String access_id;
	
	private Integer p_no;
	private Date booking_date;
	private String is_used;
	private Double promote_amt;
	private String is_unlimited_hour;
	private Integer hour_max;
	
	@Transient
	private Double used_hour;
	
	public String getIs_unlimited_hour() {
		return is_unlimited_hour;
	}
	public void setIs_unlimited_hour(String is_unlimited_hour) {
		this.is_unlimited_hour = is_unlimited_hour;
	}
	public Integer getHour_max() {
		return hour_max;
	}
	public void setHour_max(Integer hour_max) {
		this.hour_max = hour_max;
	}
	public Double getPromote_amt() {
		return promote_amt;
	}
	public void setPromote_amt(Double promote_amt) {
		this.promote_amt = promote_amt;
	}
	public Double getUsed_hour() {
		return used_hour;
	}
	public void setUsed_hour(Double used_hour) {
		this.used_hour = used_hour;
	}
	public Integer getP_no() {
		return p_no;
	}
	public void setP_no(Integer p_no) {
		this.p_no = p_no;
	}
	public Date getBooking_date() {
		return booking_date;
	}
	public void setBooking_date(Date booking_date) {
		this.booking_date = booking_date;
	}
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public int getSort_order() {
		return sort_order;
	}
	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getDisc_id() {
		return disc_id;
	}
	public void setDisc_id(String disc_id) {
		this.disc_id = disc_id;
	}
	public String getDisc_name() {
		return disc_name;
	}
	public void setDisc_name(String disc_name) {
		this.disc_name = disc_name;
	}
	public String getDisc_desc() {
		return disc_desc;
	}
	public void setDisc_desc(String disc_desc) {
		this.disc_desc = disc_desc;
	}
	public Double getDisc_hour() {
		return disc_hour;
	}
	public void setDisc_hour(Double disc_hour) {
		this.disc_hour = disc_hour;
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
