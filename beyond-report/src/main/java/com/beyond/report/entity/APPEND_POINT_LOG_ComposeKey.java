package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class APPEND_POINT_LOG_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date INVOICE_DATE;
	private String INVOICE_TIME;
	private String INVOICE_NO;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TS_APPEND_POINT_LOG_ComposeKey) {
			final APPEND_POINT_LOG_ComposeKey other = (APPEND_POINT_LOG_ComposeKey) obj;
			if (INVOICE_DATE == other.INVOICE_DATE && INVOICE_TIME == other.INVOICE_TIME && INVOICE_NO == other.INVOICE_NO)
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