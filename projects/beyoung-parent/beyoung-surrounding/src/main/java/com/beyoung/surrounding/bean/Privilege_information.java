package com.beyoung.surrounding.bean;

import java.util.List;

public class Privilege_information {
	private String title;
	private String type;
	private String promo_code;
	private Integer discount_amount;// 折抵金額
	private Float discount_ratio;// 折抵比例
	private ProductInfo product_voucher_info;
	private ProductInfo product_discount_info;
	private ProductInfo product_specified_price_info;
	private ProductInfo product_free_of_charge_info;
	private Integer redeemable_times;// 可核銷次數
	private String restricted_min_amount;// 最低消費金額使用門檻，若無限制則回傳 null
	private Integer monthly_redeem_restricted;// 每月使用次數上限，若無限制則回傳 null
	private Integer daily_redeem_restricted;// 每日使用次數上限，若無限制則回傳 null
	private List<TimePeriod> daily_redeem_available_time_period;// 可使用時段，若無限制則不回傳
	private String redeem_code;// 核銷條碼，此欄位非必傳，專案設定為回傳才會有此欄位

	public Privilege_information() {
	}

	public String getPromo_code() {
		return promo_code;
	}

	public void setPromo_code(String promo_code) {
		this.promo_code = promo_code;
	}

	public ProductInfo getProduct_voucher_info() {
		return product_voucher_info;
	}

	public void setProduct_voucher_info(ProductInfo product_voucher_info) {
		this.product_voucher_info = product_voucher_info;
	}

	public ProductInfo getProduct_discount_info() {
		return product_discount_info;
	}

	public void setProduct_discount_info(ProductInfo product_discount_info) {
		this.product_discount_info = product_discount_info;
	}

	public ProductInfo getProduct_specified_price_info() {
		return product_specified_price_info;
	}

	public void setProduct_specified_price_info(ProductInfo product_specified_price_info) {
		this.product_specified_price_info = product_specified_price_info;
	}

	public ProductInfo getProduct_free_of_charge_info() {
		return product_free_of_charge_info;
	}

	public void setProduct_free_of_charge_info(ProductInfo product_free_of_charge_info) {
		this.product_free_of_charge_info = product_free_of_charge_info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(Integer discount_amount) {
		this.discount_amount = discount_amount;
	}

	public Float getDiscount_ratio() {
		return discount_ratio;
	}

	public void setDiscount_ratio(Float discount_ratio) {
		this.discount_ratio = discount_ratio;
	}

	public Integer getRedeemable_times() {
		return redeemable_times;
	}

	public void setRedeemable_times(Integer redeemable_times) {
		this.redeemable_times = redeemable_times;
	}

	public String getRestricted_min_amount() {
		return restricted_min_amount;
	}

	public void setRestricted_min_amount(String restricted_min_amount) {
		this.restricted_min_amount = restricted_min_amount;
	}

	public Integer getMonthly_redeem_restricted() {
		return monthly_redeem_restricted;
	}

	public void setMonthly_redeem_restricted(Integer monthly_redeem_restricted) {
		this.monthly_redeem_restricted = monthly_redeem_restricted;
	}

	public Integer getDaily_redeem_restricted() {
		return daily_redeem_restricted;
	}

	public void setDaily_redeem_restricted(Integer daily_redeem_restricted) {
		this.daily_redeem_restricted = daily_redeem_restricted;
	}

	public List<TimePeriod> getDaily_redeem_available_time_period() {
		return daily_redeem_available_time_period;
	}

	public void setDaily_redeem_available_time_period(List<TimePeriod> daily_redeem_available_time_period) {
		this.daily_redeem_available_time_period = daily_redeem_available_time_period;
	}

	public String getRedeem_code() {
		return redeem_code;
	}

	public void setRedeem_code(String redeem_code) {
		this.redeem_code = redeem_code;
	}

}
