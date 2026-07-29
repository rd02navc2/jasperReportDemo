package com.beyoung.member.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LPJ_FILE")
@IdClass(LpjFileId.class)
public class LpjFile {

    @Id
    @Column(name = "LPJ01", length = 50)
    private String lpj01; // 會員ID

    @Column(name = "LPJ02", length = 10)
    private String lpj02; // 卡片等級 (000/TEMP/APP...) 業務狀態標籤 (Status Flag)，用於定義會員目前處於哪一個「生命週期」階段。"000"：代表「待正式化」的臨時會員狀態。只有處於此狀態的臨時會員，才允許透過 doFormal API 轉換為正式會員

    @Column(name = "LPJ03", length = 50)
    private String lpj03; // 卡號

    @Column(name = "LPJ04")
    private LocalDateTime lpj04; // 建立日期 (已改為 LocalDateTime 以配合 now)

    @Column(name = "LPJ06")
    private Integer lpj06; // 累積消費金額

    @Column(name = "LPJ07")
    private Integer lpj07; // 累積點數

    @Id
    @Column(name = "LPJ09", length = 5)
    private String lpj09; // 卡狀態

    @Column(name = "LPJ12")
    private Integer lpj12;

    @Column(name = "LPJ13")
    private Integer lpj13;

    @Column(name = "LPJ14")
    private Integer lpj14;

    @Column(name = "LPJ15")
    private Integer lpj15;

    @Column(name = "LPJ16", length = 5)
    private String lpj16; // 主卡旗標

    @Column(name = "LPJ17", length = 10)
    private String lpj17; // 店別

    @Column(name = "LPJ18")
    private LocalDateTime lpj18; // 建立日期2 (已改為 LocalDateTime 以配合 now)

    @Column(name = "LPJ19", length = 20)
    private String lpj19; // 建立人員

    @Column(name = "LPJPOS", length = 5)
    private String lpjpos; 
  
    @Column(name = "TA_LPJ01")
    private Integer taLpj01;

    @Column(name = "TA_LPJ02")
    private Integer taLpj02;

    @Column(name = "TA_LPJ03")
    private Integer taLpj03;

    @Column(name = "TA_LPJ04", length = 5)
    private String taLpj04; // 主卡旗標 Y/N 主卡否
    
    @Column(name = "TA_LPJ05")
    private String taLpj05; // 確保這裡的名稱與 JPQL 中的一致
    
}