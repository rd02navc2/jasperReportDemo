package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

public class VW_KPIR_DMS {

	private String DEALER_ID;
	private String DEALER_NAME;
	private String DEPT_ID;
	private String DEPT_NAME;
	private String DIVISION_ID;
	private String DIVISION_NAME;
	private String CHANNEL_ID;
	private String CHANNEL_NAME;
	private String DEALER_SALES_ID;
	private String DEALER_SALES_NAME;
	private String DEALER_CLOSE_DATE;
	private String STATUS;
	private String CHANNEL_CODE;
	public String getDEALER_ID() {
		return DEALER_ID;
	}
	public void setDEALER_ID(String dEALER_ID) {
		DEALER_ID = dEALER_ID;
	}
	public String getDEALER_NAME() {
		return DEALER_NAME;
	}
	public void setDEALER_NAME(String dEALER_NAME) {
		DEALER_NAME = dEALER_NAME;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getDIVISION_ID() {
		return DIVISION_ID;
	}
	public void setDIVISION_ID(String dIVISION_ID) {
		DIVISION_ID = dIVISION_ID;
	}
	public String getDIVISION_NAME() {
		return DIVISION_NAME;
	}
	public void setDIVISION_NAME(String dIVISION_NAME) {
		DIVISION_NAME = dIVISION_NAME;
	}
	public String getCHANNEL_ID() {
		return CHANNEL_ID;
	}
	public void setCHANNEL_ID(String cHANNEL_ID) {
		CHANNEL_ID = cHANNEL_ID;
	}
	public String getCHANNEL_NAME() {
		return CHANNEL_NAME;
	}
	public void setCHANNEL_NAME(String cHANNEL_NAME) {
		CHANNEL_NAME = cHANNEL_NAME;
	}
	public String getDEALER_SALES_ID() {
		return DEALER_SALES_ID;
	}
	public void setDEALER_SALES_ID(String dEALER_SALES_ID) {
		DEALER_SALES_ID = dEALER_SALES_ID;
	}
	public String getDEALER_SALES_NAME() {
		return DEALER_SALES_NAME;
	}
	public void setDEALER_SALES_NAME(String dEALER_SALES_NAME) {
		DEALER_SALES_NAME = dEALER_SALES_NAME;
	}
	@Override
	public String toString() {
		return "VW_KPIR_DMS [" + (CHANNEL_ID != null ? "CHANNEL_ID=" + CHANNEL_ID + ", " : "")
				+ (DIVISION_ID != null ? "DIVISION_ID=" + DIVISION_ID + ", " : "")
				+ (DEPT_ID != null ? "DEPT_ID=" + DEPT_ID + ", " : "")
				+ (DEALER_ID != null ? "DEALER_ID=" + DEALER_ID + ", " : "")
				+ (DEALER_SALES_ID != null ? "DEALER_SALES_ID=" + DEALER_SALES_ID : "") + "]";
	}
	public String getDEALER_CLOSE_DATE() {
		return DEALER_CLOSE_DATE;
	}
	public void setDEALER_CLOSE_DATE(String dEALER_CLOSE_DATE) {
		DEALER_CLOSE_DATE = dEALER_CLOSE_DATE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getCHANNEL_CODE() {
		return CHANNEL_CODE;
	}
	public void setCHANNEL_CODE(String cHANNEL_CODE) {
		CHANNEL_CODE = cHANNEL_CODE;
	}

}
