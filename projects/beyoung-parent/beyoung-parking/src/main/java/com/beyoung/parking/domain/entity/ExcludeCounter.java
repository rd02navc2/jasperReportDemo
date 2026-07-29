package com.beyoung.parking.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 排除專櫃實體類別 
 * 對應資料庫：parking_sf
 * 對應資料表：exclude_counter_file
 */
@Entity
@Table(name = "exclude_counter_file", schema = "parking_sf")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcludeCounter {

	/**
     * 專櫃代碼（主鍵）
     */
    @Id
    @Column(name = "s_counter_id", length = 50)
    private String counterId;

    /**
     * 專櫃名稱
     */
    @Column(name = "s_counter_name", length = 100)
    private String counterName;

    @Column(name = "create_user_id", length = 50)
    private String createUserId;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}