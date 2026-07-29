package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

/**
 * ParkingPosLog
 * 停車場 POS 交易日誌實體類別（現代化重構版）
 * 已升級至 Jakarta Persistence 規範、導入 Lombok 簡化，並規範化欄位映射
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PARKING_POS_LOG")
@DynamicInsert
public class ParkingPosLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 明確定義為資料庫自增主鍵（IDENTITY）
    @Column(name = "SN")
    private Integer sn;

    @Temporal(TemporalType.DATE) // 發票日期通常只需精確到日
    @Column(name = "INVOICE_DATE")
    private Date invoiceDate;

    @Column(name = "INVOICE_NO", length = 50)
    private String invoiceNo;

    @Column(name = "RANDOM_NO", length = 20)
    private String randomNo;

    @Column(name = "INVOICE_TIME", length = 20)
    private String invoiceTime;

    @Column(name = "P_NO")
    private Integer pNo;

    @Column(name = "CENTER", length = 50)
    private String center;

    @Column(name = "CHANNEL", length = 50)
    private String channel;

    @Column(name = "TRANX_TYPE")
    private Integer tranxType;

    @Column(name = "COUNTER_ID", length = 50)
    private String counterId;

    @Column(name = "COUNTER_NAME", length = 100)
    private String counterName;

    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "USER_NAME", length = 100)
    private String userName;

    @Column(name = "CARD_NO", length = 50)
    private String cardNo;

    @Column(name = "CAR_NO", length = 50)
    private String carNo;

    @Column(name = "PROMOTE_AMT")
    private Double promoteAmt;

    @Temporal(TemporalType.TIMESTAMP) // 系統寫入時間戳記，保留時分秒精度
    @Column(name = "ACCESS_DATE")
    private Date accessDate;

    @Column(name = "ACCESS_ID", length = 50)
    private String accessId;
}