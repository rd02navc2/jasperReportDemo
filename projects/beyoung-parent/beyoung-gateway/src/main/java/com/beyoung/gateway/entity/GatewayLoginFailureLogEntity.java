package com.beyoung.gateway.entity;

import lombok.Data;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity 
@Table(name = "gateway_login_failure_log") // 對應資料庫的表名
public class GatewayLoginFailureLogEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動遞增主鍵
    private Long id;
	
	@Column(name = "refer_ip", nullable = false, length = 45)
    private String referIp;         // 呼叫者的真實來源 IP (用戶的 IP)
	
	@Column(name = "gateway_node_ip", length = 45)
    private String gatewayNodeIp;   // 選擇性：如果是多台網關高可用架構，可記錄是哪一台網關處理的
	
	@Column(name = "attempt_username", length = 100)
    private String attemptUsername; // 嘗試登入的帳號
	
	@Column(name = "failure_time", nullable = false)
    private LocalDateTime failureTime;
	
	@Column(name = "failure_reason", length = 255)
    private String failureReason;   // 失敗原因或狀態碼
}