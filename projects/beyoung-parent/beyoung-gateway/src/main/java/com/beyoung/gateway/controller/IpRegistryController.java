package com.beyoung.gateway.controller;

import com.beyoung.gateway.entity.GatewayIpEntity;
import com.beyoung.gateway.entity.GatewayIpWhitelistEntity; // 確保導入正確的白名單 Entity
import com.beyoung.gateway.repository.GatewayIpRegistryRepository;
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