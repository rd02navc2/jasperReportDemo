package com.beyond.surrounding.erp.bean;

public class ResponseBean {
	protected String Code;
	protected String Message;
		
	public ResponseBean(){
	}

	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
