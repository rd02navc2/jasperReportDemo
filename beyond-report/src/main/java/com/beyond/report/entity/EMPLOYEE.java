package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEE")
public class EMPLOYEE {
	
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
	private String TITLE;	
	private String MAJOR;
	private String SCNAME;
	private String SCHOOL;
	private String DEPT_NAME;
	
	private String MANAGER_CODE;
	private String MANAGER_CNNAME;
	private String MANAGER_EMAIL;
	private String MANAGER_TITLE;
	
	private String ID;//dept_id
	private String ORGANIZATIONUNITNAME;//dept_name
	
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
	public String getMANAGER_CODE() {
		return MANAGER_CODE;
	}
	public void setMANAGER_CODE(String mANAGER_CODE) {
		MANAGER_CODE = mANAGER_CODE;
	}
	public String getMANAGER_CNNAME() {
		return MANAGER_CNNAME;
	}
	public void setMANAGER_CNNAME(String mANAGER_CNNAME) {
		MANAGER_CNNAME = mANAGER_CNNAME;
	}
	public String getMANAGER_EMAIL() {
		return MANAGER_EMAIL;
	}
	public void setMANAGER_EMAIL(String mANAGER_EMAIL) {
		MANAGER_EMAIL = mANAGER_EMAIL;
	}
	public String getMANAGER_TITLE() {
		return MANAGER_TITLE;
	}
	public void setMANAGER_TITLE(String mANAGER_TITLE) {
		MANAGER_TITLE = mANAGER_TITLE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getORGANIZATIONUNITNAME() {
		return ORGANIZATIONUNITNAME;
	}
	public void setORGANIZATIONUNITNAME(String oRGANIZATIONUNITNAME) {
		ORGANIZATIONUNITNAME = oRGANIZATIONUNITNAME;
	}

}

