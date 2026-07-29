package com.beyoung.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LPK_FILE")
@DynamicInsert
@DynamicUpdate
public class LPK_FILE implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id //  舊系統單一主鍵
    @Column(name = "LPK01", length = 50)
    private String lpk01;

    @Column(name = "LPK02", length = 255)
    private String lpk02;

    @Column(name = "LPK03", length = 255)
    private String lpk03;

    @Column(name = "LPK04", length = 255)
    private String lpk04;

    @Column(name = "LPK05", length = 255)
    private String lpk05;

    @Column(name = "LPK06", length = 255)
    private String lpk06;

    @Column(name = "LPK07", length = 255)
    private String lpk07;

    @Column(name = "LPK08", length = 255)
    private String lpk08;

    @Column(name = "LPK09", length = 255)
    private String lpk09;

    @Column(name = "LPK10", length = 255)
    private String lpk10;

    @Column(name = "LPK11", length = 255)
    private String lpk11;

    @Column(name = "LPK12", length = 255)
    private String lpk12;

    @Column(name = "LPK13", length = 255)
    private String lpk13;

    @Column(name = "LPK14", length = 255)
    private String lpk14;

    @Column(name = "LPK15", length = 255)
    private String lpk15;

    @Column(name = "LPK16", length = 255)
    private String lpk16;

    @Column(name = "LPK17", length = 255)
    private String lpk17;

    @Column(name = "LPK18", length = 255)
    private String lpk18;

    @Column(name = "LPK19", length = 255)
    private String lpk19;

    @Column(name = "LPKACTI", length = 10)
    private String lpkacti;

    @Temporal(TemporalType.TIMESTAMP) //  建立時間含時分秒
    @Column(name = "LPKCRAT")
    private Date lpkcrat;

    @Temporal(TemporalType.DATE)      //  異動日期
    @Column(name = "LPKDATE")
    private Date lpkdate;

    @Column(name = "LPKGRUP", length = 50)
    private String lpkgrup;

    @Column(name = "LPKMODU", length = 50)
    private String lpkmodu;

    @Column(name = "LPKUSER", length = 50)
    private String lpkuser;

    @Column(name = "LPKORIU", length = 50)
    private String lpkoriu;

    @Column(name = "LPKORIG", length = 50)
    private String lpkorig;

    @Column(name = "LPKPOS", length = 50)
    private String lpkpos;

    @Column(name = "LPK051")
    private Integer lpk051;

    // ==========================================
    //  使用者自訂欄位區 (User Defined Fields)
    // ==========================================
    @Column(name = "LPKUD01", length = 255)
    private String lpkud01;

    @Column(name = "LPKUD02", length = 255)
    private String lpkud02;

    @Column(name = "LPKUD03", length = 255)
    private String lpkud03;

    @Column(name = "LPKUD04", length = 255)
    private String lpkud04;

    @Column(name = "LPKUD05", length = 255)
    private String lpkud05;

    @Column(name = "LPKUD06", length = 255)
    private String lpkud06;

    @Column(name = "LPKUD07")
    private Double lpkud07;

    @Column(name = "LPKUD08")
    private Double lpkud08;

    @Column(name = "LPKUD09")
    private Double lpkud09;

    @Column(name = "LPKUD10")
    private Integer lpkud10;

    @Column(name = "LPKUD11")
    private Integer lpkud11;

    @Column(name = "LPKUD12")
    private Integer lpkud12;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPKUD13")
    private Date lpkud13;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPKUD14")
    private Date lpkud14;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPKUD15")
    private Date lpkud15;

    @Column(name = "LPK052")
    private Integer lpk052;

    @Column(name = "LPK053")
    private Integer lpk053;

    @Column(name = "LPK20", length = 255)
    private String lpk20;

    @Column(name = "LPK21", length = 255)
    private String lpk21;

    // ==========================================
    //  客製擴充 TA_LPK 欄位區
    // ==========================================
    @Column(name = "TA_LPK01")
    private Integer taLpk01;

    @Column(name = "TA_LPK02", length = 255)
    private String taLpk02;

    @Column(name = "TA_LPK03", length = 255)
    private String taLpk03;

    @Temporal(TemporalType.DATE)
    @Column(name = "TA_LPK04")
    private Date taLpk04;

    @Column(name = "TA_LPK05", length = 255)
    private String taLpk05;

    @Column(name = "TA_LPK06", length = 255)
    private String taLpk06;

    @Column(name = "TA_LPK07", length = 255)
    private String taLpk07;

    @Column(name = "TA_LPK08", length = 255)
    private String taLpk08;

    // ==========================================
    //  舊系統混入的 LPJ_ 寬表映射欄位區
    // ==========================================
    @Column(name = "LPJ03", length = 50)
    private String lpj03;

    @Column(name = "LPJ12")
    private Double lpj12;

    @Column(name = "LPJ15")
    private Double lpj15;

    @Column(name = "TA_LPJ01")
    private Double taLpj01;

    @Column(name = "TA_LPJ02")
    private Double taLpj02;

    @Column(name = "TA_LPJ03")
    private Double taLpj03;

    // ==========================================
    //  @Transient 非持久化業務欄位
    // ==========================================
    @Transient
    private Integer vipLevel;

    @Transient
    private Double lsm081; // 對應舊系統 lsm08_1

    @Transient
    private Double lsm082; // 對應舊系統 lsm08_2
    
    @Transient
    private Double totalLsm08; // 僅供展示的去年總點數，不對應資料庫欄位

    public LPK_FILE() {
    }
    
}