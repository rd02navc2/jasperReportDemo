package com.beyoung.surrounding.pos.bean;

import java.util.List;
import com.beyoung.surrounding.pos.entity.TD;
import com.beyoung.surrounding.pos.entity.TP;
import com.beyoung.surrounding.pos.entity.TR;

public class PosDetailBean {

	private TD Trancation = null;
	private List<TR> Production = null;
	private TP CreditCard = null;
	
	public TD getTrancation() {
		return Trancation;
	}
	public void setTrancation(TD trancation) {
		Trancation = trancation;
	}
	public List<TR> getProduction() {
		return Production;
	}
	public void setProduction(List<TR> production) {
		Production = production;
	}
	public TP getCreditCard() {
		return CreditCard;
	}
	public void setCreditCard(TP creditCard) {
		CreditCard = creditCard;
	}
}
