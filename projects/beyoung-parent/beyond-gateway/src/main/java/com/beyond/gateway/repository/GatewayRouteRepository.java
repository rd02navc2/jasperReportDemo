package com.beyond.gateway.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beyond.gateway.entity.GatewayRouteEntity;

@Repository
public interface GatewayRouteRepository extends JpaRepository<GatewayRouteEntity, String> {
	//dc-
	// 只撈取啟用中的路由，並依照 order 排序
    List<GatewayRouteEntity> findAllByEnabledOrderByRouteOrderAsc(Boolean enabled);
    List<GatewayRouteEntity> findByEnabledOrderByRouteOrderAsc(Boolean enabled);
    // 不需傳參，直接撈取啟用（true）且依 order 正序排列的路由（最直覺）
    List<GatewayRouteEntity> findByEnabledTrueOrderByRouteOrderAsc();

}
