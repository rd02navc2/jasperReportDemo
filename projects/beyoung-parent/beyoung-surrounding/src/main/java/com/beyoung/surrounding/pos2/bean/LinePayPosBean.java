package com.beyoung.surrounding.pos2.bean;

import com.beyoung.surrounding.bean.ResponseBean;

public class LinePayPosBean extends ResponseBean{
	private String OrderID;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
}
