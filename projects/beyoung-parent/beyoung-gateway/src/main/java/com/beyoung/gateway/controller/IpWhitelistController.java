package com.beyoung.gateway.controller;

import com.beyoung.gateway.entity.GatewayIpWhitelistEntity; // 確保導入正確的白名單 Entity
import com.beyoung.gateway.repository.GatewayIpWhitelistRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.gateway.route.RouteLocator; 
import org.springframework.context.ApplicationEventPublisher; 
import org.springframework.cloud.gateway.event.RefreshRoutesEvent; 
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/gateway")
@Slf4j
public class IpWhitelistController {

    @Autowired
    private ApplicationEventPublisher publisher;
    
    @Autowired
    private Scheduler jpaScheduler; // 注入大小為 50 的專屬執行緒池
    
    @Autowired
    private RouteLocator routeLocator; // 網關快取定位器

    @Autowired
    private GatewayIpWhitelistRepository whitelistRepository; // 修正：統一使用白名單專用儲存庫
    
    /**
     * 1. 獲取全量白名單 IP 清單
     */
    @GetMapping("/ip-whitelist")
    public Mono<ResponseEntity<List<GatewayIpWhitelistEntity>>> getAllWhitelist() {
        // 修正：回傳型態與內部撈取型態一致，均為 GatewayIpWhitelistEntity
        return Mono.fromCallable(() -> whitelistRepository.findAll())
                .subscribeOn(jpaScheduler) 
                .map(ResponseEntity::ok);
    }

    /**
     * 2. 手動持久化新增白名單 IP
     */
    @PostMapping("/ip-whitelist")
    public Mono<ResponseEntity<Object>> addWhitelist(@RequestBody GatewayIpWhitelistEntity ipEntity) {
        return Mono.fromCallable(() -> {
            // 修正：使用 whitelistRepository 進行重複性檢查與寫入
            var existing = whitelistRepository.findByIpAddress(ipEntity.getIpAddress());
            if (existing.isPresent()) {
                throw new IllegalArgumentException("該 IP 地址已存在於白名單資料庫中");
            }
            
            // 修正：白名單表不處理 loginCount，若您的白名單表確實有此欄位再保留
            // ipEntity.setLoginCount(0); 
            
            return whitelistRepository.save(ipEntity);
        })
        .subscribeOn(jpaScheduler) 
        .publishOn(jpaScheduler)   
        .flatMap(saved -> Mono.fromRunnable(() -> {
            log.info("白名單 IP {} 成功寫入 MySQL (gateway_ip_whitelist)，發送刷新事件...", saved.getIpAddress());
            publisher.publishEvent(new RefreshRoutesEvent(this));
        }).thenReturn(saved))
        .thenMany(routeLocator.getRoutes())
        .collectList()
        .map(routes -> ResponseEntity.ok((Object) createResponse(200, "安全白名單 IP 成功寫入 MySQL 並同步生效", null)))
        .onErrorResume(ex -> {
            log.error("新增白名單 IP 失敗: ", ex);
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(400, ex.getMessage(), null)));
        });
    }

    /**
     * 3. 動態切換白名單防護生效開關
     */
    @PatchMapping("/ip-whitelist/{id}/status")
    public Mono<ResponseEntity<Object>> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return Mono.fromCallable(() -> {
            // 修正：從白名單表查詢
            GatewayIpWhitelistEntity entity = whitelistRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("找不到該識別 ID 的白名單配置"));
            
            if (body.containsKey("enabled")) {
                Object enabledVal = body.get("enabled");
                if (enabledVal instanceof Boolean) {
                    entity.setEnabled((Boolean) enabledVal ? 1 : 0);
                } else if (enabledVal instanceof Number) {
                    entity.setEnabled(((Number) enabledVal).intValue());
                }
            }
            return whitelistRepository.save(entity);
        })
        .subscribeOn(jpaScheduler) 
        .publishOn(jpaScheduler)   
        .flatMap(updated -> Mono.fromRunnable(() -> {
            log.info("白名單 IP {} 生效狀態已修改，發送刷新事件...", updated.getIpAddress());
            publisher.publishEvent(new RefreshRoutesEvent(this));
        }))
        .thenMany(routeLocator.getRoutes()) 
        .collectList()
        .map(routes -> ResponseEntity.ok((Object) createResponse(200, "白名單狀態修改成功並已同步快取", null)))
        .onErrorResume(ex -> {
            log.error("修改白名單狀態失敗, ID: {}", id, ex);
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(400, ex.getMessage(), null)));
        });
    }
    
    /**
     * 4. 移除指定 IP 持久化配置
     */
    @DeleteMapping("/ip-whitelist/{id}")
    public Mono<ResponseEntity<Object>> deleteWhitelist(@PathVariable Long id) {
        return Mono.fromCallable(() -> {
            // 修正：對白名單表進行刪除
            if (!whitelistRepository.existsById(id)) {
                throw new IllegalArgumentException("找不到該白名單 IP 的對應配置項目");
            }
            whitelistRepository.deleteById(id);
            return id;
        })
        .subscribeOn(jpaScheduler) 
        .publishOn(jpaScheduler)   
        .flatMap(deletedId -> Mono.fromRunnable(() -> {
            log.info("白名單 ID {} 已自資料庫移除，發送刷新事件...", deletedId);
            publisher.publishEvent(new RefreshRoutesEvent(this));
        }))
        .thenMany(routeLocator.getRoutes()) 
        .collectList()
        .map(routes -> ResponseEntity.ok((Object) createResponse(200, "該 IP 配置已自白名單卸載移除並同步失效", null)))
        .onErrorResume(ex -> {
            log.error("刪除白名單失敗, ID: {}", id, ex);
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createResponse(404, ex.getMessage(), null)));
        });
    }
    
    private Map<String, Object> createResponse(int code, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        if (data != null) response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}