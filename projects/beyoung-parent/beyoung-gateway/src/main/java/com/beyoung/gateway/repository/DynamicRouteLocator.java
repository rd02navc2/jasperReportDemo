package com.beyoung.gateway.repository;

import com.beyoung.gateway.entity.GatewayRouteEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
public class DynamicRouteLocator implements RouteDefinitionLocator {

    private final GatewayRouteRepository jpaRepository;
    private final ObjectMapper objectMapper;

    public DynamicRouteLocator(GatewayRouteRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Mono.fromCallable(() -> {
            // 1. 只從資料庫撈取啟用的路由，實現「N 則消失」的邏輯
            return jpaRepository.findByEnabledOrderByRouteOrderAsc(true);
        })
        .subscribeOn(Schedulers.boundedElastic()) // 確保資料庫查詢不會阻塞 Gateway 主線程
        .flatMapMany(Flux::fromIterable)
        .map(entity -> {
            try {
                // 2. 將資料庫中存放的內容（可能是整個 RouteDefinition 的 JSON）轉換回物件
                // RouteDefinition definition = objectMapper.readValue(entity.getContent(), RouteDefinition.class);
            	// 正確：從 entity.getRouteDefinition() 讀取 JSON 字串，並轉回 RouteDefinition 物件
            	RouteDefinition definition = objectMapper.readValue(entity.getRouteDefinition(), RouteDefinition.class);
                
                // 3. 強制同步資料庫中的 ID 與 Order，避免 JSON 內容與資料庫主鍵不一致
                definition.setId(entity.getRouteId());
                definition.setOrder(entity.getRouteOrder());
                
                return definition;
            } catch (JsonProcessingException e) {
                // 如果解析失敗，輸出錯誤並回傳空物件，隨後會被 filter 過濾掉
                System.err.println("路由解析失敗 ID: " + entity.getRouteId() + ", 錯誤: " + e.getMessage());
                return new RouteDefinition(); 
            }
        })
        .filter(route -> route.getId() != null); // 確保不回傳無效的路由定義
    }
}