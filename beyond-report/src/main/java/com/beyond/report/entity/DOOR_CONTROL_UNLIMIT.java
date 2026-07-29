package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DOOR_CONTROL_UNLIMIT")
public class DOOR_CONTROL_UNLIMIT {
	@Id
	private String USER_ID;
	private String USER_NAME;
	
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
}
