package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "CARD_POINT_SET")
@IdClass(CARD_POINT_SET_ComposeKey.class)
public class CARD_POINT_SET implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String card_id;
	@Id
	private String sno;
	private String is_active;
	private String card_name;
	private String is_unlimited_date;
	private Date start_date;
	private Date end_date;
	private String w_1;
	private String w_2;
	private String w_3;
	private String w_4;
	private String w_5;
	private String w_6;
	private String w_7;
	private Double point_bet;
	private String is_unlimited_point;
	private Integer week_point_max;
	private String is_unlimited_price;
	private Integer reach_price;
	private Integer sort_order;
	private Date access_date;
	private String access_id;

	public Integer getSort_order() {
		return sort_order;
	}
	public void setSort_order(Integer sort_order) {
		this.sort_order = sort_order;
	}
	public String getIs_unlimited_price() {
		return is_unlimited_price;
	}
	public void setIs_unlimited_price(String is_unlimited_price) {
		this.is_unlimited_price = is_unlimited_price;
	}
	public Integer getReach_price() {
		return reach_price;
	}
	public void setReach_price(Integer reach_price) {
		this.reach_price = reach_price;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
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
	public String getIs_unlimited_date() {
		return is_unlimited_date;
	}
	public void setIs_unlimited_date(String is_unlimited_date) {
		this.is_unlimited_date = is_unlimited_date;
	}
	public String getIs_unlimited_point() {
		return is_unlimited_point;
	}
	public void setIs_unlimited_point(String is_unlimited_point) {
		this.is_unlimited_point = is_unlimited_point;
	}
	public Integer getWeek_point_max() {
		return week_point_max;
	}
	public void setWeek_point_max(Integer week_point_max) {
		this.week_point_max = week_point_max;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
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
	public Double getPoint_bet() {
		return point_bet;
	}
	public void setPoint_bet(Double point_bet) {
		this.point_bet = point_bet;
	}
}