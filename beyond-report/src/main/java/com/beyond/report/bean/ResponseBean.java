package com.beyond.report.bean;

public class ResponseBean {
	protected String Code;
	protected String YN;
	protected String Message;
	
	public ResponseBean(){
	}

	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getYN() {
		return YN;
	}
	public void setYN(String yN) {
		YN = yN;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
