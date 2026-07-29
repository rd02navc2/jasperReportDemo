package com.beyond.report.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APP_COUNTER")
public class APP_COUNTER {
	
	@Id
	private Date LPJ04;
	private Integer COUNTER_ALL;
	private Integer COUNTER_000;
	private Integer COUNTER_APP;
	private Integer COUNTER_BEYOND;
	private Integer COUNTER_NON_BEYOND;
	private Date ACCESS_DATE;
	
	@Transient
	private Integer rec_cnt;
	
	public Date getLPJ04() {
		return LPJ04;
	}
	public void setLPJ04(Date lPJ04) {
		LPJ04 = lPJ04;
	}
	public Integer getCOUNTER_ALL() {
		return COUNTER_ALL;
	}
	public void setCOUNTER_ALL(Integer cOUNTER_ALL) {
		COUNTER_ALL = cOUNTER_ALL;
	}
	public Integer getCOUNTER_000() {
		return COUNTER_000;
	}
	public void setCOUNTER_000(Integer cOUNTER_000) {
		COUNTER_000 = cOUNTER_000;
	}
	public Integer getCOUNTER_APP() {
		return COUNTER_APP;
	}
	public void setCOUNTER_APP(Integer cOUNTER_APP) {
		COUNTER_APP = cOUNTER_APP;
	}
	public Integer getCOUNTER_BEYOND() {
		return COUNTER_BEYOND;
	}
	public void setCOUNTER_BEYOND(Integer cOUNTER_BEYOND) {
		COUNTER_BEYOND = cOUNTER_BEYOND;
	}
	public Integer getCOUNTER_NON_BEYOND() {
		return COUNTER_NON_BEYOND;
	}
	public void setCOUNTER_NON_BEYOND(Integer cOUNTER_NON_BEYOND) {
		COUNTER_NON_BEYOND = cOUNTER_NON_BEYOND;
	}
	public Date getACCESS_DATE() {
		return ACCESS_DATE;
	}
	public void setACCESS_DATE(Date aCCESS_DATE) {
		ACCESS_DATE = aCCESS_DATE;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
}
