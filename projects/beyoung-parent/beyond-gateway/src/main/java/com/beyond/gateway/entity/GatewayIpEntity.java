package com.beyond.gateway.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "gateway_ip_registry")
public class GatewayIpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "ip_address", nullable = false, unique = true)
    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("enabled")
    private Integer enabled; // 1=有效, 0=無效

   
    @Column(name = "login_count")
    @JsonProperty("loginCount")
    private Integer loginCount = 0;

    @Column(name = "last_access_time")
    @JsonProperty("lastAccessTime")
    private LocalDateTime lastAccessTime;

    @Column(name = "created_time", updatable = false)
    @JsonProperty("createdTime")
    private LocalDateTime createdTime;
}
