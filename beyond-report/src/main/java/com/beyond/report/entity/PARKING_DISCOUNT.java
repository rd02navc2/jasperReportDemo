package com.beyond.report.entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "park_dicount")
public class PARKING_DISCOUNT {
	
	@Id
	private Date park_discount_date1;
	private Double discount_hour1;
	private Double discount_hour2;
	private Double discount_hour3;
	private Double discount_hour4;
	private Double discount_hour5;
	private Double discount_hour6;
	private Double discount_hour7;
	private Double discount_hour8;
	private Double discount_hour9;
	private Double discount_hour10;

	public Date getPark_discount_date1() {
		return park_discount_date1;
	}

	public void setPark_discount_date1(Date park_discount_date1) {
		this.park_discount_date1 = park_discount_date1;
	}

	public Double getDiscount_hour1() {
		return discount_hour1;
	}

	public void setDiscount_hour1(Double discount_hour1) {
		this.discount_hour1 = discount_hour1;
	}

	public Double getDiscount_hour2() {
		return discount_hour2;
	}

	public void setDiscount_hour2(Double discount_hour2) {
		this.discount_hour2 = discount_hour2;
	}

	public Double getDiscount_hour3() {
		return discount_hour3;
	}

	public void setDiscount_hour3(Double discount_hour3) {
		this.discount_hour3 = discount_hour3;
	}

	public Double getDiscount_hour4() {
		return discount_hour4;
	}

	public void setDiscount_hour4(Double discount_hour4) {
		this.discount_hour4 = discount_hour4;
	}

	public Double getDiscount_hour5() {
		return discount_hour5;
	}

	public void setDiscount_hour5(Double discount_hour5) {
		this.discount_hour5 = discount_hour5;
	}

	public Double getDiscount_hour6() {
		return discount_hour6;
	}

	public void setDiscount_hour6(Double discount_hour6) {
		this.discount_hour6 = discount_hour6;
	}

	public Double getDiscount_hour7() {
		return discount_hour7;
	}

	public void setDiscount_hour7(Double discount_hour7) {
		this.discount_hour7 = discount_hour7;
	}

	public Double getDiscount_hour8() {
		return discount_hour8;
	}

	public void setDiscount_hour8(Double discount_hour8) {
		this.discount_hour8 = discount_hour8;
	}

	public Double getDiscount_hour9() {
		return discount_hour9;
	}

	public void setDiscount_hour9(Double discount_hour9) {
		this.discount_hour9 = discount_hour9;
	}

	public Double getDiscount_hour10() {
		return discount_hour10;
	}

	public void setDiscount_hour10(Double discount_hour10) {
		this.discount_hour10 = discount_hour10;
	}
}
