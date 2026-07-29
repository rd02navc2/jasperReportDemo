package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE")
public class EMPLOYEE {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String EMPLOYEEID;
	private String CODE;
	private String CNNAME;
	private String JOBDATE;
	private String BIRTHDATE;
	private String EMPLOYEESTATEID;
	private String DEPARTMENTID;
	private String MOBILEPHONE;
	private String EMAIL;
	private String ADDRESS;
	
	private String MAJOR;
	private String SCNAME;
	
	@Transient
	private String SCHOOL;

	@Transient
	private String DEPT_NAME;

	@Transient
	private String TITLE;

	public String getJOBDATE() {
		return JOBDATE;
	}
	public void setJOBDATE(String jOBDATE) {
		JOBDATE = jOBDATE;
	}
	public String getBIRTHDATE() {
		return BIRTHDATE;
	}
	public void setBIRTHDATE(String bIRTHDATE) {
		BIRTHDATE = bIRTHDATE;
	}
	public String getMOBILEPHONE() {
		return MOBILEPHONE;
	}
	public void setMOBILEPHONE(String mOBILEPHONE) {
		MOBILEPHONE = mOBILEPHONE;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getMAJOR() {
		return MAJOR;
	}
	public void setMAJOR(String mAJOR) {
		MAJOR = mAJOR;
	}
	public String getSCNAME() {
		return SCNAME;
	}
	public void setSCNAME(String sCNAME) {
		SCNAME = sCNAME;
	}
	public String getSCHOOL() {
		return SCHOOL;
	}
	public void setSCHOOL(String sCHOOL) {
		SCHOOL = sCHOOL;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getEMPLOYEEID() {
		return EMPLOYEEID;
	}
	public void setEMPLOYEEID(String eMPLOYEEID) {
		EMPLOYEEID = eMPLOYEEID;
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
	public String getDEPARTMENTID() {
		return DEPARTMENTID;
	}
	public void setDEPARTMENTID(String dEPARTMENTID) {
		DEPARTMENTID = dEPARTMENTID;
	}
}
