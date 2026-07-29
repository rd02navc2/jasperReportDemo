package com.beyond.surrounding.pos.bean;

import com.beyond.surrounding.bean.ResponseBean;

public class LinePayPosBean extends ResponseBean{
	private String OrderID;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
}
