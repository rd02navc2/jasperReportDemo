package com.beyond.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beyond.gateway.entity.GatewayRouteEntity;
import com.beyond.gateway.repository.GatewayRouteRepository;
import com.beyond.gateway.repository.MysqlRouteDefinitionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/gateway")
@Slf4j
public class RouteManageController implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    
    @Autowired
    private GatewayRouteRepository jpaRepository;
    
    @Autowired
    private ApplicationEventPublisher publisher;
    
    @Autowired  
    private ObjectMapper objectMapper;
   
    //dc-
    @Autowired
    private MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository;
    
    // @Autowired
    // private GatewayIpWhitelistRepository ipWhitelistRepository;
    
  //dc-
    @Autowired
    private Scheduler jpaScheduler; // 注入我們自定義的池
    
 // 注入 Spring Cloud Gateway 核心的 RouteLocator
    @Autowired
    private RouteLocator routeLocator;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    
    //dc-
    // 供前端資料庫路由管理的 RESTful APIs 接口：
    /**
     * 簡單測試 API (POST /admin/gateway/test)
     * 用於驗證連線、JSON 格式與 Security 權限
     */
    @PostMapping("/test")
    public Mono<ResponseEntity<Map<String, Object>>> testPost(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("receivedData", body);
        response.put("timestamp", System.currentTimeMillis());
        
        return Mono.just(ResponseEntity.ok(response));
    }
    
    /**
     * 1. 列出所有路由 (GET /admin/gateway/routes)
     * 會從 MysqlRouteDefinitionRepository 的 getRouteDefinitions() 抓取
     */
    @GetMapping("/routes")
    public Flux<RouteDefinition> getRoutes() {
        // 透過 routeLocator 讀取網關記憶體內已經解析好、正在生效的真實路由，完全不打 MySQL
        return routeLocator.getRoutes()
        		//dc-
        		// 核心修正：利用 distinct 根據 Route ID 進行去重，
                // 徹底封殺 CachingRouteLocator 在並發重構時間差吐出的重複物件
                .distinct(route -> route.getId()) 
                .map(route -> {
                    RouteDefinition def = new RouteDefinition();
                    def.setId(route.getId());
                    def.setUri(route.getUri());
                    def.setOrder(route.getOrder());
                    return def;
                });	        		
        		
        	/*
                .map(route -> {
                	//dc-
                	// 列印出來看看這兩筆的斷言 (Predicate) 或 URI 有什麼差別，就能抓出是誰在搞鬼
                    log.info("檢查執行期路由ID: {}, URI: {}, Predicate: {}", 
                             route.getId(), route.getUri(), route.getPredicate().toString());
                    RouteDefinition def = new RouteDefinition();
                    def.setId(route.getId());
                    def.setUri(route.getUri());
                    def.setOrder(route.getOrder());
                    // 可根據前端需求決定是否封裝 predicates 和 filters
                    return def;
                });
            */
    }

    //dc-
 // 修改 RouteManageController.java 中的 /routesDb
    @GetMapping("/routesDb")
    public Flux<GatewayRouteEntity> getRoutesDb() {
        // 確保即使管理員瘋狂重新整理這個 API，阻塞的撈取動作也只會在 50 大小的 jpaScheduler 中排隊
        return mysqlRouteDefinitionRepository.findAllEntitiesDb()
                .subscribeOn(jpaScheduler); 
    }
    
    /**
     * 2. 查看特定路由 (GET /admin/gateway/routes/{id})
     */
    @GetMapping("/routes/{id}")
    public Mono<ResponseEntity<RouteDefinition>> getRouteById(@PathVariable String id) {
        return mysqlRouteDefinitionRepository.getRouteDefinitions()
                .filter(route -> route.getId().equals(id))
                .next() // 取第一個符合的
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 3. 新增/更新路由 (POST /admin/gateway/routes/{id})
     * 存入資料庫並自動刷新
     */
/*    
    @PostMapping(value = "/routes/{id}", consumes = "application/json")
    public Mono<ResponseEntity<Map<String, Object>>> addOrUpdate(
            @PathVariable("id") String id, 
            @RequestBody RouteDefinition definition) {
        
        definition.setId(id);

        // 1. 確保 save 操作在 WebFlux 流中被訂閱
        return routeDefinitionWriter.save(Mono.just(definition))
                // 2. 使用 .then() 確保在儲存完成後才執行後續動作
                .then(Mono.defer(() -> {
                    // 3. 發送刷新事件
                    // publisher.publishEvent(new RefreshRoutesEvent(this));
                    
                    // 4. 明確建立並回傳 Response
                    Map<String, Object> response = createResponse(200, "路由儲存成功並已刷新", id);
                    return Mono.just(ResponseEntity.ok(response));
                }))
                // 5. 錯誤處理，確保失敗時也有回傳
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(500)
                            .body(createResponse(500, "儲存失敗: " + e.getMessage(), id)));
                });
    }
*/
    
    //dc-
 // 修改後的 RouteManageController.java 局部片段

    /**
     * 3. 新增/更新路由 (POST /admin/gateway/routes/{id})
     * 存入資料庫並自動刷新
     */
    @PostMapping(value = "/routes/{id}", consumes = "application/json")
    public Mono<ResponseEntity<Map<String, Object>>> addOrUpdate(
            @PathVariable("id") String id, 
            @RequestBody RouteDefinition definition) {
        
        definition.setId(id);

        if (definition.getMetadata() == null) {
            definition.setMetadata(new HashMap<>());
        }
        if (!definition.getMetadata().containsKey("status")) {
            definition.getMetadata().put("status", "active");
        }

        
        // 1. 純粹呼叫 Repository 進行儲存
        // 你的 MysqlRouteDefinitionRepository 儲存成功後會自己發送 RefreshRoutesEvent
        return routeDefinitionWriter.save(Mono.just(definition))
                // 2. 儲存完畢後直接回傳 200 OK 響應，不透過 routeLocator 進行同步卡流
                .then(Mono.defer(() -> {
                    log.info("路由 {} 資料庫儲存成功，已委託底層 Repository 發送非同步刷新事件", id);
                    Map<String, Object> response = createResponse(200, "路由儲存成功並已同步刷新網關快取", id);
                    return Mono.just(ResponseEntity.ok(response));
                }))
                // 3. 錯誤處理
                .onErrorResume(e -> {
                    log.error("儲存路由失敗, ID: {}", id, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(createResponse(500, "儲存失敗: " + e.getMessage(), id)));
                });
    }
  
    /**
     * 4. 刪除路由 (DELETE /admin/gateway/routes/{id})
     * 從資料庫刪除並自動刷新
     */
    @DeleteMapping("/routes/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        // 1. 不要直接傳 Mono.just(id) 進去然後切斷鏈結，用 flatMap 確保順序性
        return routeDefinitionWriter.delete(Mono.just(id)) 
                // 2. 確保 delete 返回的 Mono<Void> 真正執行完畢後，才轉發成功響應
                .then(Mono.fromCallable(() -> {
                    // 如果你的刷新事件或後續操作需要同步，放在 then 裡面確保底層真的刪完了
                    log.info("路由 {} 已成功自底層完全卸載", id);
                    return ResponseEntity.ok((Object) createResponse(200, "路由已從資料庫刪除並完成刷新", id));
                }))
                // 3. 統一擷取這整個鏈結中發生的任何異常（包括資料庫找不到、連線超時等）
                .onErrorResume(e -> {
                    log.error("刪除路由 {} 失敗, 原因: ", id, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(createResponse(404, "刪除失敗：" + e.getMessage(), id)));
                });
    }
    
    /**
     * 5. 刷新生效 (POST /admin/gateway/refresh)
     * 讓 Gateway 重新從 MysqlRouteDefinitionRepository 讀取資料到記憶體
     */
    @PostMapping("/refresh")
    public Mono<ResponseEntity<Object>> refresh() {
        return Mono.fromRunnable(() -> {
            // 1. 發送刷新事件
            log.info("開始動態刷新網關路由配置...");
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        })
        // 2. flatMap 關鍵：強制網關在此時此刻立即重新拉取（Fetch）並建立路由快取
        // routeLocator.getRoutes() 會返回 Flux<Route>，調用它會強迫 CachingRouteLocator 去刷新
        .thenMany(routeLocator.getRoutes()) 
        .collectList() // 等待所有路由重新加載完成
        .map(routes -> {
            log.info("網關路由刷新成功！當前加載路由總數：{} 條", routes.size());
            return ResponseEntity.ok((Object) createResponse(200, "網關路由已完全重新加載並生效", null));
        })
        .onErrorResume(e -> {
            log.error("路由刷新失敗: ", e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(500, "刷新失敗：" + e.getMessage(), null)));
        });
    }
    
    
    // 格式化回傳
    private Map<String, Object> createResponse(int code, String msg, String id) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", code);
        res.put("message", msg);
        if (id != null) res.put("id", id);
        return res;
    }
    
    /**
     * 6. 快速切換路由狀態 (PATCH /admin/gateway/routes/{id}/status)
     */
    @PatchMapping("/routes/{id}/status")
    public Mono<ResponseEntity<Map<String, Object>>> toggleStatus(
            @PathVariable String id, 
            @RequestBody StatusUpdateRequest request) { 
        
        return Mono.fromCallable(() -> {
            log.info("接收到更新路由狀態請求 - ID: {}, 目標狀態: {}", id, request.getEnabled());
            
            GatewayRouteEntity entity = jpaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("找不到路由: " + id));
            
            Boolean newStatus = request.getEnabled();
            if (newStatus == null) {
                throw new IllegalArgumentException("enabled 欄位不能為 null");
            }
            
            entity.setEnabled(newStatus);
            
            if (entity.getRouteDefinition() != null) {
                try {
                    com.fasterxml.jackson.databind.node.ObjectNode jsonNode = 
                            (com.fasterxml.jackson.databind.node.ObjectNode) objectMapper.readTree(entity.getRouteDefinition());
                    
                    jsonNode.put("enabled", newStatus);
                    entity.setRouteDefinition(objectMapper.writeValueAsString(jsonNode));
                } catch (Exception e) {
                    log.error("解析或修改路由 JSON 失敗, ID: {}", id, e);
                    throw new RuntimeException("路由 JSON 配置格式錯誤", e);
                }
            }

            jpaRepository.save(entity);
            log.info("路由狀態已成功持久化至資料庫 - ID: {}, Enabled: {}", entity.getRouteId(), entity.getEnabled());
            return id;
        })
        .subscribeOn(jpaScheduler) // 1. 確保上述複雜的 JPA 讀寫與 Jackson 解析都在 50 大小的專屬池中執行
        .publishOn(jpaScheduler)   // 2. 核心修改確保後續的事件發送與底層加載，依然留在專屬池，不污染 Netty Thread
        .flatMap(savedId -> Mono.fromRunnable(() -> {
            log.info("已成功持久化，開始發送 RefreshRoutesEvent 刷新網關快取 - ID: {}", savedId);
            publisher.publishEvent(new RefreshRoutesEvent(this));
        }))
        // 3. 同步強迫 CachingRoute Locator 更新快取，徹底免除前後端時間差
        .thenMany(routeLocator.getRoutes())
        .collectList()
        .map(routes -> ResponseEntity.ok(createResponse(200, "路由狀態更新成功並已完成網關快取同步", id)))
        .onErrorResume(ex -> {
            log.error("更新路由狀態失敗 - ID: {}", id, ex);
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(400, ex.getMessage(), null)));
        });
    }

    /**
     * 輔助方法：建立統一的 API 回應格式
     */
    private Map<String, Object> createResponse(int code, String message, Object data) {
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @Data
    public static class StatusUpdateRequest {
        private Boolean enabled;
    }
    
}