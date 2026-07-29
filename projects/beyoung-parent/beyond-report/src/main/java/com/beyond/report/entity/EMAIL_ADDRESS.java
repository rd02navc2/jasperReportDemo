package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "email_address")
public class EMAIL_ADDRESS {
	
	@Id
	private String function;
	private String FROM;
	private String FROM_NAME;
	private String TO;
	private String CC;
	private String BCC;
	
	public String getFROM() {
		return FROM;
	}
	public void setFROM(String fROM) {
		FROM = fROM;
	}
	public String getFROM_NAME() {
		return FROM_NAME;
	}
	public void setFROM_NAME(String fROM_NAME) {
		FROM_NAME = fROM_NAME;
	}

	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getTO() {
		return TO;
	}
	public void setTO(String tO) {
		TO = tO;
	}
	public String getCC() {
		return CC;
	}
	public void setCC(String cC) {
		CC = cC;
	}
	public String getBCC() {
		return BCC;
	}
	public void setBCC(String bCC) {
		BCC = bCC;
	}
}

