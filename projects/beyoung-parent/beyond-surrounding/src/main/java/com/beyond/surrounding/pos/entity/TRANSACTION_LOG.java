package com.beyond.surrounding.pos.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "POS_TRANSACTION_LOG")
@Table(name = "TRANSACTION_LOG")
public class TRANSACTION_LOG implements Serializable {
	
	private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sn;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "pos_center")
    private String posCenter;

    @Column(name = "pos_counter_id")
    private String posCounterId;

    @Column(name = "pos_product_name")
    private String posProductName;

    @Column(name = "pos_amount")
    private Integer posAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "access_date")
    private Date accessDate;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_id")
    private String transactionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "one_time_key")
    private String oneTimeKey;

    @Column(name = "need_check")
    private String needCheck;

    @Column(name = "refund_transaction_id")
    private Integer refundTransactionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "refund_transaction_date")
    private Date refundTransactionDate;

    @Column(name = "invoice_no")
    private String invoiceNo;
}