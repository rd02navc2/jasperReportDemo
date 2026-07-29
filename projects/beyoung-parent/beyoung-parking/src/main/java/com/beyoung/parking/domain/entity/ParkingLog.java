package com.beyoung.parking.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_LOG", schema = "parking_sf")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // member 依據資料庫種類可改用 SEQUENCE 
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

    @Column(name = "point")
    private Integer point;

    @Column(name = "access_date")
    private LocalDateTime accessDate; // member Java 21 推薦使用新版時區時間

    @Column(name = "access_id", length = 100)
    private String accessId;
}