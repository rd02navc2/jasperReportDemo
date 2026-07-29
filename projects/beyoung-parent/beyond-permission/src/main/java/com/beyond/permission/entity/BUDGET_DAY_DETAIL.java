package com.beyond.permission.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BUDGET_DAY_DETAIL")
@IdClass(BUDGET_DAY_DETAIL_ComposeKey.class)
public class BUDGET_DAY_DETAIL implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String b_month;
	@Id
	private String floor;
	@Id
	private String dept_id;
	@Id
	private String counter_id;
	private Date access_date;
	private String access_id;
	private String dept_name;
	private String counter_name;
	private String org_name;
	private Integer b_01;
	private Integer b_02;
	private Integer b_03;
	private Integer b_04;
	private Integer b_05;
	private Integer b_06;
	private Integer b_07;
	private Integer b_08;
	private Integer b_09;
	private Integer b_10;
	private Integer b_11;
	private Integer b_12;
	private Integer b_13;
	private Integer b_14;
	private Integer b_15;
	private Integer b_16;
	private Integer b_17;
	private Integer b_18;
	private Integer b_19;
	private Integer b_20;
	private Integer b_21;
	private Integer b_22;
	private Integer b_23;
	private Integer b_24;
	private Integer b_25;
	private Integer b_26;
	private Integer b_27;
	private Integer b_28;
	private Integer b_29;
	private Integer b_30;
	private Integer b_31;
	
	@Transient
	private Integer rec_cnt;
	
	@Transient
	private BigDecimal day_budget;

	@Transient
	private BigDecimal month_budget;

	@Transient
	private BigDecimal month_accu_budget;
	
	@Transient
	private BigDecimal year_accu_budget;

	@Transient
	private BigDecimal budget;

	@Transient
	private BigDecimal day_accu_budget;
	
	public BigDecimal getDay_accu_budget() {
		return day_accu_budget;
	}
	public void setDay_accu_budget(BigDecimal day_accu_budget) {
		this.day_accu_budget = day_accu_budget;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public BigDecimal getBudget() {
		return budget;
	}
	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}
	public BigDecimal getMonth_budget() {
		return month_budget;
	}
	public void setMonth_budget(BigDecimal month_budget) {
		this.month_budget = month_budget;
	}
	public BigDecimal getDay_budget() {
		return day_budget;
	}
	public void setDay_budget(BigDecimal day_budget) {
		this.day_budget = day_budget;
	}
	public BigDecimal getMonth_accu_budget() {
		return month_accu_budget;
	}
	public void setMonth_accu_budget(BigDecimal month_accu_budget) {
		this.month_accu_budget = month_accu_budget;
	}
	public BigDecimal getYear_accu_budget() {
		return year_accu_budget;
	}
	public void setYear_accu_budget(BigDecimal year_accu_budget) {
		this.year_accu_budget = year_accu_budget;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getB_month() {
		return b_month;
	}
	public void setB_month(String b_month) {
		this.b_month = b_month;
	}
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public String getCounter_id() {
		return counter_id;
	}
	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
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
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}
	public Integer getB_01() {
		return b_01;
	}
	public void setB_01(Integer b_01) {
		this.b_01 = b_01;
	}
	public Integer getB_02() {
		return b_02;
	}
	public void setB_02(Integer b_02) {
		this.b_02 = b_02;
	}
	public Integer getB_03() {
		return b_03;
	}
	public void setB_03(Integer b_03) {
		this.b_03 = b_03;
	}
	public Integer getB_04() {
		return b_04;
	}
	public void setB_04(Integer b_04) {
		this.b_04 = b_04;
	}
	public Integer getB_05() {
		return b_05;
	}
	public void setB_05(Integer b_05) {
		this.b_05 = b_05;
	}
	public Integer getB_06() {
		return b_06;
	}
	public void setB_06(Integer b_06) {
		this.b_06 = b_06;
	}
	public Integer getB_07() {
		return b_07;
	}
	public void setB_07(Integer b_07) {
		this.b_07 = b_07;
	}
	public Integer getB_08() {
		return b_08;
	}
	public void setB_08(Integer b_08) {
		this.b_08 = b_08;
	}
	public Integer getB_09() {
		return b_09;
	}
	public void setB_09(Integer b_09) {
		this.b_09 = b_09;
	}
	public Integer getB_10() {
		return b_10;
	}
	public void setB_10(Integer b_10) {
		this.b_10 = b_10;
	}
	public Integer getB_11() {
		return b_11;
	}
	public void setB_11(Integer b_11) {
		this.b_11 = b_11;
	}
	public Integer getB_12() {
		return b_12;
	}
	public void setB_12(Integer b_12) {
		this.b_12 = b_12;
	}
	public Integer getB_13() {
		return b_13;
	}
	public void setB_13(Integer b_13) {
		this.b_13 = b_13;
	}
	public Integer getB_14() {
		return b_14;
	}
	public void setB_14(Integer b_14) {
		this.b_14 = b_14;
	}
	public Integer getB_15() {
		return b_15;
	}
	public void setB_15(Integer b_15) {
		this.b_15 = b_15;
	}
	public Integer getB_16() {
		return b_16;
	}
	public void setB_16(Integer b_16) {
		this.b_16 = b_16;
	}
	public Integer getB_17() {
		return b_17;
	}
	public void setB_17(Integer b_17) {
		this.b_17 = b_17;
	}
	public Integer getB_18() {
		return b_18;
	}
	public void setB_18(Integer b_18) {
		this.b_18 = b_18;
	}
	public Integer getB_19() {
		return b_19;
	}
	public void setB_19(Integer b_19) {
		this.b_19 = b_19;
	}
	public Integer getB_20() {
		return b_20;
	}
	public void setB_20(Integer b_20) {
		this.b_20 = b_20;
	}
	public Integer getB_21() {
		return b_21;
	}
	public void setB_21(Integer b_21) {
		this.b_21 = b_21;
	}
	public Integer getB_22() {
		return b_22;
	}
	public void setB_22(Integer b_22) {
		this.b_22 = b_22;
	}
	public Integer getB_23() {
		return b_23;
	}
	public void setB_23(Integer b_23) {
		this.b_23 = b_23;
	}
	public Integer getB_24() {
		return b_24;
	}
	public void setB_24(Integer b_24) {
		this.b_24 = b_24;
	}
	public Integer getB_25() {
		return b_25;
	}
	public void setB_25(Integer b_25) {
		this.b_25 = b_25;
	}
	public Integer getB_26() {
		return b_26;
	}
	public void setB_26(Integer b_26) {
		this.b_26 = b_26;
	}
	public Integer getB_27() {
		return b_27;
	}
	public void setB_27(Integer b_27) {
		this.b_27 = b_27;
	}
	public Integer getB_28() {
		return b_28;
	}
	public void setB_28(Integer b_28) {
		this.b_28 = b_28;
	}
	public Integer getB_29() {
		return b_29;
	}
	public void setB_29(Integer b_29) {
		this.b_29 = b_29;
	}
	public Integer getB_30() {
		return b_30;
	}
	public void setB_30(Integer b_30) {
		this.b_30 = b_30;
	}
	public Integer getB_31() {
		return b_31;
	}
	public void setB_31(Integer b_31) {
		this.b_31 = b_31;
	}
}

class BUDGET_DAY_DETAIL_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date b_month;
	private String floor;
	private String dept_id;
	private String counter_id;

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public Date getB_month() {
		return b_month;
	}

	public void setB_month(Date b_month) {
		this.b_month = b_month;
	}

	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getCounter_id() {
		return counter_id;
	}

	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BUDGET_DAY_DETAIL_ComposeKey) {
			final BUDGET_DAY_DETAIL_ComposeKey other = (BUDGET_DAY_DETAIL_ComposeKey) obj;
			if (b_month == other.b_month && dept_id == other.dept_id && counter_id == other.counter_id && floor == other.floor)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}