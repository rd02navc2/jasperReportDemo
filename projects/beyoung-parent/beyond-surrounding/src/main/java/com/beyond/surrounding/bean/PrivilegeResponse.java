package com.beyond.surrounding.bean;

public class PrivilegeResponse {

	private Rcrm rcrm;
	private Results results;

	public PrivilegeResponse() {
	}

	public PrivilegeResponse(Rcrm rcrm, Results results) {
		this.rcrm = rcrm;
		this.results = results;
	}

	public Rcrm getRcrm() {
		return rcrm;
	}

	public void setRcrm(Rcrm rcrm) {
		this.rcrm = rcrm;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

}
