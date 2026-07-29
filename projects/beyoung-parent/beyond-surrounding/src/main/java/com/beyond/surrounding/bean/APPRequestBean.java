package com.beyond.surrounding.bean;

public class APPRequestBean<T> {
	private T request_parameter;
	private String timestamp;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public T getRequest_parameter() {
		return request_parameter;
	}

	public void setRequest_parameter(T request_parameter) {
		this.request_parameter = request_parameter;
	}

}
