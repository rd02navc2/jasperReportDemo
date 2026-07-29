package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "SPOS_LPK_FILE")
@Table(name = "LPK_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LPK_FILE implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "LPK01")
    private String LPK01;
    
    @Column(name = "LPK02") private String LPK02;
    @Column(name = "LPK03") private String LPK03;
    @Column(name = "LPK04") private String LPK04;
    @Column(name = "LPK05") private String LPK05;
    @Column(name = "LPK06") private String LPK06;
    @Column(name = "LPK07") private String LPK07;
    @Column(name = "LPK08") private String LPK08;
    @Column(name = "LPK09") private String LPK09;
    @Column(name = "LPK10") private String LPK10;
    @Column(name = "LPK11") private String LPK11;
    @Column(name = "LPK12") private String LPK12;
    @Column(name = "LPK13") private String LPK13;
    @Column(name = "LPK14") private String LPK14;
    @Column(name = "LPK15") private String LPK15;
    @Column(name = "LPK16") private String LPK16;
    @Column(name = "LPK17") private String LPK17;
    @Column(name = "LPK18") private String LPK18;
    @Column(name = "LPK19") private String LPK19;
    @Column(name = "LPK20") private String LPK20;
    @Column(name = "LPK21") private String LPK21;
    
    @Column(name = "LPKACTI") private String LPKACTI;
    @Column(name = "LPKCRAT") private Date LPKCRAT;
    @Column(name = "LPKDate") private Date LPKDate;
    @Column(name = "LPKGRUP") private String LPKGRUP;
    @Column(name = "LPKMODU") private String LPKMODU;
    @Column(name = "LPKUSER") private String LPKUSER;
    @Column(name = "LPKORIU") private String LPKORIU;
    @Column(name = "LPKORIG") private String LPKORIG;
    @Column(name = "LPKPOS")  private String LPKPOS;
    
    @Column(name = "LPK051") private Integer LPK051;
    @Column(name = "LPK052") private Integer LPK052;
    @Column(name = "LPK053") private Integer LPK053;
    
    // --- 使用者自訂擴充欄位 (User Defined) ---
    @Column(name = "LPKUD01") private String LPKUD01;
    @Column(name = "LPKUD02") private String LPKUD02;
    @Column(name = "LPKUD03") private String LPKUD03;
    @Column(name = "LPKUD04") private String LPKUD04;
    @Column(name = "LPKUD05") private String LPKUD05;
    @Column(name = "LPKUD06") private String LPKUD06;
    @Column(name = "LPKUD07") private Double LPKUD07;
    @Column(name = "LPKUD08") private Double LPKUD08;
    @Column(name = "LPKUD09") private Double LPKUD09;
    @Column(name = "LPKUD10") private Integer LPKUD10;
    @Column(name = "LPKUD11") private Integer LPKUD11;
    @Column(name = "LPKUD12") private Integer LPKUD12;
    @Column(name = "LPKUD13") private Date LPKUD13;
    @Column(name = "LPKUD14") private Date LPKUD14;
    @Column(name = "LPKUD15") private Date LPKUD15;
    
    // --- 鼎新特定客製擴充欄位 (TopAction) ---
    @Column(name = "TA_LPK01") private Integer TA_LPK01;
    @Column(name = "TA_LPK02") private String TA_LPK02;
    @Column(name = "TA_LPK03") private String TA_LPK03;
    @Column(name = "TA_LPK04") private Date TA_LPK04;
    @Column(name = "TA_LPK05") private String TA_LPK05;
    @Column(name = "TA_LPK06") private String TA_LPK06;
    @Column(name = "TA_LPK07") private String TA_LPK07;
    @Column(name = "TA_LPK08") private String TA_LPK08;

    // --- 外部關聯或手動對接對應的欄位 ---
    @Column(name = "LPJ03") private String LPJ03;
    @Column(name = "LPJ12") private Double LPJ12;
    @Column(name = "LPJ15") private Double LPJ15;
    
    // ---  Transient 虛擬欄位（非資料表實體欄位，僅供檢視、傳輸或計算邏輯攜帶使用） ---
    @Transient private String vip_level;
    @Transient private Double lsm08_1;
    @Transient private Double lsm08_2;
}