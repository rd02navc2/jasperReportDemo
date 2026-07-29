package com.beyond.surrounding.bean;

import java.util.List;

public class AvailableListResults {
	private List<Privilege_information> privilege_information;

	public AvailableListResults() {
	}

	public AvailableListResults(List<Privilege_information> privilege_information) {
		this.privilege_information = privilege_information;
	}

	public List<Privilege_information> getPrivilege_information() {
		return privilege_information;
	}

	public void setPrivilege_information(List<Privilege_information> privilege_information) {
		this.privilege_information = privilege_information;
	}

}
