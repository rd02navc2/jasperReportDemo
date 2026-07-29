package com.beyond.report.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PARKING_POS_LOG")
public class PARKING_POS_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer sn;
	private Date invoice_date;
	private String invoice_no;
	private String random_no;
	private String invoice_time;
	private Integer p_no;
	private String center;
	private String channel;
	private Integer tranx_type;
	private String counter_id;
	private String counter_name;
	private String user_id;
	private String user_name;
	private String card_no;
	private String car_no;
	private Double promote_amt;
	private Date access_date;
	private String access_id;
	
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public Integer getTranx_type() {
		return tranx_type;
	}
	public void setTranx_type(Integer tranx_type) {
		this.tranx_type = tranx_type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public String getRandom_no() {
		return random_no;
	}
	public void setRandom_no(String random_no) {
		this.random_no = random_no;
	}
	public String getInvoice_time() {
		return invoice_time;
	}
	public void setInvoice_time(String invoice_time) {
		this.invoice_time = invoice_time;
	}
	public Integer getP_no() {
		return p_no;
	}
	public void setP_no(Integer p_no) {
		this.p_no = p_no;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getCounter_id() {
		return counter_id;
	}
	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
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
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public Double getPromote_amt() {
		return promote_amt;
	}
	public void setPromote_amt(Double promote_amt) {
		this.promote_amt = promote_amt;
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

