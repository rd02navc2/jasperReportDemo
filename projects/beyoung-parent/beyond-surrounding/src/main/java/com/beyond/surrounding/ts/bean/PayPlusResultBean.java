package com.beyond.surrounding.ts.bean;

public class PayPlusResultBean {
	public String ResultUrl;
	public String ApiVer;
	public String ApposId;
	public String TransNo;
	public RequestParams RequestParams;
	public String TimeStamp;
	public String Random;
	public String CheckSum;
	public Integer Sequence;
	
	public String getApiVer() {
		return ApiVer;
	}
	public void setApiVer(String apiVer) {
		ApiVer = apiVer;
	}
	public String getApposId() {
		return ApposId;
	}
	public void setApposId(String apposId) {
		ApposId = apposId;
	}
	public String getTransNo() {
		return TransNo;
	}
	public void setTransNo(String transNo) {
		TransNo = transNo;
	}
	public String getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}
	public String getRandom() {
		return Random;
	}
	public void setRandom(String random) {
		Random = random;
	}
	public String getCheckSum() {
		return CheckSum;
	}
	public void setCheckSum(String checkSum) {
		CheckSum = checkSum;
	}
	public RequestParams getRequestParams() {
		return RequestParams;
	}
	public void setRequestParams(RequestParams requestParams) {
		RequestParams = requestParams;
	}
	public String getResultUrl() {
		return ResultUrl;
	}
	public void setResultUrl(String resultUrl) {
		ResultUrl = resultUrl;
	}
	public Integer getSequence() {
		return Sequence;
	}
	public void setSequence(Integer sequence) {
		Sequence = sequence;
	}
}
