package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "SPOS_IMA_FILE")
@Table(name = "IMA_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IMA_FILE implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "IMA01")
    private String IMA01;     //  料件編號 (主鍵)
    
    @Column(name = "IMA02")
    private String IMA02;     //  品名/規格
    
    @Column(name = "IMA15")
    private String IMA15;     //  常用計量單位
    
    @Column(name = "IMA127")
    private Double IMA127;   //  數值欄位 (例如：標準售價/成本)
    
    @Column(name = "IMA128")
    private Double IMA128;   //  數值欄位
    
    // 🚀 所有手動的 Getter 和 Setter 已經透過上方的 @Getter @Setter 完美取代，直接刪除即可！
}