package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class TC_PSB_FILE_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String TC_PSBPLANT;
	private String TC_PSB01;
	private String TC_PSB02;
	private String TC_PSB03;
	private Date TC_PSB04;
	private Integer TC_PSB06;

	public String getTC_PSBPLANT() {
		return TC_PSBPLANT;
	}

	public void setTC_PSBPLANT(String tC_PSBPLANT) {
		TC_PSBPLANT = tC_PSBPLANT;
	}

	public String getTC_PSB01() {
		return TC_PSB01;
	}

	public void setTC_PSB01(String tC_PSB01) {
		TC_PSB01 = tC_PSB01;
	}

	public String getTC_PSB02() {
		return TC_PSB02;
	}

	public void setTC_PSB02(String tC_PSB02) {
		TC_PSB02 = tC_PSB02;
	}

	public String getTC_PSB03() {
		return TC_PSB03;
	}

	public void setTC_PSB03(String tC_PSB03) {
		TC_PSB03 = tC_PSB03;
	}

	public Date getTC_PSB04() {
		return TC_PSB04;
	}

	public void setTC_PSB04(Date tC_PSB04) {
		TC_PSB04 = tC_PSB04;
	}

	public Integer getTC_PSB06() {
		return TC_PSB06;
	}

	public void setTC_PSB06(Integer tC_PSB06) {
		TC_PSB06 = tC_PSB06;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TC_PSB_FILE_ComposeKey) {
			final TC_PSB_FILE_ComposeKey other = (TC_PSB_FILE_ComposeKey) obj;
			if (TC_PSBPLANT == other.TC_PSBPLANT && TC_PSB01 == other.TC_PSB01 && TC_PSB02 == other.TC_PSB02 && TC_PSB03 == other.TC_PSB03 && TC_PSB04 == other.TC_PSB04 && TC_PSB06 == other.TC_PSB06)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
