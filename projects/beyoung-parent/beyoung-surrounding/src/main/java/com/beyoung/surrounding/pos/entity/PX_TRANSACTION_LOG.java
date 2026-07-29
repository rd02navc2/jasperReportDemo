package com.beyoung.surrounding.pos.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "POS_PX_TRANSACTION_LOG")
@Table(name = "PX_TRANSACTION_LOG")
public class PX_TRANSACTION_LOG implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
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

    @Column(name = "pos_id")
    private String posId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pos_date_time")
    private Date posDateTime;

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

    @Column(name = "pay_tool")
    private String payTool;

    @Column(name = "tool_name")
    private String toolName;

    @Column(name = "identity")
    private String identity;

    @Column(name = "one_time_key")
    private String oneTimeKey;

    @Column(name = "refund_order_id")
    private String refundOrderId;

    @Column(name = "refund_transaction_id")
    private Integer refundTransactionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "refund_transaction_date")
    private Date refundTransactionDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "invo_carrier")
    private String invoCarrier;
}