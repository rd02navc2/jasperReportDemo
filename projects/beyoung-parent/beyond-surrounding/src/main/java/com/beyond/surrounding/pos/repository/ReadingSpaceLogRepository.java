package com.beyond.surrounding.pos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.pos.entity.READING_SPACE_LOG;
import com.beyond.surrounding.pos.entity.READING_SPACE_LOG_ComposeKey;


@Repository
public interface ReadingSpaceLogRepository extends JpaRepository<READING_SPACE_LOG, READING_SPACE_LOG_ComposeKey>{
	
		/**
	     * 1. 計算今天在書房內（已進場未出場）的總人數
	     */
	    @Query(value = """
	        SELECT COUNT(*) 
	        FROM READING_SPACE_LOG 
	        WHERE center = :center 
	          AND transaction_date = CURDATE() 
	          AND in_room = 'Y'
	    """, nativeQuery = true)
	    int countTodayInRoomPeople(@Param("center") String center);
	
	    /**
	     * 2. 尋找今天未退款的特定會員交易紀錄
	     */
	    @Query(value = """
	        SELECT * FROM READING_SPACE_LOG 
	        WHERE center = :center 
	          AND transaction_date = CURDATE() 
	          AND user_id = :userId 
	          AND refund_date IS NULL
	    """, nativeQuery = true)
	    List<READING_SPACE_LOG> findTodayActiveLog(
	            @Param("center") String center, 
	            @Param("userId") String userId
	    );

		@Query(value = """
		    SELECT *
		    FROM READING_SPACE_LOG
		    WHERE center = :center
		      AND transaction_date >= CURDATE()
		      AND transaction_date < CURDATE() + INTERVAL 1 DAY
		      AND user_id = :userId
		""", nativeQuery = true)
		List<READING_SPACE_LOG> findTodayLog(
		        @Param("center") String center,
		        @Param("userId") String userId
		);
		
		/**
	     * 3. 尋找特定場館、特定日期、特定會員的交易紀錄
	     * 註：transaction_date 在資料庫若為 DATE 形態，傳入字串形式 (如 '2026-06-30')，
	     * 在 Native Query 裡 MySQL 會自動進行隱式轉換，非常安全。
	     */
	    @Query(value = """
	        SELECT * FROM READING_SPACE_LOG 
	        WHERE center = :center 
	          AND transaction_date = :transactionDate 
	          AND user_id = :userId
	    """, nativeQuery = true)
	    List<READING_SPACE_LOG> findLogForRefund(
	            @Param("center") String center, 
	            @Param("transactionDate") String transactionDate,
	            @Param("userId") String userId
	    );
	    
	    
}
