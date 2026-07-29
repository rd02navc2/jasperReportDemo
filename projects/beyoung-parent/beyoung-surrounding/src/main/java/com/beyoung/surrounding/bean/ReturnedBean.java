package com.beyoung.surrounding.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReturnedBean extends UploadBean{
	private String source_transaction_id;

	public String getSource_transaction_id() {
		return source_transaction_id;
	}

	public void setSource_transaction_id(String source_transaction_id) {
		this.source_transaction_id = source_transaction_id;
	}
}
