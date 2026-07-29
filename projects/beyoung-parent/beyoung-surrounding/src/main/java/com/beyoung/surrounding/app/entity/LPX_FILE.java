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
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LPX_FILE")
@DynamicInsert
@DynamicUpdate
public class LPX_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPX01", length = 50)
    private String lpx01;

    @Column(name = "LPX02", length = 255)
    private String lpx02;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPX03")
    private Date lpx03;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPX04")
    private Date lpx04;

    @Column(name = "LPX28", length = 255)
    private String lpx28;
}