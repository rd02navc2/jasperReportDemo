package com.beyond.surrounding.erp.entity;

import java.io.Serializable;

public class TC_LRI_FILE_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String TC_LRI01;
	private String TC_LRI02;
	private Double TC_LRI03;
	private String TC_LRIPLANT;

	public String getTC_LRI01() {
		return TC_LRI01;
	}

	public void setTC_LRI01(String tC_LRI01) {
		TC_LRI01 = tC_LRI01;
	}

	public String getTC_LRI02() {
		return TC_LRI02;
	}

	public void setTC_LRI02(String tC_LRI02) {
		TC_LRI02 = tC_LRI02;
	}

	public Double getTC_LRI03() {
		return TC_LRI03;
	}

	public void setTC_LRI03(Double tC_LRI03) {
		TC_LRI03 = tC_LRI03;
	}

	public String getTC_LRIPLANT() {
		return TC_LRIPLANT;
	}

	public void setTC_LRIPLANT(String tC_LRIPLANT) {
		TC_LRIPLANT = tC_LRIPLANT;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TC_LRI_FILE_ComposeKey) {
			final TC_LRI_FILE_ComposeKey other = (TC_LRI_FILE_ComposeKey) obj;
			if (TC_LRI01 == other.TC_LRI01 && TC_LRI02 == other.TC_LRI02 && TC_LRI03 == other.TC_LRI03 && TC_LRIPLANT == other.TC_LRIPLANT)
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

