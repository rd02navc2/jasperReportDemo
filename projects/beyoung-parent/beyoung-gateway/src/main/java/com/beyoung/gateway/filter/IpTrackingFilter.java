package com.beyoung.gateway.filter;

import com.beyoung.gateway.entity.GatewayLoginFailureLogEntity;
import com.beyoung.gateway.repository.GatewayIpRepository;
import com.beyoung.gateway.repository.GatewayLoginFailureLogRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

@Component
@Slf4j
public class IpTrackingFilter implements GlobalFilter, Ordered {

    @Autowired
    private GatewayIpRepository ipRepository;
    
    // 注入新建立的失敗紀錄 Repository
    @Autowired
    private GatewayLoginFailureLogRepository failureLogRepository;
    
    private static final String GATEWAY_IP;
    
    static {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            ip = "127.0.0.1"; // 備援方案
        }
        GATEWAY_IP = ip;
    }
 
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
       
        String realIp = getClientIp(request);
        String attemptUsername = request.getQueryParams().getFirst("username");
        String safeUsername = (attemptUsername != null) ? attemptUsername : "未知用戶";
        
        
        exchange.getAttributes().put("X-Real-IP", realIp);
        ServerHttpRequest mutatedRequest = request.mutate().header("X-Real-IP", realIp).build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        
        Mono<Void> preProcessing = Mono.fromRunnable(() -> executeDbTracking(realIp))
                .subscribeOn(Schedulers.boundedElastic())
                .then();

        
        return preProcessing.then(
            Mono.defer(() -> chain.filter(mutatedExchange)
                
                .then(Mono.defer(() -> {
                    ServerHttpResponse response = mutatedExchange.getResponse();
                    HttpStatusCode statusCode = response.getStatusCode();

                   
                    if (statusCode != null && (statusCode.value() == 401 || statusCode.value() == 403)) {
                        
                        
                        String safeRealIp = exchange.getAttribute("X-Real-IP");
                        if (safeRealIp == null) {
                            safeRealIp = realIp; 
                        }

                        log.warn("偵測到登入失敗！來源 Refer IP: {}, 帳號: {}, 狀態碼: {}", safeRealIp, safeUsername, statusCode);
                        
                        GatewayLoginFailureLogEntity failureLog = new GatewayLoginFailureLogEntity();
                        failureLog.setReferIp(safeRealIp); 
                        failureLog.setGatewayNodeIp(GATEWAY_IP);    // 網關自己的 IP (例如: 192.168.5.92)
                        failureLog.setFailureTime(LocalDateTime.now());
                        failureLog.setFailureReason("HTTP Status " + statusCode.value());
                        failureLog.setAttemptUsername(safeUsername); 
                        
                        return Mono.fromCallable(() -> failureLogRepository.save(failureLog))
                                   .subscribeOn(Schedulers.boundedElastic())
                                   .then(); // 將 Mono<GatewayLoginFailureLogEntity> 轉回 Mono<Void>
                    }
                    
                    return Mono.empty();
                }))
            )
        );
    }
    
    /**
     * 將足跡 DB 邏輯抽成獨立方法，保持 filter 乾淨
     */
    private void executeDbTracking(String realIp) {
        int updatedRows = ipRepository.incrementLoginCount(realIp);
        if (updatedRows == 0) {
            log.info("偵測到新來源 IP: {}，自動建立足跡紀錄...", realIp);
            try {
                var newIpEntity = new com.beyoung.gateway.entity.GatewayIpEntity();
                newIpEntity.setIpAddress(realIp);
                newIpEntity.setDescription("系統自動偵測並初次建立");
                newIpEntity.setEnabled(1); 
                newIpEntity.setLoginCount(1);
                ipRepository.save(newIpEntity);
            } catch (Exception ex) {
                // 預防高併發時同時 insert 造成的 Unique Constraint 衝突
                ipRepository.incrementLoginCount(realIp);
            }
        }
    }
    
    /**
     * 核心演算法：穿透多層反向代理獲取真實客户端 IP
     */
/*
    private String getClientIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        
        
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多層代理時，第一個 IP 才是真實用戶端 IP
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            return ip;
        }
        ip = request.getHeaders().getFirst("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeaders().getFirst("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        
        // 若皆無代理標頭，則取底層 TCP 連線的遠端位址
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null) {
            return remoteAddress.getAddress().getHostAddress();
        }
        return "127.0.0.1";
    }
*/
    
    /**
     * 解析真實來源 IP (Refer IP) 的核心邏輯
     */
    private String getClientIp(ServerHttpRequest request) {
        // 優先從負載均衡器（如 Nginx, AWS ALB）轉發的 Standard Header 讀取
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        // 如果沒有經過代理，則直接拿 TCP 連線的 Remote Address
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "127.0.0.1";
        }
        // 對於多層代理 (e.g. User -> CDN -> Nginx -> Gateway)，X-Forwarded-For 會是 IP 串，第一個才是最原始的 Refer IP
        return ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
    
    
}
