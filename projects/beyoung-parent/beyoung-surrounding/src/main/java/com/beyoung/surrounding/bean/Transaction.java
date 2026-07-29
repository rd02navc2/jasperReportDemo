package com.beyoung.surrounding.bean;

public class Transaction {
	private String transaction_id;
	private String transaction_type;
	private Member_identity member_identity;
	private String brand_code;
	private String store_code;
	private String source_uuid;
	private String privilege_code;
	private Integer quantity;

	public Transaction() {
	}

	public Transaction(String transaction_id, String transaction_type, Member_identity member_identity,
			String brand_code, String store_code, String source_uuid, String privilege_code, Integer quantity) {
		this.transaction_id = transaction_id;
		this.transaction_type = transaction_type;
		this.member_identity = member_identity;
		this.brand_code = brand_code;
		this.store_code = store_code;
		this.source_uuid = source_uuid;
		this.privilege_code = privilege_code;
		this.quantity = quantity;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public Member_identity getMember_identity() {
		return member_identity;
	}

	public void setMember_identity(Member_identity member_identity) {
		this.member_identity = member_identity;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public String getSource_uuid() {
		return source_uuid;
	}

	public void setSource_uuid(String source_uuid) {
		this.source_uuid = source_uuid;
	}

	public String getPrivilege_code() {
		return privilege_code;
	}

	public void setPrivilege_code(String privilege_code) {
		this.privilege_code = privilege_code;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
