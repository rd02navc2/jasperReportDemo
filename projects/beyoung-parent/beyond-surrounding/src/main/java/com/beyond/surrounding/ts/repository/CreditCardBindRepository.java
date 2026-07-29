package com.beyond.surrounding.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.beyond.surrounding.ts.bean.PayPlusRequestBean;
import com.beyond.surrounding.ts.entity.CREDIT_CARD_BIND_LOG;

@Repository
public interface CreditCardBindRepository extends JpaRepository<CREDIT_CARD_BIND_LOG, String> {

    @Modifying
    @Query(value = """
        UPDATE CREDIT_CARD_BIND_LOG 
        SET BANK_NO = :bankNo, 
            CARD_NAME = :cardName, 
            CARD_NUMBER = :cardNumber, 
            CARD_STATUS = :cardStatus, 
            CARD_TYPE = :cardType, 
            CARD_TOKEN = :cardToken, 
            UPDATE_TIME = NOW() 
        WHERE ORDER_NO = :orderNo
        """, nativeQuery = true)
    int updateCreditCardStatus(
        @Param("orderNo") String orderNo,
        @Param("bankNo") String bankNo,
        @Param("cardName") String cardName,
        @Param("cardNumber") String cardNumber,
        @Param("cardStatus") String cardStatus,
        @Param("cardType") String cardType,
        @Param("cardToken") String cardToken
    );
    
    @Modifying
    @Query(value = """
        UPDATE CREDIT_CARD_BIND_LOG 
        SET CARD_STATUS = 'DELETED', 
            UPDATE_TIME = NOW() 
        WHERE ORDER_NO = :orderNo 
          AND MEMBER_ID = :memberId
        """, nativeQuery = true)
    int updateDeleteMark(
        @Param("orderNo") String orderNo,
        @Param("memberId") String memberId
    );

    /**
     * 依據訂單編號更新 3D 驗證後的卡片狀態與相關欄位
     * 使用 Java 15+ Text Blocks 語法，排版更優雅
     */
    @Modifying
    @Query(value = """
            UPDATE CREDIT_CARD_BIND_LOG
            SET CARD_STATUS = :cardStatus,
                CARD_TOKEN = COALESCE(:cardToken, CARD_TOKEN),
                UPDATE_TIME = CURRENT_TIMESTAMP
            WHERE ORDER_NO = :orderNo
            """, nativeQuery = true)
    int update3DStatus(@Param("orderNo") String orderNo, 
                       @Param("cardStatus") String cardStatus,
                       @Param("cardToken") String cardToken);
    
}