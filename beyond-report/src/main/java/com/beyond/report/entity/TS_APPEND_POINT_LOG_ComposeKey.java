package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class TS_APPEND_POINT_LOG_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String TC_PSAPLANT;
	private String TC_PSA01;
	private String TC_PSA02;
	private String TC_PSA03;
	private Date TC_PSA04;

	public void setTC_PSA04(Date tC_PSA04) {
		TC_PSA04 = tC_PSA04;
	}

	public String getTC_PSAPLANT() {
		return TC_PSAPLANT;
	}

	public void setTC_PSAPLANT(String tC_PSAPLANT) {
		TC_PSAPLANT = tC_PSAPLANT;
	}

	public String getTC_PSA01() {
		return TC_PSA01;
	}

	public void setTC_PSA01(String tC_PSA01) {
		TC_PSA01 = tC_PSA01;
	}

	public String getTC_PSA02() {
		return TC_PSA02;
	}

	public void setTC_PSA02(String tC_PSA02) {
		TC_PSA02 = tC_PSA02;
	}

	public String getTC_PSA03() {
		return TC_PSA03;
	}

	public void setTC_PSA03(String tC_PSA03) {
		TC_PSA03 = tC_PSA03;
	}

	public Date getTC_PSA04() {
		return TC_PSA04;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TS_APPEND_POINT_LOG_ComposeKey) {
			final TS_APPEND_POINT_LOG_ComposeKey other = (TS_APPEND_POINT_LOG_ComposeKey) obj;
			if (TC_PSAPLANT == other.TC_PSAPLANT && TC_PSA01 == other.TC_PSA01 && TC_PSA02 == other.TC_PSA02 && TC_PSA03 == other.TC_PSA03 && TC_PSA04 == other.TC_PSA04)
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