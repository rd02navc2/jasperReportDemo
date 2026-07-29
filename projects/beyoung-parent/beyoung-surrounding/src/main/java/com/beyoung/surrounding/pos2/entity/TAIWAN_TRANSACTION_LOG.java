package com.beyoung.surrounding.pos2.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "POS2_TAIWAN_TRANSACTION_LOG")
@Table(name = "TAIWAN_TRANSACTION_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TAIWAN_TRANSACTION_LOG implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;

    @Column(name = "pos_center")
    private String posCenter;

    @Column(name = "pos_counter_id")
    private String posCounterId;

    @Column(name = "pos_product_name")
    private String posProductName;

    @Column(name = "pos_amount")
    private Integer posAmount;

    @Column(name = "pos_id")
    private String posId;

    @Column(name = "pos_date_time")
    private Date posDateTime;

    @Column(name = "access_date")
    private Date  accessDate;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_date")
    private Date  transactionDate;

    @Column(name = "one_time_key")
    private String oneTimeKey;

    @Column(name = "refund_transaction_id")
    private Integer refundTransactionId;

    @Column(name = "refund_transaction_date")
    private Date  refundTransactionDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    // dc-
    @Column(name = "wallet_provider")
    private String walletProvider;

    // 非資料庫欄位
    @Transient
    private String code;

    @Transient
    private String message;

    @Transient
    private String transTime;
}