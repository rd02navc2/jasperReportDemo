package com.beyond.surrounding.ts.repository;

import com.beyond.surrounding.ts.entity.TS_EC_LOG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface NcccPgRepository extends JpaRepository<TS_EC_LOG, String> {

    /**
     * 使用 Java 15+ Text Blocks 實作原生刪除
     */
    @Modifying
    @Query(value = """
            DELETE FROM NCCC_EC_LOG 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    void deleteByOrderNoNative(@Param("orderNo") String orderNo);

    /**
     * 實作原生寫入 (對齊舊有 DAO 邏輯，強制寫入 type=1)
     */
    @Modifying
    @Query(value = """
            INSERT INTO NCCC_EC_LOG (
                order_no, ec_order_no, type, amt, order_desc, 
                card_no, install_period, access_date, auth_date
            ) VALUES (
                :orderNo, :ecOrderNo, 1, :amt, :orderDesc, 
                :cardNo, :installPeriod, :accessDate, :authDate
            )
            """, nativeQuery = true)
    void insertNative(
            @Param("orderNo") String orderNo,
            @Param("ecOrderNo") String ecOrderNo,
            @Param("amt") Integer amt,
            @Param("orderDesc") String orderDesc,
            @Param("cardNo") String cardNo,
            @Param("installPeriod") Integer installPeriod,
            @Param("accessDate") LocalDateTime accessDate,
            @Param("authDate") LocalDateTime authDate
    );

    /**
     * 實作標準更新 (無退款金額)
     */
    @Modifying
    @Query(value = """
            UPDATE NCCC_EC_LOG 
            SET approve_code = :approveCode, 
                type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :accessDate 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    void updateNative(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("approveCode") String approveCode,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("accessDate") LocalDateTime accessDate
    );

    /**
     * 實作退款更新 (含 amt_refund 與 auth_cancel_date)
     */
    @Modifying
    @Query(value = """
            UPDATE NCCC_EC_LOG 
            SET approve_code = :approveCode, 
                type = :txType, 
                ret_code = :retCode, 
                ret_msg = :retMsg, 
                access_date = :accessDate, 
                amt_refund = :amtRefund, 
                auth_cancel_date = :authCancelDate 
            WHERE order_no = :orderNo
            """, nativeQuery = true)
    void updateRefundNative(
            @Param("orderNo") String orderNo,
            @Param("txType") String txType,
            @Param("approveCode") String approveCode,
            @Param("retCode") String retCode,
            @Param("retMsg") String retMsg,
            @Param("accessDate") LocalDateTime accessDate,
            @Param("amtRefund") Integer amtRefund,
            @Param("authCancelDate") LocalDateTime authCancelDate
    );
    
}