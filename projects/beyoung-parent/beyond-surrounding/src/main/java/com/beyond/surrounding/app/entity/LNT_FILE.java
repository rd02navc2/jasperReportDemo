package com.beyond.surrounding.app.entity;

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
@Table(name = "LNT_FILE")
@DynamicInsert
@DynamicUpdate
public class LNT_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LNT01", length = 50)
    private String lnt01;

    @Column(name = "LNT02", length = 255)
    private String lnt02;

    @Temporal(TemporalType.DATE)
    @Column(name = "LNT03")
    private Date lnt03;

    @Column(name = "LNT04", length = 255)
    private String lnt04;

    @Column(name = "LNT05", length = 255)
    private String lnt05;

    @Column(name = "LNT06", length = 255)
    private String lnt06;

    @Column(name = "LNT07", length = 255)
    private String lnt07;

    @Column(name = "LNT08", length = 255)
    private String lnt08;

    @Column(name = "LNT09", length = 255)
    private String lnt09;

    @Column(name = "LNT10")
    private Double lnt10;

    @Column(name = "LNT11")
    private Double lnt11;

    @Column(name = "LNT12", length = 255)
    private String lnt12;

    @Column(name = "LNT13", length = 255)
    private String lnt13;

    @Column(name = "LNT14")
    private Double lnt14;

    @Column(name = "LNT15", length = 255)
    private String lnt15;

    @Column(name = "LNT16", length = 255)
    private String lnt16;

    @Temporal(TemporalType.DATE) @Column(name = "LNT17") private Date lnt17;
    @Temporal(TemporalType.DATE) @Column(name = "LNT18") private Date lnt18;
    @Temporal(TemporalType.DATE) @Column(name = "LNT19") private Date lnt19;
    @Temporal(TemporalType.DATE) @Column(name = "LNT20") private Date lnt20;
    @Temporal(TemporalType.DATE) @Column(name = "LNT21") private Date lnt21;
    @Temporal(TemporalType.DATE) @Column(name = "LNT22") private Date lnt22;
    @Temporal(TemporalType.DATE) @Column(name = "LNT23") private Date lnt23;

    @Column(name = "LNT24", length = 255)
    private String lnt24;

    @Column(name = "LNT25", length = 255)
    private String lnt25;

    @Column(name = "LNT26", length = 255)
    private String lnt26;

    @Column(name = "LNT27", length = 255)
    private String lnt27;

    @Temporal(TemporalType.DATE)
    @Column(name = "LNT28")
    private Date lnt28;

    @Column(name = "LNT29", length = 255)
    private String lnt29;

    @Column(name = "LNT30", length = 255)
    private String lnt30;

    @Column(name = "LNT31", length = 255)
    private String lnt31;

    @Column(name = "LNT32", length = 255)
    private String lnt32;

    @Column(name = "LNT33", length = 255)
    private String lnt33;

    @Column(name = "LNT34", length = 255)
    private String lnt34;

    @Column(name = "LNT35", length = 255)
    private String lnt35;

    @Column(name = "LNT36")
    private Double lnt36;

    @Column(name = "LNT37", length = 255)
    private String lnt37;

    @Column(name = "LNT38", length = 255)
    private String lnt38;

    @Column(name = "LNT39")
    private Integer lnt39;

    @Column(name = "LNT40", length = 255)
    private String lnt40;

    @Column(name = "LNT41", length = 255)
    private String lnt41;

    @Column(name = "LNT42")
    private Double lnt42;

    @Column(name = "LNT43", length = 255)
    private String lnt43;

    @Column(name = "LNT44", length = 255)
    private String lnt44;

    @Column(name = "LNT45", length = 255)
    private String lnt45;

    @Temporal(TemporalType.DATE)
    @Column(name = "LNT46")
    private Date lnt46;

    @Column(name = "LNT47", length = 255)
    private String lnt47;

    @Column(name = "LNT48", length = 255)
    private String lnt48;

    @Column(name = "LNT49", length = 255)
    private String lnt49;

    @Column(name = "LNT50", length = 255)
    private String lnt50;

    @Column(name = "LNT51")
    private Integer lnt51;

    @Temporal(TemporalType.DATE) @Column(name = "LNT52") private Date lnt52;
    @Temporal(TemporalType.DATE) @Column(name = "LNT53") private Date lnt53;

    @Column(name = "LNTACTI", length = 10)
    private String lntacti;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LNTCRAT")
    private Date lntcrat;

    @Temporal(TemporalType.DATE)
    @Column(name = "LNTDATE")
    private Date lntdate;

    @Column(name = "LNTGRUP", length = 50)
    private String lntgrup;

    @Column(name = "LNTLEGAL", length = 10)
    private String lntlegal;

    @Column(name = "LNTMODU", length = 50)
    private String lntmodu;

    @Column(name = "LNTUSER", length = 50)
    private String lntuser;

    @Column(name = "LNTORIU", length = 50)
    private String lntoriu;

    @Column(name = "LNTORIG", length = 50)
    private String lntorig;

    @Column(name = "LNTPLANT", length = 10)
    private String lntplant;

    @Column(name = "LNT54", length = 255)
    private String lnt54;

    @Column(name = "LNTPOS", length = 50)
    private String lntpos;

    @Column(name = "LNT55", length = 255)
    private String lnt55;

    @Column(name = "LNT56", length = 255)
    private String lnt56;

    @Column(name = "LNT57", length = 255)
    private String lnt57;

    @Column(name = "LNT58", length = 255)
    private String lnt58;

    @Column(name = "LNT59", length = 255)
    private String lnt59;

    @Column(name = "LNT60", length = 255)
    private String lnt60;

    @Column(name = "LNT61")
    private Double lnt61;

    @Column(name = "LNT62", length = 255)
    private String lnt62;

    @Column(name = "LNT63", length = 255)
    private String lnt63;

    @Column(name = "LNT64") private Double lnt64;
    @Column(name = "LNT65") private Double lnt65;
    @Column(name = "LNT66") private Double lnt66;
    @Column(name = "LNT67") private Double lnt67;
    @Column(name = "LNT68") private Double lnt68;
    @Column(name = "LNT69") private Double lnt69;

    @Column(name = "LNT70", length = 255)
    private String lnt70;

    @Column(name = "LNT71", length = 255)
    private String lnt71;

    @Column(name = "LNT72")
    private Integer lnt72;

    @Column(name = "LNT73", length = 255)
    private String lnt73;

    @Column(name = "TA_LNT01") private Double taLnt01;
    @Column(name = "TA_LNT02") private Integer taLnt02;
    @Column(name = "TA_LNT03") private Integer taLnt03;
    @Column(name = "TA_LNT04") private Double taLnt04;

    @Column(name = "TA_LNT05", columnDefinition = "TEXT") private String taLnt05;
    @Column(name = "TA_LNT06", columnDefinition = "TEXT") private String taLnt06;
    @Column(name = "TA_LNT07", columnDefinition = "TEXT") private String taLnt07;
    @Column(name = "TA_LNT08", columnDefinition = "TEXT") private String taLnt08;
    @Column(name = "TA_LNT09") private Double taLnt09;
    @Column(name = "TA_LNT10", columnDefinition = "TEXT") private String taLnt10;
    @Column(name = "TA_LNT11", columnDefinition = "TEXT") private String taLnt11;
    @Column(name = "TA_LNT12", columnDefinition = "TEXT") private String taLnt12;
    @Column(name = "TA_LNT13", columnDefinition = "TEXT") private String taLnt13;
    @Column(name = "TA_LNT14") private Integer taLnt14;
    @Column(name = "TA_LNT15", columnDefinition = "TEXT") private String taLnt15;

    @Column(name = "TQA02", columnDefinition = "TEXT")
    private String tqa02;

    @Column(name = "TC_PSA12")
    private Double tcPsa12;

    @Column(name = "TC_PSA40")
    private Double tcPsa40;

    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSA04")
    private Date tcPsa04; 

    @Column(name = "TC_PSA05", columnDefinition = "TEXT")
    private String tcPsa05;

    @Column(name = "OBA01", length = 50)
    private String oba01;

    @Column(name = "OBA02", columnDefinition = "TEXT")
    private String oba02;

    public LNT_FILE() {
    }
    
}