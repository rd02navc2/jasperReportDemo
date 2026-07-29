package com.beyoung.bonus.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BONUS_LOG", schema = "bonus_sf")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 依據資料庫種類可改用 SEQUENCE 
    @Column(name = "id") // 假設您有主鍵 id 自增欄位，若沒有可視資料庫現況調整
    private Long id;

    @Column(name = "center", length = 100)
    private String center;

    @Column(name = "counter_id", length = 50)
    private String counterId;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "card_no", length = 100)
    private String cardNo;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "access_date")
    private LocalDateTime accessDate; // Java 21 推薦使用新版時區時間
    
    // ==========================================
    // 預設到期日為永久有效 (9999-12-31)
    // ==========================================
    @Builder.Default
    @Column(name = "expiry_date")
    private LocalDate expiryDate = LocalDate.of(9999, 12, 31);

    @Column(name = "access_id", length = 100)
    private String accessId;
}