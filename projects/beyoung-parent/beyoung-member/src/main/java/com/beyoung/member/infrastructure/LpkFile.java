package com.beyoung.member.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LPK_FILE")
public class LpkFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPK01", length = 50)
    private String lpk01; // 會員系統內部 ID

    @Column(name = "LPK02", length = 10)
    private String lpk02; // 會員類別/狀態

    @Column(name = "LPK03", length = 50)
    private String lpk03; // 身分證字號

    @Column(name = "LPK04", length = 100)
    private String lpk04; // 會員姓名 / 等級名稱

    @Column(name = "LPK05")
    @Temporal(TemporalType.DATE)
    private java.util.Date lpk05; // 生日

    @Column(name = "LPK06", length = 5)
    private String lpk06; // 性別 1:男 0:女

    @Column(name = "LPK10", length = 20)
    private String lpk10; 

    @Column(name = "LPK13", length = 20)
    private String lpk13; 

    @Column(name = "LPK14", length = 20)
    private String lpk14;
    
    @Column(name = "LPK15", length = 255)
    private String lpk15; // 通訊地址

    @Column(name = "LPK18", length = 50)
    private String lpk18; // 行動電話

    @Column(name = "LPK19", length = 100)
    private String lpk19; // Email

    @Column(name = "LPKACTI", length = 5)
    private String lpkacti; // 有效旗標 (Y/N)

    @Column(name = "LPKCRAT")
    private LocalDateTime lpkcrat; // 建立時間

    @Column(name = "LPKDATE")
    private LocalDateTime lpkdate; // 異動日期

    @Column(name = "LPKORIU", length = 50)
    private String lpkoriu; // 原創人員

    @Column(name = "LPKORIG", length = 50)
    private String lpkorig; // 原創群組

    @Column(name = "LPKPOS", length = 5)
    private String lpkpos; // POS 狀態

    @Column(name = "LPKUD02", length = 50)
    private String lpkud02; // 自訂欄位02 VIP

    @Column(name = "LPKUD08")
    private Integer lpkud08; // 自訂欄位08

    @Column(name = "LPKUD09")
    private Integer lpkud09; // 自訂欄位09

    @Column(name = "LPKUD10")
    private Integer lpkud10; // 自訂欄位10

    @Column(name = "LPK20", length = 50)
    private String lpk20; 

    @Column(name = "LPK21", length = 50)
    private String lpk21; 

    @Column(name = "TA_LPK04")
    private LocalDateTime taLpk04; // 擴充時間欄位

    @Column(name = "TA_LPK05", length = 5)
    private String taLpk05; 

    @Column(name = "TA_LPK06", length = 10)
    private String taLpk06; 
}