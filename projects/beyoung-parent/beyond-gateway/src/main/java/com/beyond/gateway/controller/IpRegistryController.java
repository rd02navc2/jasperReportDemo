package com.beyond.gateway.controller;

import com.beyond.gateway.entity.GatewayIpEntity;
import com.beyond.gateway.repository.GatewayIpRegistryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.gateway.route.RouteLocator; 
import org.springframework.context.ApplicationEventPublisher; 
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import java.util.List;

@RestController
@RequestMapping("/admin/gateway")
@Slf4j
public class IpRegistryController {

    @Autowired
    private ApplicationEventPublisher publisher;
    
    @Autowired
    private Scheduler jpaScheduler; // 注入大小為 50 的專屬執行緒池
    
    @Autowired
    private RouteLocator routeLocator; // 網關快取定位器

    @Autowired
    private GatewayIpRegistryRepository ipRegistryRepository; // 修正：統一使用註冊表專用儲存庫
    

    /**
     * 1. 獲取全量 IP 註冊/足跡清單 (包含安全白名單)
     */
    @GetMapping("/ip-registry")
    public Mono<ResponseEntity<List<GatewayIpEntity>>> getAllIpRegistry() {
        // 回傳型態與內部撈取型態完全一致，均為 GatewayIpEntity
        return Mono.fromCallable(() -> ipRegistryRepository.findAll())
                .subscribeOn(jpaScheduler)
                .map(ResponseEntity::ok);
    }


}