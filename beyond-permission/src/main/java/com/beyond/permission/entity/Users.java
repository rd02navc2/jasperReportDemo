package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@IdClass(value = UsersComposeKey.class)
public class Users {
	@Id
	private String c_no;
	@Id
	private String USERID;
	private String USERNAME;
	private String PASSWORD;
	private String ChannelId;
	private String DivisionId;
	private String DivisionName;
	private String DEPID;
	private String DeptName;
	private String lv4_id;
	private String lv4_name;
	private String EMAIL;
	private String TEL;
	private String cell;
	private String address;
	private String ISLOCK;
	private String Pre_1_Password;
	private String Pre_2_Password;
	private String Pre_3_Password;
	private Integer wrong_times;
	private String remove_cookie;

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getChannelId() {
		return ChannelId;
	}

	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}

	public String getDivisionId() {
		return DivisionId;
	}

	public void setDivisionId(String divisionId) {
		DivisionId = divisionId;
	}

	public String getDivisionName() {
		return DivisionName;
	}

	public void setDivisionName(String divisionName) {
		DivisionName = divisionName;
	}

	public String getDEPID() {
		return DEPID;
	}

	public void setDEPID(String dEPID) {
		DEPID = dEPID;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getTEL() {
		return TEL;
	}

	public void setTEL(String tEL) {
		TEL = tEL;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getISLOCK() {
		return ISLOCK;
	}

	public void setISLOCK(String iSLOCK) {
		ISLOCK = iSLOCK;
	}

	public String getPre_1_Password() {
		return Pre_1_Password;
	}

	public void setPre_1_Password(String pre_1_Password) {
		Pre_1_Password = pre_1_Password;
	}

	public String getPre_2_Password() {
		return Pre_2_Password;
	}

	public void setPre_2_Password(String pre_2_Password) {
		Pre_2_Password = pre_2_Password;
	}

	public String getPre_3_Password() {
		return Pre_3_Password;
	}

	public void setPre_3_Password(String pre_3_Password) {
		Pre_3_Password = pre_3_Password;
	}

	public Integer getWrong_times() {
		return wrong_times;
	}

	public void setWrong_times(Integer wrong_times) {
		this.wrong_times = wrong_times;
	}

	public String getRemove_cookie() {
		return remove_cookie;
	}

	public void setRemove_cookie(String remove_cookie) {
		this.remove_cookie = remove_cookie;
	}

	public String getLv4_name() {
		return lv4_name;
	}

	public void setLv4_name(String lv4_name) {
		this.lv4_name = lv4_name;
	}

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
	public String toString() {
		return "Users [" + (USERID != null ? "USERID=" + USERID + ", " : "")
				+ (USERNAME != null ? "USERNAME=" + USERNAME + ", " : "")
				+ (ChannelId != null ? "ChannelId=" + ChannelId + ", " : "")
				+ (DivisionId != null ? "DivisionId=" + DivisionId + ", " : "")
				+ (DEPID != null ? "DEPID=" + DEPID + ", " : "") + (lv4_id != null ? "lv4_id=" + lv4_id : "") + "]";
	}

}
