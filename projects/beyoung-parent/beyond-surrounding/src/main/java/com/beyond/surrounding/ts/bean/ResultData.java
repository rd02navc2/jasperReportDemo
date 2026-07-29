package com.beyond.surrounding.ts.bean;

import com.beyond.surrounding.bean.ResponseBean;

public class ResultData extends ResponseBean{
	private String BankNo;
	private String CardToken;
	private String CardNumber;
	private String CardName;
	private String CardType;
	private String CardStatus;
	private String PaymentType;
	private String Barcode;
	private String ExpDate;
	private String MemberId;
	
	public String getBankNo() {
		return BankNo;
	}
	public void setBankNo(String bankNo) {
		BankNo = bankNo;
	}
	public String getCardToken() {
		return CardToken;
	}
	public void setCardToken(String cardToken) {
		CardToken = cardToken;
	}
	public String getCardNumber() {
		return CardNumber;
	}
	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}
	public String getCardName() {
		return CardName;
	}
	public void setCardName(String cardName) {
		CardName = cardName;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public String getCardStatus() {
		return CardStatus;
	}
	public void setCardStatus(String cardStatus) {
		CardStatus = cardStatus;
	}
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}
	public String getBarcode() {
		return Barcode;
	}
	public void setBarcode(String barcode) {
		Barcode = barcode;
	}
	public String getExpDate() {
		return ExpDate;
	}
	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

}
