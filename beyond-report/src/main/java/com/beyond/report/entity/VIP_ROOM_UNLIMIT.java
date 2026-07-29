package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VIP_ROOM_UNLIMIT")
public class VIP_ROOM_UNLIMIT {
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
