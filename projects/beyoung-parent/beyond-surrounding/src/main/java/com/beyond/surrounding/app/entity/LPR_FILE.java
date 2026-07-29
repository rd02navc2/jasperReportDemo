package com.beyond.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LPR_FILE")
@IdClass(LPR_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class LPR_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPR01", length = 50)
    private String lpr01;

    @Column(name = "LPR02")
    private Integer lpr02;

    @Column(name = "LPR03", length = 255)
    private String lpr03;

    @Column(name = "LPRLEGAL", length = 50)
    private String lprlegal;

    @Id
    @Column(name = "LPRPLANT", length = 50)
    private String lprplant;

    @Column(name = "LPRPOS", length = 50)
    private String lprpos;

    @Column(name = "LPR04", length = 255)
    private String lpr04;

    @Column(name = "LPR05")
    private Double lpr05;

    @Id
    @Column(name = "LPR06")
    private Integer lpr06;

    @Column(name = "LPR07")
    private Double lpr07;

    @Id
    @Column(name = "LPR08", length = 50)
    private String lpr08;

    @Id
    @Column(name = "LPR00", length = 50)
    private String lpr00;

    @Id
    @Column(name = "LPR09", length = 50)
    private String lpr09;

    @Column(name = "TA_LPR01", length = 255)
    private String taLpr01;

    @Column(name = "TA_LPR02")
    private Double taLpr02;

    @Column(name = "TA_LPR03", length = 255)
    private String taLpr03;
    
}