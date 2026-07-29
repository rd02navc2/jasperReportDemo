package com.beyoung.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ReactiveConfig {

    @Bean
    public Scheduler jpaScheduler() {
        // 讓執行緒池的最大執行緒數 精準等於 你的 Hikari maximum-pool-size (50)
        // 這樣最多隻會有 50 個執行緒去搶 50 個連線，絕對不會有人因為排隊而超時！
        return Schedulers.newBoundedElastic(50, 10000, "jpa-tasks");
    }
}