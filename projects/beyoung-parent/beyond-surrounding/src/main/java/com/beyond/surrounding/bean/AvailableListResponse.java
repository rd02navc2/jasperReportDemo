package com.beyond.surrounding.bean;

public class AvailableListResponse {
	private String code;
	private String message;
	private Rcrm rcrm;
	private AvailableListResults results;

	public AvailableListResponse() {
	}

	public AvailableListResponse(String code, String message, Rcrm rcrm, AvailableListResults results) {
		this.code = code;
		this.message = message;
		this.rcrm = rcrm;
		this.results = results;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Rcrm getRcrm() {
		return rcrm;
	}

	public void setRcrm(Rcrm rcrm) {
		this.rcrm = rcrm;
	}

	public AvailableListResults getResults() {
		return results;
	}

	public void setResults(AvailableListResults results) {
		this.results = results;
	}

}
