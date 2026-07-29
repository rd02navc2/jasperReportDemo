package com.beyoung.surrounding.dc.entity; // 💡 對齊新專案套件路徑

import java.io.Serializable;
import jakarta.persistence.*; // 升級為 Spring Boot 3 的 jakarta 規範
import lombok.*;

@Entity
@Table(name = "VIP_ROOM_UNLIMIT")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VIP_ROOM_UNLIMIT implements Serializable { // 轉換為標準大駝峰
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "user_id")
    private String userId; // 使用標準駝峰命名，並對齊資料庫底線欄位
    
    @Column(name = "user_name")
    private String userName;

    // 手動補回無參建構子，防止 Hibernate 反射實例化失敗
    public VIP_ROOM_UNLIMIT() {
    }
    
}