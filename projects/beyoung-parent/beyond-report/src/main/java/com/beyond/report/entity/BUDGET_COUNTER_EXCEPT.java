package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BUDGET_COUNTER_EXCEPT")
public class BUDGET_COUNTER_EXCEPT {
	
	@Id
	private String counter_id;
	private String counter_name;
	public String getCounter_id() {
		return counter_id;
	}
	public void setCounter_id(String counter_id) {
		this.counter_id = counter_id;
	}
	public String getCounter_name() {
		return counter_name;
	}
	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}
}
