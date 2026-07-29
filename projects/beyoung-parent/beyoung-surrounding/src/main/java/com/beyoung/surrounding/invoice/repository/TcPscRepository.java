package com.beyoung.surrounding.invoice.repository;

import com.beyoung.surrounding.app.entity.TC_PSC_FILE;
import com.beyoung.surrounding.app.entity.TC_PSC_FILE_ComposeKey;
import com.beyoung.surrounding.app.entity.TcPscProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * TcPscRepository
 * 對應 TC_PSC_FILE 的付款方式 / 信用卡明細查詢
 *
 * 對應舊系統 SQL：
 *   SELECT tc_psc07, tc_psc08
 *   FROM tc_psc_file
 *   WHERE tc_pscplant = ?
 *     AND tc_psc01 = ?
 *     AND tc_psc02 = ?
 *     AND tc_psc03 = ?
 *     AND tc_psc04 = ?
 *     AND tc_psc05 IN ('11','12','13','14')    -- 信用卡付款類型
 *     AND LENGTH(TRIM(tc_psc07)) > 0            -- 信用卡號不為空
 */
@Repository
public interface TcPscRepository extends JpaRepository<TC_PSC_FILE, TC_PSC_FILE_ComposeKey> {

    @Query(value = """
            SELECT b.tc_psc07, b.tc_psc08
            FROM tc_psc_file b
            WHERE tc_pscplant = :plant
              AND tc_psc01    = :psa01
              AND tc_psc02    = :psa02
              AND tc_psc03    = :psa03
              AND tc_psc04    = :psa04
              AND tc_psc05    IN ('11', '12', '13', '14')
              AND LENGTH(TRIM(tc_psc07)) > 0
            """, nativeQuery = true)
    List<TcPscProjection> findCreditCard(
            @Param("plant") String plant,
            @Param("psa01") String psa01,
            @Param("psa02") String psa02,
            @Param("psa03") String psa03,
            @Param("psa04") Date   psa04);
}
