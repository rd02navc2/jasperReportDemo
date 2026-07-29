package com.beyond.gateway.repository;

import com.beyond.gateway.entity.GatewayLoginFailureLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayLoginFailureLogRepository extends JpaRepository<GatewayLoginFailureLogEntity, Long> {
    // 繼承 JpaRepository 後，預設就自帶了 .save() 方法，不需另外寫程式碼
}