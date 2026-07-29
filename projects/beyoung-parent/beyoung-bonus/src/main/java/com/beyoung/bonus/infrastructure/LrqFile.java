package com.beyoung.bonus.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LRQ_FILE")
@IdClass(LrqFileId.class)
public class LrqFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LRQ01", length = 10)
    private String lrq01;
	
    @Id
    @Column(name = "LRQ02", length = 50)
    private String lrq02;

    @Id
    @Column(name = "LRQPLANT", length = 10)
    private String lrqplant;

    @Column(name = "LRQ03")
    private Integer lrq03;

    @Column(name = "LRQACTI", length = 5)
    private String lrqacti;

    @Column(name = "LRQ10")
    private LocalDate lrq10;

    @Column(name = "LRQ11")
    private LocalDate lrq11;

    @Column(name = "LRQ04")
    private Double lrq04;

    @Column(name = "LRQ05")
    private Integer lrq05;

    @Column(name = "LRQ06", length = 20)
    private String lrq06;

    @Column(name = "LRQCONF", length = 5)
    private String lrqconf;

    @Column(name = "LRQUU", length = 20)
    private String lrquu;

    @Column(name = "LRQGRUP", length = 20)
    private String lrqgrup;

    @Column(name = "LRQMODU", length = 20)
    private String lrqmodu;

    @Column(name = "LRQDATE")
    private LocalDate lrqdate;

    @Column(name = "LRQUID", length = 50)
    private String lrquid;
}