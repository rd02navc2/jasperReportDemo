package com.beyoung.gateway.controller;

import com.beyoung.gateway.entity.GatewayLoginFailureLogEntity;
import com.beyoung.gateway.repository.GatewayLoginFailureLogRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

@RestController
@RequestMapping("/admin/gateway")
@Slf4j
public class FailureLogController {

    @Autowired
    private Scheduler jpaScheduler;

    @Autowired
    private GatewayLoginFailureLogRepository failureLogRepository;

    /**
     * 獲取全量登入失敗歷史紀錄
     * Table: gateway_login_failure_log
     */
    @GetMapping("/failure-logs")
    public Mono<ResponseEntity<List<GatewayLoginFailureLogEntity>>> getAllFailureLogs() {
        return Mono.fromCallable(() -> failureLogRepository.findAll())
                .subscribeOn(jpaScheduler)
                .map(ResponseEntity::ok);
    }
}