package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LOGDB")
public class LOGDB {
	
	@Id
	private Integer LogNum;
	private String LogTime;
	private String UserID;
	private String LogIP;
	private String CardNo;
	private Integer DoorNum;
	private Integer MachineNum;
	private Integer SeqNum;
	private Integer EventID;
	private Integer UserUsed;
	private Integer WorkingStatus;
	private Integer TryCount;
	private Integer FingerNum;
	private Integer StationNum;
	private Integer LocalLogNum;
	private Integer SysRecordNum;
	private Integer ExtractFlag;
	private Integer UpdateFlag;
	private String PhotoFileName;
	
	private String WSName;
	private String access_id;
	private String done;
	
	@Transient
	private String LogTime2;
	@Transient
	private Integer rec_cnt;
	
	public String getDone() {
		return done;
	}

	public void setDone(String done) {
		this.done = done;
	}

	public String getAccess_id() {
		return access_id;
	}

	public void setAccess_id(String access_id) {
		this.access_id = access_id;
	}

	public Integer getRec_cnt() {
		return rec_cnt;
	}

	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}

	public String getLogTime() {
		return LogTime;
	}

	public void setLogTime(String logTime) {
		LogTime = logTime;
	}

	public String getWSName() {
		return WSName;
	}

	public void setWSName(String wSName) {
		WSName = wSName;
	}

	public Integer getLogNum() {
		return LogNum;
	}

	public void setLogNum(Integer logNum) {
		LogNum = logNum;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getLogIP() {
		return LogIP;
	}

	public void setLogIP(String logIP) {
		LogIP = logIP;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public Integer getDoorNum() {
		return DoorNum;
	}

	public void setDoorNum(Integer doorNum) {
		DoorNum = doorNum;
	}

	public Integer getMachineNum() {
		return MachineNum;
	}

	public void setMachineNum(Integer machineNum) {
		MachineNum = machineNum;
	}

	public Integer getSeqNum() {
		return SeqNum;
	}

	public void setSeqNum(Integer seqNum) {
		SeqNum = seqNum;
	}

	public Integer getEventID() {
		return EventID;
	}

	public void setEventID(Integer eventID) {
		EventID = eventID;
	}

	public Integer getUserUsed() {
		return UserUsed;
	}

	public void setUserUsed(Integer userUsed) {
		UserUsed = userUsed;
	}

	public Integer getWorkingStatus() {
		return WorkingStatus;
	}

	public void setWorkingStatus(Integer workingStatus) {
		WorkingStatus = workingStatus;
	}

	public Integer getTryCount() {
		return TryCount;
	}

	public void setTryCount(Integer tryCount) {
		TryCount = tryCount;
	}

	public Integer getFingerNum() {
		return FingerNum;
	}

	public void setFingerNum(Integer fingerNum) {
		FingerNum = fingerNum;
	}

	public Integer getStationNum() {
		return StationNum;
	}

	public void setStationNum(Integer stationNum) {
		StationNum = stationNum;
	}

	public Integer getLocalLogNum() {
		return LocalLogNum;
	}

	public void setLocalLogNum(Integer localLogNum) {
		LocalLogNum = localLogNum;
	}

	public Integer getSysRecordNum() {
		return SysRecordNum;
	}

	public void setSysRecordNum(Integer sysRecordNum) {
		SysRecordNum = sysRecordNum;
	}

	public Integer getExtractFlag() {
		return ExtractFlag;
	}

	public void setExtractFlag(Integer extractFlag) {
		ExtractFlag = extractFlag;
	}

	public Integer getUpdateFlag() {
		return UpdateFlag;
	}

	public void setUpdateFlag(Integer updateFlag) {
		UpdateFlag = updateFlag;
	}

	public String getPhotoFileName() {
		return PhotoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		PhotoFileName = photoFileName;
	}

	public String getLogTime2() {
		return LogTime2;
	}

	public void setLogTime2(String logTime2) {
		LogTime2 = logTime2;
	}
}

