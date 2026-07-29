package com.beyond.report.entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "COUPON_LOG")
public class COUPON_LOG {
	@Id
	@GeneratedValue
	private Integer sn;
	private String coupon_no;
	private String coupon_status;
	private Date release_date;
	private Date sale_date;
	private Date used_date;
	private String used_counter_id;
	private String used_counter_name;
	private String used_posid;
	private Date ori_active_date;
	private Date ori_end_date;
	private Date new_active_date;
	private Date new_end_date;
	private String access_id;
	private Date access_date;
	
	@Transient
	private int rec_cnt;
	
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getCoupon_no() {
		return coupon_no;
	}
	public void setCoupon_no(String coupon_no) {
		this.coupon_no = coupon_no;
	}
	public String getCoupon_status() {
		return coupon_status;
	}
	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	public Date getRelease_date() {
		return release_date;
	}
	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}
	public Date getSale_date() {
		return sale_date;
	}
	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}
	public Date getUsed_date() {
		return used_date;
	}
	public void setUsed_date(Date used_date) {
		this.used_date = used_date;
	}
	public String getUsed_counter_id() {
		return used_counter_id;
	}
	public void setUsed_counter_id(String used_counter_id) {
		this.used_counter_id = used_counter_id;
	}
	public String getUsed_counter_name() {
		return used_counter_name;
	}
	public void setUsed_counter_name(String used_counter_name) {
		this.used_counter_name = used_counter_name;
	}
	public String getUsed_posid() {
		return used_posid;
	}
	public void setUsed_posid(String used_posid) {
		this.used_posid = used_posid;
	}
	public Date getOri_active_date() {
		return ori_active_date;
	}
	public void setOri_active_date(Date ori_active_date) {
		this.ori_active_date = ori_active_date;
	}
	public Date getOri_end_date() {
		return ori_end_date;
	}
	public void setOri_end_date(Date ori_end_date) {
		this.ori_end_date = ori_end_date;
	}
	public Date getNew_active_date() {
		return new_active_date;
	}
	public void setNew_active_date(Date new_active_date) {
		this.new_active_date = new_active_date;
	}
	public Date getNew_end_date() {
		return new_end_date;
	}
	public void setNew_end_date(Date new_end_date) {
		this.new_end_date = new_end_date;
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
	public int getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(int rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
}
