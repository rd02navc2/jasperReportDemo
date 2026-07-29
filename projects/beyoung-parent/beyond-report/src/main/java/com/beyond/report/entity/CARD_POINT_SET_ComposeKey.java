package com.beyond.report.entity;

import java.io.Serializable;

class CARD_POINT_SET_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String card_id;
	private String sno;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CARD_POINT_SET_ComposeKey) {
			final CARD_POINT_SET_ComposeKey other = (CARD_POINT_SET_ComposeKey) obj;
			if (card_id == other.card_id && sno == other.sno)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
