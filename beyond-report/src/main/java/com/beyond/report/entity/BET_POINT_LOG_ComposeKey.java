package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;

class BET_POINT_LOG_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tc_psaplant;
	private String tc_psa01;
	private String tc_psa02;
	private String tc_psa03;
	private Date tc_psa04;

	public String getTc_psaplant() {
		return tc_psaplant;
	}

	public void setTc_psaplant(String tc_psaplant) {
		this.tc_psaplant = tc_psaplant;
	}

	public String getTc_psa01() {
		return tc_psa01;
	}

	public void setTc_psa01(String tc_psa01) {
		this.tc_psa01 = tc_psa01;
	}

	public String getTc_psa02() {
		return tc_psa02;
	}

	public void setTc_psa02(String tc_psa02) {
		this.tc_psa02 = tc_psa02;
	}

	public String getTc_psa03() {
		return tc_psa03;
	}

	public void setTc_psa03(String tc_psa03) {
		this.tc_psa03 = tc_psa03;
	}

	public Date getTc_psa04() {
		return tc_psa04;
	}

	public void setTc_psa04(Date tc_psa04) {
		this.tc_psa04 = tc_psa04;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BET_POINT_LOG_ComposeKey) {
			final BET_POINT_LOG_ComposeKey other = (BET_POINT_LOG_ComposeKey) obj;
			if (tc_psaplant == other.tc_psaplant && tc_psa01 == other.tc_psa01 && tc_psa02 == other.tc_psa02 && tc_psa03 == other.tc_psa03 && tc_psa04 == other.tc_psa04)
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
