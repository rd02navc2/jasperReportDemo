package com.beyoung.gateway.repository;

import com.beyoung.gateway.entity.GatewayIpEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GatewayIpRepository extends JpaRepository<GatewayIpEntity, Long> {
    
    Optional<GatewayIpEntity> findByIpAddress(String ipAddress);

    /**
     * 高併發下安全的原子操作：當 IP 通過驗證或觸發登入時，登入次數 + 1
     */
    //dc-
    @Transactional // 缺少這個會導致 TransactionRequiredException
    @Modifying
    @Query("UPDATE GatewayIpEntity g SET g.loginCount = g.loginCount + 1, g.lastAccessTime = CURRENT_TIMESTAMP WHERE g.ipAddress = :ipAddress")
    int incrementLoginCount(@Param("ipAddress") String ipAddress);
}