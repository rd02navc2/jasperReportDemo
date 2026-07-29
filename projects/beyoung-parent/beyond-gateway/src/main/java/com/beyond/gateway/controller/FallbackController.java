package com.beyond.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 熔斷降級控制器
 * 當新微服務不可用時，此 Controller 接收 Circuit Breaker 的 fallback 請求，
 * 並回傳明確的降級回應。
 * 在實際生產中，可在此處串接舊系統或回傳靜態替代資料。
 */
@RestController
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    /**
     * 會員服務降級端點
     * 當 beyond-member (8081) 出現異常時，Gateway Circuit Breaker 會將請求
     * forward 到此路徑，之後再由 Gateway 路由規則將流量轉向舊系統 (8085)。
     */
    @RequestMapping("/fallback/legacy-member")
    public Mono<ResponseEntity<Map<String, Object>>> memberFallback() {
        log.warn("[CircuitBreaker] 會員服務異常，已自動降級至舊系統，時間: {}", LocalDateTime.now());

        Map<String, Object> body = Map.of(
            "status",    "FALLBACK",
            "message",   "會員服務暫時不可用，系統已自動切換至舊版服務。",
            "timestamp", LocalDateTime.now().toString()
        );

        return Mono.just(
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("X-Fallback", "true")
                .header("X-Fallback-Service", "legacy-member")
                .body(body)
        );
    }

    /**
     * 交易服務降級端點（預留擴充）
     */
    @RequestMapping("/fallback/legacy-transaction")
    public Mono<ResponseEntity<Map<String, Object>>> transactionFallback() {
        log.warn("[CircuitBreaker] 交易服務異常，已自動降級至舊系統，時間: {}", LocalDateTime.now());

        Map<String, Object> body = Map.of(
            "status",    "FALLBACK",
            "message",   "交易服務暫時不可用，系統已自動切換至舊版服務。",
            "timestamp", LocalDateTime.now().toString()
        );

        return Mono.just(
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("X-Fallback", "true")
                .header("X-Fallback-Service", "legacy-transaction")
                .body(body)
        );
    }
}
