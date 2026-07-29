package com.beyoung.surrounding.pos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "POS_READING_SPACE_LOG")
@Table(name = "READING_SPACE_LOG")
@IdClass(READING_SPACE_LOG_ComposeKey.class)
public class READING_SPACE_LOG implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "center")
    private String center;

    @Id
    @Column(name = "transaction_date")
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "transaction_time")
    private String transactionTime;

    @Column(name = "counter_id")
    private String counterId;

    @Column(name = "pos_id")
    private String posId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "price")
    private Integer price;

    @Column(name = "enter_date")
    private Date enterDate;

    @Column(name = "exit_date")
    private Date exitDate;

    @Column(name = "refund_date")
    private Date refundDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "vip")
    private String vip;

    @Column(name = "in_room")
    private String inRoom;

    @Column(name = "comment")
    private String comment;

    @Column(name = "point")
    private Integer point;

    @Transient
    private Integer recCnt;
}