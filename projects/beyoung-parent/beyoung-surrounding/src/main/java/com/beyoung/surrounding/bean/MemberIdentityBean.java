package com.beyoung.surrounding.bean;

public class MemberIdentityBean {
	
	private String type;
	private String identity;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	
	public interface Type{
		public String cardno="cardno";
		public String mobile="mobile";
		
	}
}
