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
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LRQ_FILE")
@IdClass(LRQ_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class LRQ_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "LRQ00", length = 255)
    private String lrq00;

    @Column(name = "LRQ01", length = 255)
    private String lrq01;

    @Id
    @Column(name = "LRQ02", length = 50)
    private String lrq02;

    @Column(name = "LRQ03")
    private Integer lrq03;

    @Column(name = "LRQ04")
    private Integer lrq04;

    @Column(name = "LRQ05")
    private Double lrq05;

    @Column(name = "LRQ06")
    private Double lrq06;

    @Column(name = "LRQ07")
    private Double lrq07;

    @Column(name = "LRQ08")
    private Double lrq08;

    @Column(name = "LRQ09", length = 255)
    private String lrq09;

    @Temporal(TemporalType.DATE)
    @Column(name = "LRQ10")
    private Date lrq10;

    @Temporal(TemporalType.DATE)
    @Column(name = "LRQ11")
    private Date lrq11;

    @Column(name = "LRQACTI", length = 10)
    private String lrqacti;

    @Id
    @Column(name = "LRQ12", length = 50)
    private String lrq12;

    @Id
    @Column(name = "LRQ13", length = 50)
    private String lrq13;

    @Column(name = "LRQLEGAL", length = 50)
    private String lrqlegal;

    @Id
    @Column(name = "LRQPLANT", length = 50)
    private String lrqplant;
}