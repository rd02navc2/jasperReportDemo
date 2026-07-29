package com.beyond.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;

/**
 * Wallet
 * 錢包設定檔（重構版）
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "WALLET")
@DynamicInsert
public class WALLET implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "WALLET_ID", length = 20) // 建議加上 length 定義以符合資料庫規格
    private String walletId;

    @Column(name = "LOCAL_NAME", length = 100)
    private String localName;

}