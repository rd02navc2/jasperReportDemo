package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "POS2_LNT_FILE")
@Table(name = "LNT_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LntFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LNT01", length = 50)
    private String LNT01;

    @Column(name = "LNT02")
    private String LNT02;

    @Column(name = "LNT03")
    private Date LNT03;

    @Column(name = "LNT04")
    private String LNT04;

    @Column(name = "LNT05")
    private String LNT05;

    @Column(name = "LNT06")
    private String LNT06;

    @Column(name = "LNT07")
    private String LNT07;

    @Column(name = "LNT08")
    private String LNT08;

    @Column(name = "LNT09")
    private String LNT09;

    @Column(name = "LNT10")
    private Double LNT10;

    @Column(name = "LNT11")
    private Double LNT11;

    @Column(name = "LNT12")
    private String LNT12;

    @Column(name = "LNT13")
    private String LNT13;

    @Column(name = "LNT14")
    private Double LNT14;

    @Column(name = "LNT15")
    private String LNT15;

    @Column(name = "LNT16")
    private String LNT16;

    @Column(name = "LNT17")
    private String LNT17;

    @Column(name = "LNT18")
    private String LNT18;

    @Column(name = "LNT19")
    private Date LNT19;

    @Column(name = "LNT20")
    private Date LNT20;

    @Column(name = "LNT21")
    private Date LNT21;

    @Column(name = "LNT22")
    private Date LNT22;

    @Column(name = "LNT23")
    private Date LNT23;

    @Column(name = "LNT24")
    private String LNT24;

    @Column(name = "LNT25")
    private String LNT25;

    @Column(name = "LNT26")
    private String LNT26;

    @Column(name = "LNT27")
    private String LNT27;

    @Column(name = "LNT28")
    private Date LNT28;

    @Column(name = "LNT29")
    private String LNT29;

    @Column(name = "LNT30")
    private String LNT30;

    @Column(name = "LNT31")
    private String LNT31;

    @Column(name = "LNT32")
    private String LNT32;

    @Column(name = "LNT33")
    private String LNT33;

    @Column(name = "LNT34")
    private String LNT34;

    @Column(name = "LNT35")
    private String LNT35;

    @Column(name = "LNT36")
    private Double LNT36;

    @Column(name = "LNT37")
    private String LNT37;

    @Column(name = "LNT38")
    private String LNT38;

    @Column(name = "LNT39")
    private Integer LNT39;

    @Column(name = "LNT40")
    private String LNT40;

    @Column(name = "LNT41")
    private String LNT41;

    @Column(name = "LNT42")
    private Double LNT42;

    @Column(name = "LNT43")
    private String LNT43;

    @Column(name = "LNT44")
    private String LNT44;

    @Column(name = "LNT45")
    private String LNT45;

    @Column(name = "LNT46")
    private Date LNT46;

    @Column(name = "LNT47")
    private String LNT47;

    @Column(name = "LNT48")
    private String LNT48;

    @Column(name = "LNT49")
    private String LNT49;

    @Column(name = "LNT50")
    private String LNT50;

    @Column(name = "LNT51")
    private Integer LNT51;

    @Column(name = "LNT52")
    private Date LNT52;

    @Column(name = "LNT53")
    private Date LNT53;

    @Column(name = "LNTACTI")
    private String LNTACTI;

    @Column(name = "LNTCRAT")
    private Date LNTCRAT;

    @Column(name = "LNTDATE")
    private String LNTDATE;

    @Column(name = "LNTGRUP")
    private String LNTGRUP;

    @Column(name = "LNTLEGAL")
    private String LNTLEGAL;

    @Column(name = "LNTMODU")
    private String LNTMODU;

    @Column(name = "LNTUSER")
    private String LNTUSER;

    @Column(name = "LNTORIU")
    private String LNTORIU;

    @Column(name = "LNTORIG")
    private String LNTORIG;

    @Column(name = "LNTPLANT")
    private String LNTPLANT;

    @Column(name = "LNT54")
    private String LNT54;

    @Column(name = "LNTPOS")
    private String LNTPOS;

    @Column(name = "LNT55")
    private String LNT55;

    @Column(name = "LNT56")
    private String LNT56;

    @Column(name = "LNT57")
    private String LNT57;

    @Column(name = "LNT58")
    private String LNT58;

    @Column(name = "LNT59")
    private String LNT59;

    @Column(name = "LNT60")
    private String LNT60;

    @Column(name = "LNT61")
    private Double LNT61;

    @Column(name = "LNT62")
    private String LNT62;

    @Column(name = "LNT63")
    private String LNT63;

    @Column(name = "LNT64")
    private Double LNT64;

    @Column(name = "LNT65")
    private Double LNT65;

    @Column(name = "LNT66")
    private Double LNT66;

    @Column(name = "LNT67")
    private Double LNT67;

    @Column(name = "LNT68")
    private Double LNT68;

    @Column(name = "LNT69")
    private Double LNT69;

    @Column(name = "LNT70")
    private String LNT70;

    @Column(name = "LNT71")
    private String LNT71;

    @Column(name = "LNT72")
    private Integer LNT72;

    @Column(name = "LNT73")
    private String LNT73;

    @Column(name = "TA_LNT01")
    private Double TA_LNT01;

    @Column(name = "TA_LNT02")
    private Integer TA_LNT02;

    @Column(name = "TA_LNT03")
    private Integer TA_LNT03;

    @Column(name = "TA_LNT04")
    private Double TA_LNT04;

    //  調整：同步修正為 TEXT，釋放既有欄位的行空間
    @Column(name = "TA_LNT05", columnDefinition = "TEXT")
    private String TA_LNT05;

    @Column(name = "TA_LNT06", columnDefinition = "TEXT")
    private String TA_LNT06;

    @Column(name = "TA_LNT07", columnDefinition = "TEXT")
    private String TA_LNT07;

    @Column(name = "TA_LNT08", columnDefinition = "TEXT")
    private String TA_LNT08;

    @Column(name = "TA_LNT09")
    private Double TA_LNT09;

    @Column(name = "TA_LNT10", columnDefinition = "TEXT")
    private String TA_LNT10;

    @Column(name = "TA_LNT11", columnDefinition = "TEXT")
    private String TA_LNT11;

    @Column(name = "TA_LNT12", columnDefinition = "TEXT")
    private String TA_LNT12;

    @Column(name = "TA_LNT13", columnDefinition = "TEXT")
    private String TA_LNT13;

    @Column(name = "TA_LNT14")
    private Integer TA_LNT14;

    @Column(name = "TA_LNT15", columnDefinition = "TEXT")
    private String TA_LNT15;

    @Column(name = "TQA02", columnDefinition = "TEXT")
    private String TQA02;

    @Column(name = "TC_PSA12")
    private Double TC_PSA12;

    @Column(name = "TC_PSA40")
    private Double TC_PSA40;

    @Column(name = "TC_PSA04")
    private Date TC_PSA04;

    @Column(name = "TC_PSA05", columnDefinition = "TEXT")
    private String TC_PSA05;

    @Column(name = "OBA01", length = 50) // 主動縮減不必要長度
    private String OBA01;

    @Column(name = "OBA02", columnDefinition = "TEXT")
    private String OBA02;

    @Column(name = "OBA02_2", columnDefinition = "TEXT") //  新增：同步改為 TEXT
    private String OBA02_2;

    @Column(name = "TC_LND03")
    private String TC_LND03;

    @Column(name = "TC_LND04")
    private String TC_LND04;

    // ==========================================
    //  🔥 核心引爆點：強制改為 TEXT 規避 Row Size 65535 錯誤
    // ==========================================
    @Column(name = "TC_LND10", columnDefinition = "TEXT")
    private String TC_LND10;

    @Column(name = "TC_LND11", columnDefinition = "TEXT")
    private String TC_LND11;

    @Column(name = "TC_LND14", columnDefinition = "TEXT")
    private String TC_LND14;

    @Column(name = "LNE06")
    private String LNE06;

    @Column(name = "GEM02")
    private String GEM02;
}