package com.beyond.gateway.repository;

import com.beyond.gateway.entity.GatewayIpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayIpRegistryRepository extends JpaRepository<GatewayIpEntity, Long> {
    // 依據業務需求，後續可自行擴充如 findByIpAddress 等方法
}
