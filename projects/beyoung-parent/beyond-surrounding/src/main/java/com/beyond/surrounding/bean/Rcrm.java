package com.beyond.surrounding.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rcrm {
	private String RC;
	private String RM;
	private String RM_detail;

	public Rcrm() {
	}

	public String getRC() {
		return RC;
	}

	public void setRC(String rC) {
		RC = rC;
	}

	public String getRM() {
		return RM;
	}

	public void setRM(String rM) {
		RM = rM;
	}

	public String getRM_detail() {
		return RM_detail;
	}

	public void setRM_detail(String rM_detail) {
		RM_detail = rM_detail;
	}

}
