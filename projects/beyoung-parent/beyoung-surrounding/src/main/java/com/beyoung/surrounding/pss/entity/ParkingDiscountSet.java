package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

/**
 * ParkingDiscountSet
 * 停車優惠設定主檔（現代化重構版）
 * 已升級至 Jakarta Persistence、導入 Lombok，並規範化欄位命名與映射
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PARKING_DISCOUNT_SET")
@DynamicInsert
public class ParkingDiscountSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DISC_ID", length = 50)
    private String discId;

    @Column(name = "IS_ACTIVE", length = 10)
    private String isActive;

    @Column(name = "DISC_NAME", length = 100)
    private String discName;

    @Column(name = "DISC_DESC", length = 255)
    private String discDesc;

    @Column(name = "DISC_HOUR")
    private Double discHour;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder; // 將基本型態 int 改為包裝類別 Integer，容許資料庫 Null 值

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCESS_DATE")
    private Date accessDate;

    @Column(name = "ACCESS_ID", length = 50)
    private String accessId;

    @Column(name = "P_NO")
    private Integer pNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BOOKING_DATE")
    private Date bookingDate;

    @Column(name = "IS_USED", length = 10)
    private String isUsed;

    @Column(name = "PROMOTE_AMT")
    private Double promoteAmt;

    @Column(name = "IS_UNLIMITED_HOUR", length = 10)
    private String isUnlimitedHour;

    @Column(name = "HOUR_MAX")
    private Integer hour_max;

    // ==========================================
    //  非資料庫映射欄位 (Transient)
    // ==========================================
    @Transient
    private Double usedHour;
}