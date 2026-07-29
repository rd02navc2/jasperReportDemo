package com.beyoung.surrounding.bean;

public class TransactionCheckBean {
	private String type;
	private String id;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	public interface Type{
		public String mmrmTid="mmrm_tid";
		public String transactionId="transaction_id";
		
	}
	
	
}
