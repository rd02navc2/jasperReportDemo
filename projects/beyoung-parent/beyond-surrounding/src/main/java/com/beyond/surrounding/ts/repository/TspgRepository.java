package com.beyond.surrounding.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.beyond.surrounding.ts.entity.TS_EC_LOG;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TspgRepository extends JpaRepository<TS_EC_LOG, String> {
	
	/**
     * 依據訂單號碼刪除舊有的 EC LOG 紀錄
     */
    @Modifying
    @Query(value = "DELETE FROM TS_EC_LOG WHERE order_no = :orderNo", nativeQuery = true)
    void deleteByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 新增初始授權的交易紀錄 (type 預設為 1)
     */
    @Modifying
    @Query(value = """
            INSERT INTO TS_EC_LOG (
                order_no, ec_order_no, type, amt, order_desc, 
                card_no, install_period, access_date, auth_date
            ) VALUES (
                :orderNo, :ecOrderNo, 1, :amt, :orderDesc, 
                :cardNo, :installPeriod, :now, :now
            )
            """, nativeQuery = true)
    void insertInitialLog(
            @Param("orderNo") String orderNo,
            @Param("ecOrderNo") String ecOrderNo,
            @Param("amt") Integer amt,
            @Param("orderDesc") String orderDesc,
            @Param("cardNo") String cardNo,
            @Param("installPeriod") Integer installPeriod,
            @Param("now") java.time.LocalDateTime now
    );

    /**
     *  取消授權更新：同時更新 access_date 與 auth_cancel_date
     */
    @Modifying
    @Query(value = """
            UPDATE TS_EC_LOG 
            SET type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :now, 
                amt_refund = :amtRefund, 
                auth_cancel_date = :now 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    int updateAuthCancel(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("amtRefund") Integer amtRefund,
            @Param("now") LocalDateTime now
    );

    /**
     * 退款更新：同時更新 access_date 與 refund_date
     */
    @Modifying
    @Query(value = """
            UPDATE TS_EC_LOG 
            SET type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :now, 
                amt_refund = :amtRefund, 
                refund_date = :now 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    int updateRefund(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("amtRefund") Integer amtRefund,
            @Param("now") LocalDateTime now
    );
    
    /**
     * 取消退款更新：同時更新 access_date 與 refund_cancel_date
     */
    @Modifying
    @Query(value = """
            UPDATE TS_EC_LOG 
            SET type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :now, 
                refund_cancel_date = :now 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    int updateRefundCancel(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("now") java.time.LocalDateTime now
    );
  
    /**
     * 處理背景 PostBack 授權回傳更新 (type = '1')
     */
    @Modifying
    @Query(value = """
            UPDATE TS_EC_LOG 
            SET type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :now, 
                auth_date = :now 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    int updateAuthPostBack(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("now") java.time.LocalDateTime now
    );
    
    /**
     * 當 card_type != "1" 時，查詢台新金流資料表
     */
    @Query(value = """
            SELECT * 
            FROM TS_EC_LOG 
            WHERE order_no = :orderNo 
            LIMIT 1
            """, nativeQuery = true)
    Optional<TS_EC_LOG> getStatusFromTs(@Param("orderNo") String orderNo);
    
    /**
     * 當 card_type == "1" 時，跨表查詢聯合信用卡資料表，並對應至 TS_EC_LOG 實體
     */
    @Query(value = """
            SELECT * 
            FROM NCCC_EC_LOG 
            WHERE order_no = :orderNo 
            LIMIT 1
            """, nativeQuery = true)
    Optional<TS_EC_LOG> getStatusFromNccc(@Param("orderNo") String orderNo);
    
    
}