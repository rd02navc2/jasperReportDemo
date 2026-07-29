package com.beyond.surrounding.erp.entity;

import java.io.Serializable;

public class TC_LRJ_FILE_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String TC_LRJ01;
	private String TC_LRJ02;
	private String TC_LRJ09;
	private String TC_LRJPLANT;

	public String getTC_LRJ01() {
		return TC_LRJ01;
	}

	public void setTC_LRJ01(String tC_LRJ01) {
		TC_LRJ01 = tC_LRJ01;
	}

	public String getTC_LRJ02() {
		return TC_LRJ02;
	}

	public void setTC_LRJI02(String tC_LRJI02) {
		TC_LRJ02 = tC_LRJI02;
	}

	public String getTC_LRJ09() {
		return TC_LRJ09;
	}

	public void setTC_LRJ09(String tC_LRJ09) {
		TC_LRJ09 = tC_LRJ09;
	}

	public String getTC_LRJPLANT() {
		return TC_LRJPLANT;
	}

	public void setTC_LRJPLANT(String tC_LRJPLANT) {
		TC_LRJPLANT = tC_LRJPLANT;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TC_LRJ_FILE_ComposeKey) {
			final TC_LRJ_FILE_ComposeKey other = (TC_LRJ_FILE_ComposeKey) obj;
			if (TC_LRJ01 == other.TC_LRJ01 && TC_LRJ02 == other.TC_LRJ02 && TC_LRJ09 == other.TC_LRJ09 && TC_LRJPLANT == other.TC_LRJPLANT)
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