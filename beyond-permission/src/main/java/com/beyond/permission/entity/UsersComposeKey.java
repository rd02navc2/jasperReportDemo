package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

public class UsersComposeKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private String c_no;
	private String USERID;
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((USERID == null) ? 0 : USERID.hashCode());
		result = prime * result + ((c_no == null) ? 0 : c_no.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UsersComposeKey))
			return false;
		UsersComposeKey other = (UsersComposeKey) obj;
		if (USERID == null) {
			if (other.USERID != null)
				return false;
		} else if (!USERID.equals(other.USERID))
			return false;
		if (c_no == null) {
			if (other.c_no != null)
				return false;
		} else if (!c_no.equals(other.c_no))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UsersComposeKey [" + (USERID != null ? "USERID=" + USERID : "") + "]";
	}

}
