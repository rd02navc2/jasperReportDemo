package com.beyoung.member.domain.bean;

import java.util.Set;

public class ResponseBean {
	protected String Code;
	protected String YN;
	protected String Message;
	protected String User_id;
	protected String User_name;
	protected String Mobile;
	protected String Card_id;
	protected String Identity;
	protected String upload_message;
	protected String download_message;
	protected String invo_carrier;
	protected String sHashKey;
	protected Double base_bet;
	protected Double total_point;
	protected Double pre_point;
	protected Double this_point;

	protected String car;
	protected String motor;

	protected Integer iAmt;

	protected String lpj03;
	protected Double ta_lpj01;
	protected Double ta_lpj02;
	protected Double ta_lpj03;
	protected Double Ins_Integral;
	
	protected String card_vip;
	protected String card_type;
	
	//dc-
	private Object data;

	public String getInvo_carrier() {
		return invo_carrier;
	}

	public void setInvo_carrier(String invo_carrier) {
		this.invo_carrier = invo_carrier;
	}
	public String getDownload_message() {
		return download_message;
	}

	public void setDownload_message(String download_message) {
		this.download_message = download_message;
	}

	public String getUpload_message() {
		return upload_message;
	}

	public void setUpload_message(String upload_message) {
		this.upload_message = upload_message;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getUser_name() {
		return User_name;
	}

	public void setUser_name(String user_name) {
		User_name = user_name;
	}

	public ResponseBean(){
	}

	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getYN() {
		return YN;
	}
	public void setYN(String yN) {
		YN = yN;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}

	public String getsHashKey() {
		return sHashKey;
	}

	public void setsHashKey(String sHashKey) {
		this.sHashKey = sHashKey;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public Integer getiAmt() {
		return iAmt;
	}

	public void setiAmt(Integer iAmt) {
		this.iAmt = iAmt;
	}

	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getCard_id() {
		return Card_id;
	}

	public void setCard_id(String card_id) {
		Card_id = card_id;
	}

	public String getIdentity() {
		return Identity;
	}

	public void setIdentity(String identity) {
		Identity = identity;
	}

	public Double getTotal_point() {
		return total_point;
	}

	public void setTotal_point(Double total_point) {
		this.total_point = total_point;
	}

	public Double getPre_point() {
		return pre_point;
	}

	public void setPre_point(Double pre_point) {
		this.pre_point = pre_point;
	}

	public Double getThis_point() {
		return this_point;
	}

	public void setThis_point(Double this_point) {
		this.this_point = this_point;
	}

	public String getLpj03() {
		return lpj03;
	}

	public void setLpj03(String lpj03) {
		this.lpj03 = lpj03;
	}

	public Double getTa_lpj01() {
		return ta_lpj01;
	}

	public void setTa_lpj01(Double ta_lpj01) {
		this.ta_lpj01 = ta_lpj01;
	}

	public Double getTa_lpj02() {
		return ta_lpj02;
	}

	public void setTa_lpj02(Double ta_lpj02) {
		this.ta_lpj02 = ta_lpj02;
	}

	public Double getTa_lpj03() {
		return ta_lpj03;
	}

	public void setTa_lpj03(Double ta_lpj03) {
		this.ta_lpj03 = ta_lpj03;
	}

	public Double getIns_Integral() {
		return Ins_Integral;
	}

	public void setIns_Integral(Double ins_Integral) {
		Ins_Integral = ins_Integral;
	}

	public Double getBase_bet() {
		return base_bet;
	}

	public void setBase_bet(Double base_bet) {
		this.base_bet = base_bet;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getCard_vip() {
		return card_vip;
	}

	public void setCard_vip(String card_vip) {
		this.card_vip = card_vip;
	}

	//dc-
	public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
