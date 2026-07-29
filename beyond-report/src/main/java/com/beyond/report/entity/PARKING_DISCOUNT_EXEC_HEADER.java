package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import com.google.gson.JsonArray;

@Entity
@Table(name = "PARKING_DISCOUNT_EXEC_HEADER")
public class PARKING_DISCOUNT_EXEC_HEADER implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer p_no;
	private Date enter_date;
	private Date exit_date;
	private String user_id;
	private String center;
	private String card_id;
	private String car_no;
	private Double parking_hour;
	private Double parking_fee;
	private Double disc_fee;
	private Double pay_amt;
	private Double paid_amt;
	private Double tot_disc_hour;
	private Double real_disc_hour;
	private Double other_disc_fee;
	private Double other_disc_hour;
	private String is_used;
	private Date booking_date;
	private String booking_id;
	private Date access_date;
	private String access_id;
	
	@Transient
	private JsonArray jaDiscount;
	@Transient
	private String is_unlimited_date;
	@Transient
	private String user_name;
	
	public String getIs_unlimited_date() {
		return is_unlimited_date;
	}
	public void setIs_unlimited_date(String is_unlimited_date) {
		this.is_unlimited_date = is_unlimited_date;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Double getPaid_amt() {
		return paid_amt;
	}
	public void setPaid_amt(Double paid_amt) {
		this.paid_amt = paid_amt;
	}
	public JsonArray getJaDiscount() {
		return jaDiscount;
	}
	public void setJaDiscount(JsonArray jaDiscount) {
		this.jaDiscount = jaDiscount;
	}
	public Date getEnter_date() {
		return enter_date;
	}
	public void setEnter_date(Date enter_date) {
		this.enter_date = enter_date;
	}
	public Date getExit_date() {
		return exit_date;
	}
	public void setExit_date(Date exit_date) {
		this.exit_date = exit_date;
	}
	public Double getOther_disc_fee() {
		return other_disc_fee;
	}
	public void setOther_disc_fee(Double other_disc_fee) {
		this.other_disc_fee = other_disc_fee;
	}
	public Double getOther_disc_hour() {
		return other_disc_hour;
	}
	public void setOther_disc_hour(Double other_disc_hour) {
		this.other_disc_hour = other_disc_hour;
	}
	public Date getBooking_date() {
		return booking_date;
	}
	public void setBooking_date(Date booking_date) {
		this.booking_date = booking_date;
	}
	public String getBooking_id() {
		return booking_id;
	}
	public void setBooking_id(String booking_id) {
		this.booking_id = booking_id;
	}
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public Integer getP_no() {
		return p_no;
	}
	public void setP_no(Integer p_no) {
		this.p_no = p_no;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public Double getParking_hour() {
		return parking_hour;
	}
	public void setParking_hour(Double parking_hour) {
		this.parking_hour = parking_hour;
	}
	public Double getParking_fee() {
		return parking_fee;
	}
	public void setParking_fee(Double parking_fee) {
		this.parking_fee = parking_fee;
	}
	public Double getDisc_fee() {
		return disc_fee;
	}
	public void setDisc_fee(Double disc_fee) {
		this.disc_fee = disc_fee;
	}
	public Double getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(Double pay_amt) {
		this.pay_amt = pay_amt;
	}
	public Double getTot_disc_hour() {
		return tot_disc_hour;
	}
	public void setTot_disc_hour(Double tot_disc_hour) {
		this.tot_disc_hour = tot_disc_hour;
	}
	public Double getReal_disc_hour() {
		return real_disc_hour;
	}
	public void setReal_disc_hour(Double real_disc_hour) {
		this.real_disc_hour = real_disc_hour;
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