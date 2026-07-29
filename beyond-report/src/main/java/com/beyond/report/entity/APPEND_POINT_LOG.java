package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "APPEND_POINT_LOG")
@IdClass(APPEND_POINT_LOG_ComposeKey.class)
public class APPEND_POINT_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Date INVOICE_DATE;
	@Id
	private String INVOICE_TIME;
	@Id
	private String INVOICE_NO;
	private String COUNTER_ID;
	private String COUNTER_NAME;
	private Double PROMOTE_AMT;
	private String USER_ID;
	private String USER_NAME;
	private String CARD_NO;
	private Double POINT_BASE;
	private Double POINT;
	private Date ACCESS_DATE;
	private String ACCESS_ID;
	
	@Transient
	private Integer rec_cnt;
	
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public Date getINVOICE_DATE() {
		return INVOICE_DATE;
	}
	public void setINVOICE_DATE(Date iNVOICE_DATE) {
		INVOICE_DATE = iNVOICE_DATE;
	}
	public String getINVOICE_TIME() {
		return INVOICE_TIME;
	}
	public void setINVOICE_TIME(String iNVOICE_TIME) {
		INVOICE_TIME = iNVOICE_TIME;
	}
	public String getINVOICE_NO() {
		return INVOICE_NO;
	}
	public void setINVOICE_NO(String iNVOICE_NO) {
		INVOICE_NO = iNVOICE_NO;
	}
	public String getCOUNTER_ID() {
		return COUNTER_ID;
	}
	public void setCOUNTER_ID(String cOUNTER_ID) {
		COUNTER_ID = cOUNTER_ID;
	}
	public String getCOUNTER_NAME() {
		return COUNTER_NAME;
	}
	public void setCOUNTER_NAME(String cOUNTER_NAME) {
		COUNTER_NAME = cOUNTER_NAME;
	}
	public Double getPROMOTE_AMT() {
		return PROMOTE_AMT;
	}
	public void setPROMOTE_AMT(Double pROMOTE_AMT) {
		PROMOTE_AMT = pROMOTE_AMT;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}
	public Double getPOINT_BASE() {
		return POINT_BASE;
	}
	public void setPOINT_BASE(Double pOINT_BASE) {
		POINT_BASE = pOINT_BASE;
	}
	public Double getPOINT() {
		return POINT;
	}
	public void setPOINT(Double pOINT) {
		POINT = pOINT;
	}
	public Date getACCESS_DATE() {
		return ACCESS_DATE;
	}
	public void setACCESS_DATE(Date aCCESS_DATE) {
		ACCESS_DATE = aCCESS_DATE;
	}
	public String getACCESS_ID() {
		return ACCESS_ID;
	}
	public void setACCESS_ID(String aCCESS_ID) {
		ACCESS_ID = aCCESS_ID;
	}
}


