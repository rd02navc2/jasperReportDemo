package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "POS2_TR")
@Table(name = "TR")
@IdClass(TR_ComposeKey.class) // 使用與 TD 相同的複合主鍵結構
public class TR implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "SAL_DATE", length = 20) private String salDate;
	@Id @Column(name = "STORE_NO", length = 20) private String storeNo;
	@Id @Column(name = "POS_NO",   length = 20) private String posNo;
	@Id @Column(name = "TRN_NO",   length = 20) private String trnNo;
	@Id @Column(name = "ITEM_NO",  length = 20) private String itemNo;
	
    @Column(name = "QTY")       
    private Double qty;
    
    @Column(name = "SAL_PRICE") 
    private Double salPrice;
    
    @Column(name = "GRD_AMT")   
    private Double grdAmt;
}