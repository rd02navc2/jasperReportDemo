package com.beyoung.parking.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_COUPON_LOG", schema = "parking_sf")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingCouponLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "center", length = 100)
    private String center;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "case_no", length = 100)
    private String caseNo;

    @Column(name = "coupon_no", length = 100)
    private String couponNo;

    @Column(name = "case_item", length = 255)
    private String caseItem;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "point")
    private Integer point;

    @Column(name = "access_date")
    private LocalDateTime accessDate;

    @Column(name = "access_id", length = 100)
    private String accessId;
}