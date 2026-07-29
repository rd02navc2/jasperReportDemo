package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "POS2_LPX_FILE")
@Table(name = "LPX_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LpxFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPX01")
    private String lpx01;

    @Column(name = "LPX02")
    private String lpx02;

    @Column(name = "LPX03")
    private Date lpx03;

    @Column(name = "LPX04")
    private Date lpx04;

    @Column(name = "LPX17")
    private String lpx17;

    @Column(name = "LPX23")
    private String lpx23;

    @Column(name = "LPX28")
    private String lpx28;
}