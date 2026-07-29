package com.beyoung.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

/**
 * OBA_FILE 實體重構版
 * 升級至 Jakarta Persistence, 採用 Lombok 簡化
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "OBA_FILE")
public class OBA_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "OBA01", length = 50)
    private String oba01;
    
    @Column(name = "OBA02", length = 255)
    private String oba02;
    
    // 這些欄位如果原本是 JOIN 查詢暫存，建議保留或移至 DTO
    @Column(name = "LNT09", length = 50)
    private String lnt09;
    
    @Column(name = "LNT06", length = 50)
    private String lnt06;
    
    @Column(name = "TQA02", length = 255)
    private String tqa02;

    @Temporal(TemporalType.DATE)
    @Column(name = "LNT21")
    private Date lnt21;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "LNT22")
    private Date lnt22;

    @Transient
    private String excelLnt09;
    
    @Transient
    private String excelOba01;

    @Transient
    private Integer recCnt;
}