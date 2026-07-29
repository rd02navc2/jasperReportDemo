package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ATTENDANCEEMPRANK")
public class ATTENDANCEEMPRANK {
	
	@Id
	private String ATTENDANCEEMPLOYEERANKID;
	private String CODE;
	private String CNNAME;
	private String EMPLOYEESTATEID;
	private String DATE;
	private String WEEKDAY;
	private String SHORTNAME;
	private String WORKBEGINTIME;
	private String WORKENDTIME;
	
	public String getATTENDANCEEMPLOYEERANKID() {
		return ATTENDANCEEMPLOYEERANKID;
	}
	public void setATTENDANCEEMPLOYEERANKID(String aTTENDANCEEMPLOYEERANKID) {
		ATTENDANCEEMPLOYEERANKID = aTTENDANCEEMPLOYEERANKID;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getCNNAME() {
		return CNNAME;
	}
	public void setCNNAME(String cNNAME) {
		CNNAME = cNNAME;
	}
	public String getEMPLOYEESTATEID() {
		return EMPLOYEESTATEID;
	}
	public void setEMPLOYEESTATEID(String eMPLOYEESTATEID) {
		EMPLOYEESTATEID = eMPLOYEESTATEID;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public String getWEEKDAY() {
		return WEEKDAY;
	}
	public void setWEEKDAY(String wEEKDAY) {
		WEEKDAY = wEEKDAY;
	}
	public String getSHORTNAME() {
		return SHORTNAME;
	}
	public void setSHORTNAME(String sHORTNAME) {
		SHORTNAME = sHORTNAME;
	}
	public String getWORKBEGINTIME() {
		return WORKBEGINTIME;
	}
	public void setWORKBEGINTIME(String wORKBEGINTIME) {
		WORKBEGINTIME = wORKBEGINTIME;
	}
	public String getWORKENDTIME() {
		return WORKENDTIME;
	}
	public void setWORKENDTIME(String wORKENDTIME) {
		WORKENDTIME = wORKENDTIME;
	}
}
