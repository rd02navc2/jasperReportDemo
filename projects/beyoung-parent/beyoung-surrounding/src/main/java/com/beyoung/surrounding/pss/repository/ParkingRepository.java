package com.beyoung.surrounding.pss.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.beyoung.surrounding.pss.bean.DiscountDetailBean;
import com.beyoung.surrounding.pss.dto.ParkingDetailDTO;
import com.beyoung.surrounding.pss.entity.LpjFile;
import com.beyoung.surrounding.pss.entity.ParkingDiscountExec;
import com.beyoung.surrounding.pss.entity.ParkingDiscountExecHeader;
import com.beyoung.surrounding.pss.entity.ParkingDiscountExecId;
import com.beyoung.surrounding.pss.entity.ParkingRent;
import com.beyoung.surrounding.pss.entity.TcPsaFile;

@Repository
// @Transactional(transactionManager = "RMS_TM")
@Transactional
public interface ParkingRepository extends JpaRepository<ParkingDiscountExec, ParkingDiscountExecId> {

    /*
     	DELIMITER //

		CREATE PROCEDURE sp_append_invoice_parking(
		    IN p_date VARCHAR(20),
		    IN p_inv_no VARCHAR(20),
		    IN p_rand_no VARCHAR(20),
		    IN p_inv_time VARCHAR(20),
		    IN p_center VARCHAR(20),
		    IN p_channel VARCHAR(20),
		    IN p_type INT,
		    IN p_ctr_id VARCHAR(20),
		    IN p_card_no VARCHAR(50),
		    IN p_car_no VARCHAR(20),
		    IN p_amt DOUBLE
		)
		BEGIN
		    -- 這裡處理您原本 Java 裡面的邏輯 (簡化版)
		    IF p_type = 0 THEN
		        INSERT INTO PARKING_POS_LOG (
		            invoice_date, invoice_no, random_no, invoice_time, center, 
		            channel, tranx_type, counter_id, promote_amt, card_no, car_no, access_date
		        ) VALUES (
		            p_date, p_inv_no, p_rand_no, p_inv_time, p_center, 
		            p_channel, p_type, p_ctr_id, p_amt, p_card_no, p_car_no, NOW()
		        );
		    ELSE
		        -- 處理負數折抵的情況
		        INSERT INTO PARKING_POS_LOG (
		            invoice_date, invoice_no, random_no, invoice_time, center, 
		            channel, tranx_type, counter_id, promote_amt, card_no, car_no, access_date
		        ) 
		        SELECT p_date, p_inv_no, p_rand_no, invoice_time, center, 
		               p_channel, p_type, counter_id, -p_amt, card_no, car_no, NOW()
		        FROM PARKING_POS_LOG 
		        WHERE invoice_date = p_date AND invoice_no = p_inv_no AND random_no = p_rand_no;
		    END IF;
		END //
		
		DELIMITER ;
		

    @Modifying
    
    @Query(value = "CALL sp_append_invoice_parking(:date, :invNo, :randNo, :invTime, :center, :ch, :type, :ctrId, :cardNo, :carNo, :amt)", nativeQuery = true)
    void executeParkingAppendProcedure(@Param("date") String date, @Param("invNo") String invNo, @Param("randNo") String randNo, 
                                       @Param("invTime") String invTime, @Param("center") String center, @Param("ch") String ch, 
                                       @Param("type") Integer type, @Param("ctrId") String ctrId, @Param("cardNo") String sCardNO, 
                                       @Param("carNo") String carNo, @Param("amt") Double amt);
	*/
	
	@Modifying
	@Query(value = """
	    INSERT INTO PARKING_POS_LOG (
	        invoice_date, invoice_no, random_no, invoice_time, center, 
	        channel, tranx_type, counter_id, promote_amt, card_no, car_no, access_date
	    ) VALUES (
	        :date, :invNo, :randNo, :invTime, :center, 
	        :ch, :type, :ctrId, :cardNo, :carNo, :amt, NOW()
	    )
	    """, nativeQuery = true)
	void executeParkingAppend(
	    @Param("date") String date, 
	    @Param("invNo") String invNo, 
	    @Param("randNo") String randNo, 
	    @Param("invTime") String invTime, 
	    @Param("center") String center, 
	    @Param("ch") String ch, 
	    @Param("type") Integer type, 
	    @Param("ctrId") String ctrId, 
	    @Param("cardNo") String cardNo, 
	    @Param("carNo") String carNo, 
	    @Param("amt") Double amt
	);
	
    /**
     * 檢核發票狀態 (使用 LpjFile 對齊 Controller 與 Service 邏輯)
     */
	/**
     * 修正：對齊前述業務邏輯，查詢 tc_psa_file 且綁定當日發票限制
     * 欄位對應說明：tc_psa16 || tc_psa17 為發票號碼，tc_psa04 為發票日期
     */
    @Query(value = """
            SELECT * FROM tc_psa_file 
            WHERE (tc_psa16 || tc_psa17) = :invoiceNo 
              AND TO_CHAR(tc_psa04, 'yyyy-MM-dd') = :todayStr
            """, 
           nativeQuery = true)
    Optional<TcPsaFile> check4ParkingUncheck(@Param("invoiceNo") String invoiceNo, 
                                             @Param("todayStr") String todayStr);

    /**
     * 帶有隨機碼 (tc_psa31) 的完整校驗查詢
     */
    @Query(value = """
            SELECT * FROM tc_psa_file 
            WHERE (tc_psa16 || tc_psa17) = :invoiceNo 
              AND tc_psa31 = :randomNo 
              AND TO_CHAR(tc_psa04, 'yyyy-MM-dd') = :todayStr
            """, 
           nativeQuery = true)
    Optional<TcPsaFile> check4ParkingWithRandom(@Param("invoiceNo") String invoiceNo, 
                                                @Param("randomNo") String randomNo, 
                                                @Param("todayStr") String todayStr);

    /**
     * 檢核 APS_UN 日誌是否存在
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM PARKING_POS_LOG WHERE invoice_no = :invNo AND random_no = :randNo", nativeQuery = true)
    boolean existsParkingLog(@Param("invNo") String invNo, @Param("randNo") String randNo);

    // --- 補強：供 Service.insertCard 使用的 Header 管理 ---

    @Query(value = "SELECT * FROM PARKING_DISCOUNT_EXEC_HEADER WHERE car_no = :carNo AND DATE_FORMAT(create_date, '%Y-%m-%d') = :date LIMIT 1", nativeQuery = true)
    Optional<ParkingDiscountExecHeader> findTodayHeaderByCarNo(@Param("carNo") String carNo, @Param("date") String date);

    default ParkingDiscountExecHeader saveHeader(ParkingDiscountExecHeader header) {
        // 需注入 HeaderRepository 或在此透過 EntityManager 儲存
        // 建議拆分為 HeaderRepository 以符合 Spring Data JPA 標準
        return null; 
    }

    default void saveExec(ParkingDiscountExec exec) {
        this.save(exec);
    }

    // --- 原有邏輯保留 ---

    @Query(value = """
        SELECT * FROM PARKING_DISCOUNT_EXEC 
        WHERE DATE_FORMAT(access_date, '%Y%m%d') = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND car_no <> :carNo 
          AND card_id = :cardId
        LIMIT 1
        """, nativeQuery = true)
    Optional<ParkingDiscountExec> getCardUsed(@Param("carNo") String carNo, @Param("cardId") String cardId);

    @Query(value = """
        SELECT * FROM PARKING_DISCOUNT_EXEC 
        WHERE DATE_FORMAT(access_date, '%Y%m%d') = DATE_FORMAT(NOW(), '%Y%m%d') 
          AND car_no <> :carNo 
          AND user_id = :userId
        LIMIT 1
        """, nativeQuery = true)
    Optional<ParkingDiscountExec> getVIPUsed(@Param("carNo") String carNo, @Param("userId") String userId);

    @Query(value = """
        SELECT * FROM PARKING_RENT 
        WHERE car_no = :carNo 
          AND (is_unlimited_date = 'Y' 
               OR (DATE_FORMAT(NOW(), '%Y%m%d') BETWEEN DATE_FORMAT(start_date, '%Y%m%d') AND DATE_FORMAT(end_date, '%Y%m%d')))
        LIMIT 1
        """, nativeQuery = true)
    Optional<ParkingRent> getParkingRent(@Param("carNo") String carNo);

    @Query(value = """
    	    SELECT 
    	        t2.p_no, t1.disc_id, t1.disc_name, t1.disc_hour, 
    	        t1.is_unlimited_hour, t1.hour_max, t2.booking_date, 
    	        t2.disc_hour AS used_hour, t2.is_used, 
    	        0.0 AS promote_amt -- 補上這個佔位欄位，讓陣列長度達到 10
    	    FROM PARKING_DISCOUNT_SET t1
    	    LEFT JOIN PARKING_DISCOUNT_EXEC t2 ON t1.disc_id = t2.disc_id 
    	        AND t2.car_no = :carNo 
    	        AND DATE_FORMAT(t2.access_date, '%Y%m%d') = DATE_FORMAT(NOW(), '%Y%m%d')
    	    WHERE t1.is_active = 'Y'
    	    ORDER BY t1.sort_order
    	    """, nativeQuery = true)
    	List<Object[]> getDiscExecNative(@Param("carNo") String carNo);

	@Query(value = """
	        SELECT t1.disc_id, t1.disc_name, t2.p_no, t2.is_used 
	        FROM PARKING_DISCOUNT_SET t1 
	        LEFT JOIN (
	            SELECT p_no, disc_id, is_used 
	            FROM PARKING_DISCOUNT_EXEC 
	            WHERE car_no = :carNo 
	              AND DATE_FORMAT(access_date, '%Y%m%d') = DATE_FORMAT(NOW(), '%Y%m%d')
	        ) t2 ON t1.disc_id = t2.disc_id 
	        WHERE t1.is_active = 'Y' 
	          AND t1.disc_id IN ('vip', 'black_card', 'member_card', 'ts_common') 
	        ORDER BY t1.sort_order
	        """, nativeQuery = true)
	    List<Object[]> getDiscExec2Native(@Param("carNo") String carNo);

    @Modifying
    @Query(value = """
        UPDATE PARKING_DISCOUNT_EXEC_HEADER 
        SET enter_date = STR_TO_DATE(:enterDt, '%Y-%m-%d %H:%i:%s'), 
            exit_date = STR_TO_DATE(:exitDt, '%Y-%m-%d %H:%i:%s'), 
            car_no = :carNo, 
            parking_hour = :parkingHour, 
            parking_fee = :parkingFee, 
            disc_fee = :discFee, 
            pay_amt = :payAmt, 
            paid_amt = :paidAmt, 
            tot_disc_hour = :totDiscHour, 
            real_disc_hour = :realDiscHour, 
            other_disc_fee = :otherDiscFee, 
            other_disc_hour = :otherDiscHour, 
            is_used = :isUsed, 
            access_date = NOW(), 
            access_id = :accessId 
        WHERE p_no = :pNo
        """, nativeQuery = true)
    void updateHeaderSync(@Param("enterDt") String enterDt, @Param("exitDt") String exitDt, @Param("carNo") String carNo, @Param("parkingHour") Double parkingHour, @Param("parkingFee") Double parkingFee, @Param("discFee") Double discFee, @Param("payAmt") Double payAmt, @Param("paidAmt") Double paidAmt, @Param("totDiscHour") Double totDiscHour, @Param("realDiscHour") Double realDiscHour, @Param("otherDiscFee") Double otherDiscFee, @Param("otherDiscHour") Double otherDiscHour, @Param("isUsed") String isUsed, @Param("accessId") String accessId, @Param("pNo") Integer pNo);

    @Modifying
    @Query(value = """
        UPDATE PARKING_POS_LOG 
        SET p_no = :pNo, 
            user_id = :userId, 
            card_no = :cardNo 
        WHERE p_no IS NULL 
          AND invoice_date = :todayDate 
          AND car_no = :carNo
        """, nativeQuery = true)
    void updatePosLogSync(@Param("pNo") Integer pNo, @Param("userId") String userId, @Param("cardNo") String cardNo, @Param("todayDate") String todayDate, @Param("carNo") String carNo);

    @Modifying
    @Query(value = """
        UPDATE PARKING_DISCOUNT_EXEC 
        SET is_used = :isUsed, 
            access_date = NOW(), 
            access_id = :accessId 
        WHERE p_no = :pNo 
          AND car_no = :carNo 
          AND disc_id = :discId
        """, nativeQuery = true)
    void updateExecSync(@Param("isUsed") String isUsed, @Param("accessId") String accessId, @Param("pNo") Integer pNo, @Param("carNo") String carNo, @Param("discId") String discId);

    @Modifying
    @Query(value = """
        DELETE FROM PARKING_DISCOUNT_EXEC 
        WHERE p_no = :pNo 
          AND card_id = :cardId
        """, nativeQuery = true)
    void deleteCardNo(@Param("pNo") Integer pNo, @Param("cardId") String cardId);
    @Query(value = """
    	    SELECT 
    	        car_no, enter_date, parking_hour, parking_fee, p_no 
    	    FROM PARKING_DISCOUNT_EXEC_HEADER 
    	    WHERE car_no = :carNo 
    	    AND exit_date IS NULL 
    	    LIMIT 1
    	    """, nativeQuery = true)
    	ParkingDetailDTO findByCarNo(@Param("carNo") String carNo);
    
    
    @Query(value = """
            SELECT p_no, car_no 
            FROM PARKING_IN 
            WHERE car_no = :carNo 
            LIMIT 1
            """, nativeQuery = true)
        Object[] findActiveParkingIn(@Param("carNo") String carNo);

	Optional<LpjFile> findParkingRentByCarNo(String carNO);

	// void sync(DiscountDetailBean requestBody);

	// 1. 如果 ParkingDiscountExecHeader 不是此 Repository 的主實體，
    // 建議直接呼叫該實體的專屬 Repository.save()。
    // 如果這群方法都在同一個 ParkingRepository 裡，保持這樣宣告即可。
    ParkingDiscountExecHeader save(ParkingDiscountExecHeader header);

    // 2. 替代舊系統 update PARKING_DISCOUNT_EXEC_HEADER 的原生 SQL
    @Modifying
    @Query(value = """
        UPDATE PARKING_DISCOUNT_EXEC_HEADER 
        SET enter_date = :enterDt, 
            exit_date = :exitDt, 
            car_no = :carNo, 
            parking_hour = :parkingHour, 
            parking_fee = :parkingFee, 
            disc_fee = :discFee, 
            pay_amt = :payAmt, 
            paid_amt = :paidAmt, 
            tot_disc_hour = :totDiscHour, 
            real_disc_hour = :realDiscHour, 
            other_disc_fee = :otherDiscFee, 
            other_disc_hour = :otherDiscHour, 
            is_used = :isUsed, 
            access_date = :accessDate, 
            access_id = :accessId 
        WHERE p_no = :pNo
        """, nativeQuery = true)
    void updateParkingHeader(
            @Param("enterDt") String enterDt, @Param("exitDt") String exitDt, @Param("carNo") String carNo,
            @Param("parkingHour") Double parkingHour, @Param("parkingFee") Double parkingFee, @Param("discFee") Double discFee,
            @Param("payAmt") Double payAmt, @Param("paidAmt") Double paidAmt, @Param("totDiscHour") Double totDiscHour,
            @Param("realDiscHour") Double realDiscHour, @Param("otherDiscFee") Double otherDiscFee, @Param("otherDiscHour") Double otherDiscHour,
            @Param("isUsed") String isUsed, @Param("accessDate") java.sql.Timestamp accessDate, @Param("accessId") String accessId, @Param("pNo") Integer pNo);

    // 3. 替代舊系統 update PARKING_POS_LOG
    @Modifying
    @Query(value = """
        UPDATE PARKING_POS_LOG 
        SET p_no = :pNo, 
            user_id = :userId, 
            card_no = :cardNo 
        WHERE p_no IS NULL 
          AND invoice_date = :invoiceDate 
          AND car_no = :carNo
        """, nativeQuery = true)
    void updateParkingPosLog(
            @Param("pNo") Integer pNo, @Param("userId") String userId, @Param("cardNo") String cardNo, 
            @Param("invoiceDate") String invoiceDate, @Param("carNo") String carNo);

    // 4. 替代舊系統 update PARKING_DISCOUNT_EXEC
    @Modifying
    @Query(value = """
        UPDATE PARKING_DISCOUNT_EXEC 
        SET is_used = :isUsed, 
            access_date = :accessDate, 
            access_id = :accessId 
        WHERE p_no = :pNo 
          AND car_no = :carNo 
          AND disc_id = :discId
        """, nativeQuery = true)
    void updateParkingDiscountExecStatus(
            @Param("isUsed") String isUsed, @Param("accessDate") java.sql.Timestamp accessDate, @Param("accessId") String accessId,
            @Param("pNo") Integer pNo, @Param("carNo") String carNo, @Param("discId") String discId);
    
}