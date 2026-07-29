package com.beyond.surrounding.pos2.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "POS2_TD")
@Table(name = "TD")
@IdClass(TD_ComposeKey.class)
public class TD implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 主鍵欄位 (Composite Key) ---
    @Id @Column(name = "SAL_DATE", length = 20) private String salDate;
    @Id @Column(name = "STORE_NO", length = 20) private String storeNo;
    @Id @Column(name = "POS_NO",   length = 20) private String posNo;
    @Id @Column(name = "TRN_NO",   length = 20) private String trnNo;
    
    // --- 主檔欄位 (PSA) ---
    @Column(name = "VIP_NO")      private String vipNo;
    @Column(name = "TOT_SALES")   private Double totSales;
    @Column(name = "INV_AMT")     private Double invAmt;
    @Column(name = "PROMOT_AMT")  private Double promotAmt;
    @Column(name = "SAL_TIME")    private String salTime;
    @Column(name = "TENT_NO")     private String tentNo;
    @Column(name = "SEQ_NO")      private String seqNo;
    @Column(name = "INV_NO")      private String invNo;
    @Column(name = "SAL_TYPE")    private String salType;

    // --- 交易明細欄位 (TR) ---
    @Column(name = "ITEM_NO")     private String itemNo;
    @Column(name = "QTY")         private Double qty;
    @Column(name = "SAL_PRICE")   private Double salPrice;
    @Column(name = "GRD_AMT")     private Double grdAmt;
    
    // --- 付款明細欄位 (TP) ---
    @Column(name = "MEMO3")       private String memo3;
    @Column(name = "PAY_AMT")     private Double payAmt;
    @Column(name = "INSTALLMENT_PERIOD") private Double installmentPeriod;
}