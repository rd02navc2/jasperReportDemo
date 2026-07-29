package com.beyond.surrounding.bonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.ec.entity.TC_LRJ_FILE;
import com.beyond.surrounding.ec.entity.TC_LRJ_FILE_ComposeKey;
import java.util.Date;

@Repository
public interface BonusLogRepository extends JpaRepository<TC_LRJ_FILE, TC_LRJ_FILE_ComposeKey> {

    @Modifying
    @Query(value = """
            INSERT INTO BONUS_LOG (
                center, counter_id, user_id, user_name, 
                card_no, point, access_date, access_id
            ) VALUES (
                :center, :counterId, :userId, :userName, 
                :cardNo, :point, :accessDate, :loginId
            )
            """, nativeQuery = true)
    void insertBonusLog(
        @Param("center") String center,
        @Param("counterId") String counterId,
        @Param("userId") String userId,
        @Param("userName") String userName,
        @Param("cardNo") String cardNo,
        @Param("point") Integer point,
        @Param("accessDate") Date accessDate,
        @Param("loginId") String loginId
    );
    
}