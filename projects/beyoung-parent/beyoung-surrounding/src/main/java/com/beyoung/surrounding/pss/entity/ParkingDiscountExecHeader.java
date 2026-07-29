package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import com.google.gson.JsonArray;
import java.io.Serializable;
import java.util.Date;

/**
 * ParkingDiscountExecHeader
 * 停車優惠執行主檔實體類別（現代化重構版）
 * 已升級為 Jakarta Persistence 規範、導入 Lombok 簡化，並補齊欄位映射
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PARKING_DISCOUNT_EXEC_HEADER")
@DynamicInsert
public class ParkingDiscountExecHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 明確定義自增策略，防範個別資料庫行為不一致
    @Column(name = "P_NO")
    private Integer pNo;
    
    @Column(name = "IS_SYNC", length = 10)
    private String isSync;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP) // 依欄位性質（進出場時間），保留時分秒精度
    @Column(name = "ENTER_DATE")
    private Date enterDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXIT_DATE")
    private Date exitDate;

    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "CENTER", length = 50)
    private String center;

    @Column(name = "CARD_ID", length = 50)
    private String cardId;

    @Column(name = "CAR_NO", length = 50)
    private String carNo;

    @Column(name = "PARKING_HOUR")
    private Double parkingHour;

    @Column(name = "PARKING_FEE")
    private Double parkingFee;

    @Column(name = "DISC_FEE")
    private Double discFee;

    @Column(name = "PAY_AMT")
    private Double payAmt;

    @Column(name = "PAID_AMT")
    private Double paidAmt;

    @Column(name = "TOT_DISC_HOUR")
    private Double totDiscHour;

    @Column(name = "REAL_DISC_HOUR")
    private Double realDiscHour;

    @Column(name = "OTHER_DISC_FEE")
    private Double otherDiscFee;

    @Column(name = "OTHER_DISC_HOUR")
    private Double otherDiscHour;

    @Column(name = "IS_USED", length = 10)
    private String isUsed;

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

    // ==========================================
    //  非資料庫映射欄位 (Transient)
    // ==========================================
    @Transient
    private JsonArray jaDiscount;

    @Transient
    private String isUnlimitedDate;

    @Transient
    private String userName;
}