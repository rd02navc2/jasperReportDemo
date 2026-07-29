package com.beyoung.surrounding.pos2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.pos2.entity.LqeFile;
import com.beyoung.surrounding.pos2.entity.LntFile;

@Repository
public interface CouponPos2Repository extends JpaRepository<LqeFile, String> {

	/**
     * 1. 批量失效優惠券 (對應原 doCouponInvalid)
     * 使用 @Modifying 標記為更新操作，並確保事務由調用方（Service）控制
     */
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE LQE_FILE 
        SET LQE11 = :center, 
            LQE12 = CURRENT_TIMESTAMP, 
            LQE17 = '3' 
        WHERE LQE01 IN :ids
        """, nativeQuery = true)
    void updateCouponInvalid(@Param("center") String center, @Param("ids") List<String> ids);
    
    /**
     * 2. 透過發票號碼查詢優惠券詳細資訊 (對應原 getCouponByInvoiceNO)
     * 使用 nativeQuery 處理複雜的 CASE 邏輯
     */
    @Query(value = """
        SELECT GC_NO AS gcNo, 
               CASE LEFT(GC_NO, 2) 
                   WHEN 'UK' THEN 100 WHEN 'UL' THEN 200 WHEN 'UM' THEN 300 
                   WHEN 'UN' THEN 500 WHEN 'TL' THEN 100 ELSE 0 END AS gcAmt,
               CASE LEFT(UPD_NAME, 1) WHEN '9' THEN 'Y' ELSE 'N' END AS isApp 
        FROM COUPON_DATA 
        WHERE PKEY IN (SELECT PKEY FROM COUPON_SALE WHERE RECEI_NO = :invoiceNo)
        """, nativeQuery = true)
    List<Object[]> findCouponInfoByInvoice(@Param("invoiceNo") String invoiceNo);
    
    @Query(value = """
            SELECT 
                lqe01, lqe17, lqe20, lqe21, ta_lqe02,
                CASE 
                    WHEN ta_lqe09 = 'Y' 
                         OR TRUNC(sysdate) < lqe20 
                         OR TRUNC(sysdate) > lqe21 THEN 'Y' 
                    ELSE 'N' 
                END AS ta_lqe09,
                -- 補齊其餘欄位為 NULL，讓 Hibernate 能順利封裝成 LQE_FILE 實體
                NULL as lqe02, NULL as lqe03, NULL as lqe04, NULL as lqe05, 
                NULL as lqe06, NULL as lqe07, NULL as lqe08, NULL as lqe10, 
                NULL as lqe11, NULL as lqe12, NULL as lqe13, NULL as lqe14, 
                NULL as lqe15, NULL as lqe16, NULL as lqe18, NULL as lqe19, 
                NULL as lqepos, NULL as lqe22, NULL as lqe23, NULL as lqe24, 
                NULL as lqe25, NULL as ta_lqe01, NULL as ta_lqe03, NULL as ta_lqe04, 
                NULL as ta_lqe05, NULL as ta_lqe06, NULL as ta_lqe07
            FROM LQE_FILE 
            WHERE lqe01 IN (:couponIds)
            """, nativeQuery = true)
    List<LntFile> findCouponStatusRaw(@Param("couponIds") List<String> couponIds);
    
    @Query(value = """
            SELECT 
                lqe01 AS "LQE01", 
                lqe17 AS "LQE17", 
                lqe20 AS "LQE20", 
                lqe21 AS "LQE21", 
                ta_lqe02 AS "TA_LQE02",
                CASE 
                    WHEN ta_lqe09 = 'Y' 
                         OR CURDATE() < lqe20 
                         OR CURDATE() > lqe21 THEN 'Y' 
                    ELSE 'N' 
                END AS "TA_LQE09",
                -- 依據實體類別結構，將其餘未用到的欄位全部精準對齊大寫別名補 NULL
                NULL AS "LQE02",    NULL AS "LQE03",    NULL AS "LQE04",    NULL AS "LQE05", 
                NULL AS "LQE06",    NULL AS "LQE07",    NULL AS "LQE08",    NULL AS "LQE09", 
                NULL AS "LQE10",    NULL AS "LQE11",    NULL AS "LQE12",    NULL AS "LQE13", 
                NULL AS "LQE14",    NULL AS "LQE15",    NULL AS "LQE16",    NULL AS "LQE18", 
                NULL AS "LQE19",    NULL AS "LQEPOS",   NULL AS "LQE22",    NULL AS "LQE23", 
                NULL AS "LQE24",    NULL AS "LQE25",    
                NULL AS "TA_LQE01", NULL AS "TA_LQE03", NULL AS "TA_LQE04", NULL AS "TA_LQE05", 
                NULL AS "TA_LQE06", NULL AS "TA_LQE07"
            FROM LQE_FILE 
            WHERE lqe01 IN (:couponIds)
            """, nativeQuery = true)
    List<LqeFile> findCouponStatusRawLqeFile(@Param("couponIds") List<String> couponIds);
    
    @Query(value = """
            SELECT 
                lqe01 AS "LQE01", 
                lqe17 AS "LQE17", 
                CASE WHEN ta_lqe09 = 'Y' THEN 'Y' ELSE 'N' END AS "TA_LQE09", 
                ta_lqe02 AS "TA_LQE02",
                -- 補齊 Entity 其餘必要欄位為 NULL，防止 Hibernate 拋出 Column Not Found
                NULL AS "LQE02", NULL AS "LQE03", NULL AS "LQE04", NULL AS "LQE05", 
                NULL AS "LQE06", NULL AS "LQE07", NULL AS "LQE08", NULL AS "LQE09", 
                NULL AS "LQE10", NULL AS "LQE11", NULL AS "LQE12", NULL AS "LQE13", 
                NULL AS "LQE14", NULL AS "LQE15", NULL AS "LQE16", NULL AS "LQE18", 
                NULL AS "LQE19", NULL AS "LQEPOS", NULL AS "LQE22", NULL AS "LQE23", 
                NULL AS "LQE24", NULL AS "LQE25", NULL AS "TA_LQE01", NULL AS "TA_LQE03", 
                NULL AS "TA_LQE04", NULL AS "TA_LQE05", NULL AS "TA_LQE06", NULL AS "TA_LQE07"
            FROM LQE_FILE 
            WHERE lqe01 IN (:couponIds)
            """, nativeQuery = true)
    List<LqeFile> findCouponRealStatusList(@Param("couponIds") List<String> couponIds);

    
    
    
    
    
}