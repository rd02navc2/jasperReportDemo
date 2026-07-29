package com.beyoung.surrounding.dc.repositiry;

import com.beyoung.surrounding.dc.entity.DOOR_CONTROL_UNLIMIT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoorControlUnlimitRepository extends JpaRepository<DOOR_CONTROL_UNLIMIT, String> {
    
    /**
     * 檢查 user_id 是否存在於特權名單中
     * Spring Data JPA 會自動將其轉譯為最優效能的 SQL 語法：
     * select count(*) > 0 from door_control_unlimit where user_id = ? (或是 limit 1 形式)
     */
    boolean existsByUserId(String userId);
}