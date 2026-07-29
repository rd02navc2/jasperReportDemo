package com.beyond.gateway.repository;

import com.beyond.gateway.entity.GatewayIpWhitelistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GatewayIpWhitelistRepository extends JpaRepository<GatewayIpWhitelistEntity, Long> {
    List<GatewayIpWhitelistEntity> findByEnabled(Integer enabled);
    
    Optional<GatewayIpWhitelistEntity> findByIpAddress(String ipAddress);
}
