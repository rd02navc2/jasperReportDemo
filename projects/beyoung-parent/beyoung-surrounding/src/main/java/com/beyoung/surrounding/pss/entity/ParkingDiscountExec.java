package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PARKING_DISCOUNT_EXEC")
@IdClass(ParkingDiscountExecId.class) // 指向獨立的 Id 類別
@DynamicInsert
public class ParkingDiscountExec implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "P_NO")
    private Integer pNo;

    @Id
    @Column(name = "CAR_NO", length = 50)
    private String carNo;

    @Id
    @Column(name = "DISC_ID", length = 50)
    private String discId;

    @Column(name = "CARD_ID", length = 50)
    private String cardId;

    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "DISC_NAME", length = 100)
    private String discName;

    @Column(name = "DISC_HOUR")
    private Double discHour;

    @Column(name = "CENTER", length = 50)
    private String center;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BOOKING_DATE")
    private Date bookingDate;

    @Column(name = "BOOKING_ID", length = 50)
    private String bookingId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCESS_DATE")
    private Date accessDate;

    @Column(name = "ACCESS_ID", length = 50)
    private String accessId;

    @Column(name = "IS_USED", length = 10)
    private String isUsed;

    @Transient
    private String modify;

    @Transient
    private Date enterDate;

    @Transient
    private Date exitDate;

    @Transient
    private Double usedHour;

    @Transient
    private String setDate;
}