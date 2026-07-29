package com.beyond.surrounding.bean;

public class TimePeriod {
	String begin_time;
	String end_time;

	public TimePeriod() {
	}

	public TimePeriod(String begin_time, String end_time) {
		this.begin_time = begin_time;
		this.end_time = end_time;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
