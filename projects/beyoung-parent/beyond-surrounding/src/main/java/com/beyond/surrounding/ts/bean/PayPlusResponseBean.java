package com.beyond.surrounding.ts.bean;

import java.util.List;
import com.beyond.surrounding.bean.ResponseBean;

public class PayPlusResponseBean extends ResponseBean{
	private String sRetCode;
	private String sRetMsg;
	private String sOrderNO;
	private String sMemberId;
	private String sCardAuthUrl;
	private List<ResultData>lResultData;
	
	public String getsRetCode() {
		return sRetCode;
	}
	public void setsRetCode(String sRetCode) {
		this.sRetCode = sRetCode;
	}
	public String getsRetMsg() {
		return sRetMsg;
	}
	public void setsRetMsg(String sRetMsg) {
		this.sRetMsg = sRetMsg;
	}
	public String getsOrderNO() {
		return sOrderNO;
	}
	public void setsOrderNO(String sOrderNO) {
		this.sOrderNO = sOrderNO;
	}
	public String getsMemberId() {
		return sMemberId;
	}
	public void setsMemberId(String sMemberId) {
		this.sMemberId = sMemberId;
	}
	public String getsCardAuthUrl() {
		return sCardAuthUrl;
	}
	public void setsCardAuthUrl(String sCardAuthUrl) {
		this.sCardAuthUrl = sCardAuthUrl;
	}
	public List<ResultData> getlResultData() {
		return lResultData;
	}
	public void setlResultData(List<ResultData> lResultData) {
		this.lResultData = lResultData;
	}
}
