package com.beyoung.surrounding.pos2.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class TD_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String salDate;
	private String storeNo;
	private String posNo;
	private String trnNo;
}