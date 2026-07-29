package com.beyoung.bonus.domain.bean;

import java.util.Date;

public class AppendInvoiceBean extends ResponseBean{
	private String Name;
	private String CounterID;
	private String CounterName;
	private String InvoiceSN;
	private Double Amount;
	private Double Point;
	private Double PointBase;
	private Date InvoiceDate;
	private String InvoiceTime;
	private Double TotalPoint;
	private Double PrePoint;
	private Double LastPoint;
	private String CreditCard;
	private Double InvAmt;
	private String PosId;
	private Double CreditCardAmt;

	public Double getCreditCardAmt() {
		return CreditCardAmt;
	}
	public void setCreditCardAmt(Double creditCardAmt) {
		CreditCardAmt = creditCardAmt;
	}
	public String getPosId() {
		return PosId;
	}
	public void setPosId(String posId) {
		PosId = posId;
	}
	public Double getInvAmt() {
		return InvAmt;
	}
	public void setInvAmt(Double invAmt) {
		InvAmt = invAmt;
	}
	public String getCreditCard() {
		return CreditCard;
	}
	public void setCreditCard(String creditCard) {
		CreditCard = creditCard;
	}
	public String getInvoiceSN() {
		return InvoiceSN;
	}
	public void setInvoiceSN(String invoiceSN) {
		InvoiceSN = invoiceSN;
	}
	public Double getTotalPoint() {
		return TotalPoint;
	}
	public void setTotalPoint(Double totalPoint) {
		TotalPoint = totalPoint;
	}
	public Double getPrePoint() {
		return PrePoint;
	}
	public void setPrePoint(Double prePoint) {
		PrePoint = prePoint;
	}
	public Double getLastPoint() {
		return LastPoint;
	}
	public void setLastPoint(Double lastPoint) {
		LastPoint = lastPoint;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Double getPointBase() {
		return PointBase;
	}
	public void setPointBase(Double pointBase) {
		PointBase = pointBase;
	}
	public String getInvoiceTime() {
		return InvoiceTime;
	}
	public void setInvoiceTime(String invoiceTime) {
		InvoiceTime = invoiceTime;
	}
	public Date getInvoiceDate() {
		return InvoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		InvoiceDate = invoiceDate;
	}
	public String getCounterID() {
		return CounterID;
	}
	public void setCounterID(String counterID) {
		CounterID = counterID;
	}
	public String getCounterName() {
		return CounterName;
	}
	public void setCounterName(String counterName) {
		CounterName = counterName;
	}
	public Double getAmount() {
		return Amount;
	}
	public void setAmount(Double amount) {
		Amount = amount;
	}
	public Double getPoint() {
		return Point;
	}
	public void setPoint(Double point) {
		Point = point;
	}
}
