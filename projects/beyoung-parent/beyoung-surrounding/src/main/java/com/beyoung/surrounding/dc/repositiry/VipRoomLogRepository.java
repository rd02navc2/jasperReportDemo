package com.beyoung.surrounding.dc.repositiry;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.dc.entity.VIP_ROOM_LOG; // 請依實際路徑調整
import com.beyoung.surrounding.dc.entity.VIP_ROOM_LOG_ComposeKey;

@Repository
public interface VipRoomLogRepository extends JpaRepository<VIP_ROOM_LOG, VIP_ROOM_LOG_ComposeKey> {

	// 1. 查詢今日未退款紀錄
    @Query(value = """
        SELECT * FROM VIP_ROOM_LOG 
        WHERE center = :center 
          AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND user_id = :userId 
          AND refund_date IS NULL
        """, nativeQuery = true)
    List<VIP_ROOM_LOG> findTodayActiveLogs(@Param("center") String center, @Param("userId") String userId);

    // 2. 查詢今日所有紀錄 (供 exit 與 refund 使用)
    @Query(value = """
        SELECT * FROM VIP_ROOM_LOG 
        WHERE center = :center 
          AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND user_id = :userId
        """, nativeQuery = true)
    List<VIP_ROOM_LOG> findTodayLogs(@Param("center") String center, @Param("userId") String userId);

    // 3. 進入 VIP 室更新
    @Modifying
    @Query(value = """
        UPDATE VIP_ROOM_LOG SET enter_date = NOW(), in_room = 'Y' 
        WHERE center = :center 
          AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND user_id = :userId
        """, nativeQuery = true)
    void updateEnterStatus(@Param("center") String center, @Param("userId") String userId);

    // 4. 離開 VIP 室更新
    @Modifying
    @Query(value = """
        UPDATE VIP_ROOM_LOG SET exit_date = NOW(), in_room = 'N' 
        WHERE center = :center 
          AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND user_id = :userId
        """, nativeQuery = true)
    void updateExitStatus(@Param("center") String center, @Param("userId") String userId);

    // 5. 取消/退款 VIP 室更新
    @Modifying
    @Query(value = """
        UPDATE VIP_ROOM_LOG SET refund_date = NOW() 
        WHERE center = :center 
          AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND user_id = :userId
        """, nativeQuery = true)
    void updateRefundStatus(@Param("center") String center, @Param("userId") String userId);

	
	@Modifying
    @Query(value = """
            UPDATE VIP_ROOM_LOG SET 
                transaction_time = :transactionTime, 
                card_no = :cardId, 
                enter_date = NULL, 
                exit_date = NULL, 
                refund_date = NULL, 
                invoice_no = NULL, 
                vip = NULL, 
                in_room = NULL 
            WHERE center = :center 
              AND transaction_date = DATE_FORMAT(NOW(), '%Y%m%d') 
              AND user_id = :userId
            """, nativeQuery = true)
    void updateRefundedLog(
            @Param("transactionTime") String transactionTime,
            @Param("cardId") String cardId,
            @Param("center") String center,
            @Param("userId") String userId
    );
    
}