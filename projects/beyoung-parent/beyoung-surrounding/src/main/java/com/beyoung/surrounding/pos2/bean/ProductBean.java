package com.beyoung.surrounding.pos2.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductBean {

	@JsonProperty("Code")
	private String code;

	@JsonProperty("YN")
	private String yn;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("p_no")
	private String pNo;

	@JsonProperty("p_name")
	private String pName;

	private Double price1;
	private Double price2;

	@JsonProperty("counter_no")
	private String counterNo;

	@JsonProperty("type_no")
	private String typeNo;

	@JsonProperty("is_tax")
	private String isTax;

	@JsonProperty("access_date")
	private String accessDate;

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public String getYN() { return yn; }
	public void setYN(String yn) { this.yn = yn; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public String getP_no() { return pNo; }
	public void setP_no(String pNo) { this.pNo = pNo; }

	public String getP_name() { return pName; }
	public void setP_name(String pName) { this.pName = pName; }

	public Double getPrice1() { return price1; }
	public void setPrice1(Double price1) { this.price1 = price1; }

	public Double getPrice2() { return price2; }
	public void setPrice2(Double price2) { this.price2 = price2; }

	public String getCounter_no() { return counterNo; }
	public void setCounter_no(String counterNo) { this.counterNo = counterNo; }

	public String getType_no() { return typeNo; }
	public void setType_no(String typeNo) { this.typeNo = typeNo; }

	public String getIs_tax() { return isTax; }
	public void setIs_tax(String isTax) { this.isTax = isTax; }

	public String getAccess_date() { return accessDate; }
	public void setAccess_date(String accessDate) { this.accessDate = accessDate; }
}