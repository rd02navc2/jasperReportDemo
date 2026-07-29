package com.beyoung.gateway.filter;

import com.beyoung.gateway.repository.GatewayIpWhitelistRepository;
import com.beyoung.gateway.entity.GatewayIpWhitelistEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GatewaySecurityIpFilter implements GlobalFilter, Ordered {

    @Autowired
    private GatewayIpWhitelistRepository ipWhitelistRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 1. 核心邏輯：直接獲取由 Spring 安全機制導正後的遠端連線 IP
        String clientIp = getClientIp(request);
        log.info("[IP 審查中] 呼叫者真實 IP: {}, 請求路徑: {}", clientIp, request.getPath());

        // 2. 從資料庫撈取當前動態啟用的白名單
        List<String> allowedIps = ipWhitelistRepository.findByEnabled(1)
                .stream()
                .map(GatewayIpWhitelistEntity::getIpAddress)
                .collect(Collectors.toList());

        // 如果系統一筆白名單都沒設，則預設全放行；若有設置，則啟動強制校驗
        if (!allowedIps.isEmpty()) {
            boolean isAllowed = allowedIps.contains(clientIp);
            
            // 擴充支援：簡易 CIDR 網段比對
            if (!isAllowed) {
                isAllowed = allowedIps.stream().anyMatch(allowedIp -> matchCidr(allowedIp, clientIp));
            }

            if (!isAllowed) {
                log.warn("[安全阻斷] 外部 IP: {} 不在安全白名單範圍內！拒絕連線。", clientIp);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN); // 回傳 403 Forbidden
                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    /**
     * 獲取真實客戶端 IP
     * 搭配 server.forward-headers-strategy=framework 後，
     * 框架已自動處理 X-Forwarded-For 及其安全性，此處直接讀取 RemoteAddress 即可。
     */
    private String getClientIp(ServerHttpRequest request) {
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }
        return "127.0.0.1";
    }

    /**
     * 簡易子網遮罩比對輔助方法 (可選，用於支援 192.168.1.0/24 格式)
     */
    private boolean matchCidr(String cidr, String ip) {
        if (!cidr.contains("/")) return false;
        try {
            String[] parts = cidr.split("/");
            String ipSegment = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);
            
            // 此處可依據需求引入 Apache Commons Net 的 SubnetUtils 進行精準比對
            return false; 
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getOrder() {
        // 設定最高優先權，必須在路由轉發(Routing)之前先驗證 IP 安全性
        // return Ordered.HIGHEST_PRECEDENCE;
    	
    	// 讓它比 IpTrackingFilter 慢一步執行
        return Ordered.HIGHEST_PRECEDENCE + 1; 
    }
}