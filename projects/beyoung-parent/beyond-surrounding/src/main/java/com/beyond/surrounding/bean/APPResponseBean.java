package com.beyond.surrounding.bean;

public class APPResponseBean<T> {
	private Rcrm rcrm;
	private T results;
	private String next;
	
	public Rcrm getRcrm() {
		return rcrm;
	}

	public void setRcrm(Rcrm rcrm) {
		this.rcrm = rcrm;
	}

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}
	
	
	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}




}
