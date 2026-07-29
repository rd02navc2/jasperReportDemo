package com.beyond.surrounding.bean;

public class Results {
	private Privilege_information privilege_information;
	private Privilege_transaction_information privilege_transaction_information;
	private String mmrm_tid;
	private Data data;

	public Results() {
	}

	public String getMmrm_tid() {
		return mmrm_tid;
	}

	public void setMmrm_tid(String mmrm_tid) {
		this.mmrm_tid = mmrm_tid;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Privilege_transaction_information getPrivilege_transaction_information() {
		return privilege_transaction_information;
	}

	public void setPrivilege_transaction_information(
			Privilege_transaction_information privilege_transaction_information) {
		this.privilege_transaction_information = privilege_transaction_information;
	}

	public Privilege_information getPrivilege_information() {
		return privilege_information;
	}

	public void setPrivilege_information(Privilege_information privilege_information) {
		this.privilege_information = privilege_information;
	}

}
