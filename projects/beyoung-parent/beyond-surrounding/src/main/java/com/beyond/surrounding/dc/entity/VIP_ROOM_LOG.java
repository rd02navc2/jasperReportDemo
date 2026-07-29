package com.beyond.surrounding.dc.entity;

import jakarta.persistence.*; // 升級為 jakarta 規範
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VIP_ROOM_LOG")
@IdClass(VIP_ROOM_LOG_ComposeKey.class) // 綁定複合主鍵類別
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VIP_ROOM_LOG implements Serializable {
	

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "center")
    private String center; // 複合主鍵 1

    @Id
    @Column(name = "transaction_date")
    @Temporal(TemporalType.DATE) // 指定僅對應日期
    private Date transactionDate; // 複合主鍵 2

    @Id
    @Column(name = "user_id")
    private String userId; // 複合主鍵 3

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

    @Column(name = "total_qty")
    private Integer totalQty;

    @Column(name = "access_id")
    private String accessId;

    // ERP 整合擴充欄位 (保留全大寫)
    @Column(name = "LPK01") private String LPK01;
    @Column(name = "LPK04") private String LPK04;
    @Column(name = "LPK06") private String LPK06;
    @Column(name = "LPK15") private String LPK15;
    @Column(name = "LPK18") private String LPK18;

    // 非實體映射欄位 (Transient)
    @Transient private Integer age;
    @Transient private Integer rec_cnt;
    @Transient private Integer tot_rec_cnt;

    // 鋼鐵防禦空建構子
	public VIP_ROOM_LOG() {
	}
	    
}