package com.beyond.surrounding.ec.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EcTcLrjFile")
@Table(name = "TC_LRJ_FILE")
@IdClass(TC_LRJ_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor  // JPA 規範必備無參構造函數
@AllArgsConstructor
@Builder
public class TC_LRJ_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    //  修正：為所有複合主鍵欄位指定合理的長度，避免預設 255 導致索引過長
    
    @Id
    @Column(name = "TC_LRJ01", length = 40) // 假設為單號
    private String TC_LRJ01;

    @Id
    @Column(name = "TC_LRJ02", length = 10) // 假設為項次
    private String TC_LRJ02;

    @Id
    @Column(name = "TC_LRJ09", length = 10) 
    private String TC_LRJ09;

    @Id
    @Column(name = "TC_LRJPLANT", length = 20) // 廠區代碼（原報錯欄位，縮短至 20 完美解決）
    private String TC_LRJPLANT;

    @Column(name = "TC_LRJ03")
    private Double TC_LRJ03;

    @Column(name = "TC_LRJ04")
    private Double TC_LRJ04;

    @Column(name = "TC_LRJ05")
    private Double TC_LRJ05;

    @Column(name = "TC_LRJ06")
    private String TC_LRJ06;

    @Column(name = "TC_LRJ07")
    private String TC_LRJ07;

    @Column(name = "TC_LRJ08")
    private String TC_LRJ08;

    @Column(name = "TC_LRJACTI", length = 1) // 狀態（如 Y/N）通常也可以給長度 1
    private String TC_LRJACTI;
    
}