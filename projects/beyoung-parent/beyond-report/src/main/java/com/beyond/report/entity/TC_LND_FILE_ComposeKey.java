package com.beyond.report.entity;

import java.io.Serializable;

class TC_LND_FILE_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String TC_LND01;
	private Integer TC_LND02;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TC_LND_FILE_ComposeKey) {
			final TC_LND_FILE_ComposeKey other = (TC_LND_FILE_ComposeKey) obj;
			if (TC_LND01 == other.TC_LND01 && 
					TC_LND02 == other.TC_LND02)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public String getTC_LND01() {
		return TC_LND01;
	}

	public void setTC_LND01(String tC_LND01) {
		TC_LND01 = tC_LND01;
	}

	public Integer getTC_LND02() {
		return TC_LND02;
	}

	public void setTC_LND02(Integer tC_LND02) {
		TC_LND02 = tC_LND02;
	}
}

