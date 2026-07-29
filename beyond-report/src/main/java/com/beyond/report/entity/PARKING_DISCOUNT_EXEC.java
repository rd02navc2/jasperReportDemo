package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "PARKING_DISCOUNT_EXEC")
@IdClass(PARKING_DISCOUNT_EXEC_ComposeKey.class)
public class PARKING_DISCOUNT_EXEC implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Integer p_no;
	@Id
	private String user_id;
	@Id
	private String disc_id;
	private String card_id;
	private String car_no;
	private String disc_name;
	private Double disc_hour;
	private String center;
	private Date booking_date;
	private String booking_id;
	private Date access_date;
	private String access_id;
	private String is_used;
	
	@Transient
	private String modify;
	@Transient
	private Date enter_date;
	@Transient
	private Date exit_date;
	@Transient
	private Double used_hour;
	@Transient
	private String set_date;
	@Transient
	private Integer rec_cnt;
	
	@Transient
	private Double sale_hour;
	@Transient
	private Double hour_direct;
	@Transient
	private Double birthday_coupon;
	@Transient
	private Double cathay_card;
	@Transient
	private Double centuryasia;
	@Transient
	private Double member_card;
	@Transient
	private Double ts_common;
	@Transient
	private Double ts_holiday;
	@Transient
	private Double vip;
	@Transient
	private Double vip_common;
	@Transient
	private Double vip_holiday;
	@Transient
	private Double black_card;
	@Transient
	private java.math.BigInteger car_cnt;

	public String getModify() {
		return modify;
	}
	public void setModify(String modify) {
		this.modify = modify;
	}
	public Double getUsed_hour() {
		return used_hour;
	}
	public void setUsed_hour(Double used_hour) {
		this.used_hour = used_hour;
	}
	public String getSet_date() {
		return set_date;
	}
	public void setSet_date(String set_date) {
		this.set_date = set_date;
	}
	public Double getSale_hour() {
		return sale_hour;
	}
	public void setSale_hour(Double sale_hour) {
		this.sale_hour = sale_hour;
	}
	public Double getHour_direct() {
		return hour_direct;
	}
	public void setHour_direct(Double hour_direct) {
		this.hour_direct = hour_direct;
	}
	public Double getBirthday_coupon() {
		return birthday_coupon;
	}
	public void setBirthday_coupon(Double birthday_coupon) {
		this.birthday_coupon = birthday_coupon;
	}
	public Double getCathay_card() {
		return cathay_card;
	}
	public void setCathay_card(Double cathay_card) {
		this.cathay_card = cathay_card;
	}
	public Double getMember_card() {
		return member_card;
	}
	public void setMember_card(Double member_card) {
		this.member_card = member_card;
	}
	public Double getTs_common() {
		return ts_common;
	}
	public void setTs_common(Double ts_common) {
		this.ts_common = ts_common;
	}
	public Double getTs_holiday() {
		return ts_holiday;
	}
	public void setTs_holiday(Double ts_holiday) {
		this.ts_holiday = ts_holiday;
	}
	public Double getVip() {
		return vip;
	}
	public void setVip(Double vip) {
		this.vip = vip;
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
	public String getBooking_id() {
		return booking_id;
	}
	public void setBooking_id(String booking_id) {
		this.booking_id = booking_id;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public Double getDisc_hour() {
		return disc_hour;
	}
	public void setDisc_hour(Double disc_hour) {
		this.disc_hour = disc_hour;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
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
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public java.math.BigInteger getCar_cnt() {
		return car_cnt;
	}
	public void setCar_cnt(java.math.BigInteger car_cnt) {
		this.car_cnt = car_cnt;
	}
	public Double getCenturyasia() {
		return centuryasia;
	}
	public void setCenturyasia(Double centuryasia) {
		this.centuryasia = centuryasia;
	}
	public Double getBlack_card() {
		return black_card;
	}
	public void setBlack_card(Double black_card) {
		this.black_card = black_card;
	}
	public Double getVip_common() {
		return vip_common;
	}
	public void setVip_common(Double vip_common) {
		this.vip_common = vip_common;
	}
	public Double getVip_holiday() {
		return vip_holiday;
	}
	public void setVip_holiday(Double vip_holiday) {
		this.vip_holiday = vip_holiday;
	}
}