package com.beyond.surrounding.bean;

import java.util.List;

public class RegionBean {

	private int regionId;
	private String regionDescription;
	private List<DetailBean>lTest;

	public RegionBean() {
	}

	public List<DetailBean> getlTest() {
		return lTest;
	}

	public void setlTest(List<DetailBean> lTest) {
		this.lTest = lTest;
	}

	public RegionBean(int regionId, String regionDescription) {
		this.regionId = regionId;
		this.regionDescription = regionDescription;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionDescription() {
		return regionDescription;
	}

	public void setRegionDescription(String regionDescription) {
		this.regionDescription = regionDescription;
	}
}