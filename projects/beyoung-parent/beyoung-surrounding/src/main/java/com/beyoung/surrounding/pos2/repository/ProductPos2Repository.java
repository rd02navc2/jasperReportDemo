package com.beyoung.surrounding.pos2.repository;

import com.beyoung.surrounding.pos2.entity.IMA_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductPos2Repository extends JpaRepository<IMA_FILE, String> {

    // 善用 MySQL 的 DATE_FORMAT（取代 Oracle 的 TO_CHAR），並利用 JPA 的自動映射特性
    @Query(value = """
            SELECT 
                IMA01, IMA02, TA_IMA01, IMA15, IMA127, IMA128, IMA131, 
                DATE_FORMAT(IMADATE, '%Y-%m-%d') AS IMADATE,
                NULL AS OBA02, NULL AS IMA1005, NULL AS TQA02, NULL AS IMA25, NULL AS IMA54, NULL AS RTG05
            FROM IMA_FILE 
            WHERE IMA01 = :pNO
            """, nativeQuery = true)
    Optional<IMA_FILE> findProductByPnoRaw(@Param("pNO") String pNO);

    // 1. 依據日期區間查詢基礎商品
    @Query(value = """
            SELECT 
                i.IMA01, i.IMA02, i.TA_IMA01, i.IMA15, i.IMA127, i.IMA128, i.IMA131, 
                DATE_FORMAT(i.IMADATE, '%Y-%m-%d') AS IMADATE,
                i.OBA02, i.IMA1005, i.TQA02, i.IMA25, i.IMA54, i.RTG05
            FROM IMA_FILE i
            WHERE i.IMADATE BETWEEN STR_TO_DATE(:sFromDate, '%Y-%m-%d') 
                                AND STR_TO_DATE(:sToDate, '%Y-%m-%d')
            """, nativeQuery = true)
    List<IMA_FILE> getProductByDate(@Param("sFromDate") String sFromDate, @Param("sToDate") String sToDate);

    // 2. 依據日期區間查詢變價商品
    @Query(value = """
            SELECT 
                i.IMA01, i.IMA02, i.TA_IMA01, i.IMA15, i.IMA127, i.IMA128, i.IMA131, 
                DATE_FORMAT(i.IMADATE, '%Y-%m-%d') AS IMADATE,
                i.OBA02, i.IMA1005, i.TQA02, i.IMA25, i.IMA54, i.RTG05
            FROM IMA_FILE i
            WHERE i.IMA01 IN (
                --  這裡請依你原本舊系統 getChangePriceByDate 實際的業務邏輯 Table 微調
                -- 範例：SELECT p_no FROM price_change_table WHERE change_date BETWEEN ...
                SELECT IMA01 FROM IMA_FILE WHERE IMADATE BETWEEN STR_TO_DATE(:sFromDate, '%Y-%m-%d') AND STR_TO_DATE(:sToDate, '%Y-%m-%d')
            )
            """, nativeQuery = true)
    List<IMA_FILE> getChangePriceByDate(@Param("sFromDate") String sFromDate, @Param("sToDate") String sToDate);
    
    
    
}