package com.beyoung.gateway.repository;

import com.beyoung.gateway.entity.GatewayRouteEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 使用 MySQL 實作的動態路由儲存庫
 * 解決了 WebFlux 線程阻塞問題，並支援自動刷新 Cache
 */
@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final Logger log = LoggerFactory.getLogger(MysqlRouteDefinitionRepository.class);

    @Autowired
    private GatewayRouteRepository jpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 讀取從 MySQL 載入所有 Gateway Route
     * 透過 Schedulers.boundedElastic() 確保不堵塞 Netty 線程
     */
    /*
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Mono.fromCallable(() -> jpaRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic()) // 切換到專門處理阻塞 IO 的線程池
                .flatMapMany(Flux::fromIterable)
                //dc-
                // 核心修正：只保留資料庫中 enabled 欄位為 true 的路由實體
                .filter(entity -> Boolean.TRUE.equals(entity.getEnabled()))
                .map(entity -> {
                    try {
                        return objectMapper.readValue(entity.getRouteDefinition(), RouteDefinition.class);
                    } catch (Exception e) {
                        log.error("路由 JSON 解析失敗, ID: {}", entity.getRouteId(), e);
                        // 返回一個空的物件避免 Flux 中斷，或根據需求拋出異常
                        return new RouteDefinition();
                    }
                })
                .filter(route -> route.getId() != null);
    }
*/
    //dc-
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        // 核心修正：改用資料庫層級過濾（只撈 enabled=true），並加上 Order 排序避免網關匹配錯亂
        return Mono.fromCallable(() -> jpaRepository.findByEnabledTrueOrderByRouteOrderAsc()) 
                .subscribeOn(Schedulers.boundedElastic()) // 確保阻塞的 JPA 查詢執行在彈性執行緒池
                .flatMapMany(Flux::fromIterable)
                .map(entity -> {
                    try {
                        return objectMapper.readValue(entity.getRouteDefinition(), RouteDefinition.class);
                    } catch (Exception e) {
                        log.error("路由 JSON 解析失敗, ID: {}", entity.getRouteId(), e);
                        // 回傳一個空物件，交給下方的 filter 過濾掉，避免單一路由解析失敗導致整個網關掛載失敗
                        return new RouteDefinition();
                    }
                })
                .filter(route -> route.getId() != null); // 過濾掉上面解析失敗的無效路由
    }    
    
    

    /**
     * 儲存/更新新增或更新路由，完成後自動刷新 Gateway 內部快取
     */
    
    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(definition -> 
            Mono.fromCallable(() -> {
                try {
                    log.info("準備儲存/更新路由至資料庫: {}", definition.getId());

                    // 1. 先查出既有資料，以支援「更新」邏輯並保留其餘欄位（如 content）
                    GatewayRouteEntity entity = jpaRepository.findById(definition.getId())
                            .orElse(new GatewayRouteEntity());

                    // 2. 基本欄位設定
                    entity.setRouteId(definition.getId());
                    entity.setRouteOrder(definition.getOrder());
                    //dc-
                    if (definition.getUri() != null) {
                        entity.setUri(definition.getUri().toString()); // 強制轉成字串存入外層欄位
                    } else {
                        entity.setUri(""); 
                    }
                    
                    // 3. 修改外部實體的 Boolean 欄位（預設啟用 true）
                    entity.setEnabled(true); 

                    // 4. 特殊邏輯：如果是 Order 20 (舊系統)，注入 Session 轉換的 Filter 定義
                    // if (definition.getOrder() != null && definition.getOrder() == 20) {
                    //    entity.setContent("AddRequestHeader=X-Legacy-Session, #{session.id}");
                    //    log.info("偵測到舊系統路由 (Order 20)，自動注入 Session 轉換 Filter");
                    // }

                    // 5. 第二組設定強制將 "enabled": true 塞進 route_definition 的 JSON 字串中
                    // 先將傳入的 definition 轉成 ObjectNode
                    com.fasterxml.jackson.databind.node.ObjectNode jsonNode = 
                            (com.fasterxml.jackson.databind.node.ObjectNode) objectMapper.valueToTree(definition);
                    
                    // 在 JSON 肚子裡強行塞入 "enabled": true 屬性
                    jsonNode.put("enabled", true);
                    
                    // 將帶有 enabled 的全新 JSON 轉回字串存入 TEXT 欄位
                    entity.setRouteDefinition(objectMapper.writeValueAsString(jsonNode));
                   
                    // 6. 持久化到 MySQL
                    GatewayRouteEntity saved = jpaRepository.save(entity);
                    log.info("路由已成功儲存至資料庫: ID={}, 外部Enabled={}, JSON內部已同步新增", 
                            saved.getRouteId(), saved.getEnabled());
                    return definition;
                } catch (Exception e) {
                    log.error("路由儲存失敗: {}", definition.getId(), e);
                    throw new RuntimeException("資料庫寫入錯誤", e);
                }
            }).subscribeOn(Schedulers.boundedElastic()) // 必須在彈性線程池執行阻塞儲存
        )
        //dc-
        		.doOnSuccess(definition -> {
        		    // 切到單獨的 scheduler 避免佔用 boundedElastic
        		    Schedulers.boundedElastic().schedule(() ->
        		        publisher.publishEvent(new RefreshRoutesEvent(this))
        		    );
        		})
        .then();
    }
    
    
    
    /**
     * 刪除根據 ID 刪除路由，完成後自動刷新快取
     */
    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> 
            Mono.fromCallable(() -> {
            	//dc-
            	GatewayRouteEntity entity = jpaRepository.findById(id)
            	        .orElseThrow(() -> new NotFoundException("找不到路由 ID: " + id));
            	jpaRepository.delete(entity);
                log.info("路由已從資料庫刪除: {}", id);
                return id;
               
            })
            .subscribeOn(Schedulers.boundedElastic())
        )
        .doOnSuccess(id -> {
            // 刪除成功後刷新快取
            publisher.publishEvent(new RefreshRoutesEvent(this));
            log.info("刪除成功，Gateway 快取已刷新");
        })
        .then();
    }



    //dc-
    public Flux<GatewayRouteEntity> findAllEntitiesDb() {
        return Flux.defer(() -> Flux.fromIterable(jpaRepository.findAll()))
                   .subscribeOn(Schedulers.boundedElastic());
    }
}