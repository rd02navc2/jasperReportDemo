package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "POS2_TP")
@Table(name = "TP")
@IdClass(TP_ComposeKey.class)
public class TP implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "SAL_DATE", length = 20) private String salDate;
	@Id @Column(name = "STORE_NO", length = 20) private String storeNo;
	@Id @Column(name = "POS_NO",   length = 20) private String posNo;
	@Id @Column(name = "TRN_NO",   length = 20) private String trnNo;

    @Column(name = "MEMO3")              
    private String memo3;
    
    @Column(name = "PAY_AMT")            
    private Double payAmt;
    
    @Column(name = "INSTALLMENT_PERIOD") 
    private Double installmentPeriod;
}