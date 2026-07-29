package com.beyond.gateway.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "gateway_ip_whitelist")
public class GatewayIpWhitelistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "ip_address", nullable = false, unique = true)
    @JsonProperty("ipAddress")
    private String ipAddress;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "enabled", nullable = false)
    @JsonProperty("enabled")
    private Integer enabled; // 1: 啟用, 0: 停用

    @Column(name = "create_time", insertable = false, updatable = false)
    @JsonProperty("createTime")
    private LocalDateTime createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    @JsonProperty("updateTime")
    private LocalDateTime updateTime;
}
