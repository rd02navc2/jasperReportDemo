package com.beyoung.surrounding.dc.repositiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.dc.entity.VIP_ROOM_UNLIMIT; // 請依實體路徑調整

@Repository
public interface VipRoomUnlimitRepository extends JpaRepository<VIP_ROOM_UNLIMIT, String> {
    // 檢查員工白名單是否存在
    boolean existsByUserId(String userId);
}