package com.beyoung.surrounding.bean;

public class Privilege_transaction_information {
	private String transaction_id;
	private String mmrm_tid;

	public Privilege_transaction_information() {
	}

	public Privilege_transaction_information(String transaction_id, String mmrm_tid) {
		this.transaction_id = transaction_id;
		this.mmrm_tid = mmrm_tid;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getMmrm_tid() {
		return mmrm_tid;
	}

	public void setMmrm_tid(String mmrm_tid) {
		this.mmrm_tid = mmrm_tid;
	}

}
