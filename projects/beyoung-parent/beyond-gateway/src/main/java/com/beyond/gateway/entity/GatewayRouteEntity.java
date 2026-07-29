package com.beyond.gateway.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "gateway_routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRouteEntity {

    @Id
    private String routeId;

    
    @Column(name = "route_order")
    @JsonProperty("routeOrder")
    private Integer routeOrder;
    
    // private String enabled;
    @Column(name = "enabled")
    @JsonProperty("enabled")
    private Boolean enabled; // 改為 Boolean
    
    @Column(name = "uri")
    @JsonProperty("uri")
    private String uri;
    
    // private String content; // Session 轉換
    //dc-
    // 當成功切換到 Order 20 (舊系統) 後，在資料庫的 content 欄位裡，為 Order 20 事先寫好 AddRequestHeader 或自定義的 Session 轉換 Filter， 
    // legacy-surrounding 的 filters 欄位中預設加入一個 Header 轉換器，將新系統的 Session 轉為這樣切換過去時，身份驗證才會自動銜接

    @Column(columnDefinition = "TEXT")
    private String routeDefinition; // JSON 字串
    
    
    
    
    
}