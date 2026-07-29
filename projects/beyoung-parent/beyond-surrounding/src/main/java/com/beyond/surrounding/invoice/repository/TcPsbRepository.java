package com.beyond.surrounding.invoice.repository;

import com.beyond.surrounding.app.entity.TC_PSB_FILE;
import com.beyond.surrounding.app.entity.TC_PSB_FILE_ComposeKey;
import com.beyond.surrounding.app.entity.TcPsbProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * TcPsbRepository
 * 對應 TC_PSB_FILE 的發票商品明細查詢
 *
 * 對應舊系統 SQL：
 *   SELECT tc_psb01, tc_psb02, tc_psb03, tc_psb04, tc_psb06,
 *          tc_psb09, tc_psb13, tc_psb19, ima25, lnt04
 *   FROM tc_psb_file
 *     LEFT JOIN ima_file ON tc_psb19 = ima01,
 *     lnt_file
 *   WHERE tc_psbplant = ?
 *     AND tc_psb01 = ?
 *     AND tc_psb02 = ?
 *     AND tc_psb03 = ?
 *     AND tc_psb04 = ?
 *     AND tc_psb01 = lnt06
 *     AND tc_psb04 BETWEEN lnt17 AND lnt18
 */
//dc- 原程式碼缺少資料表 ima_file  欄位定義; (6/14) 
@Repository
public interface TcPsbRepository extends JpaRepository<TC_PSB_FILE, TC_PSB_FILE_ComposeKey> {
    /*
    @Query(value = """
           SELECT b.*, --dc- i.ima25, l.lnt04
           FROM tc_psb_file b
           --dc- LEFT JOIN ima_file i ON b.tc_psb19 = i.ima01
           JOIN lnt_file l
             ON b.tc_psb01 = l.lnt06
            AND b.tc_psb04 BETWEEN l.lnt17 AND l.lnt18
           WHERE b.tc_psbplant = :plant
             AND b.tc_psb01    = :psa01
             AND b.tc_psb02    = :psa02
             AND b.tc_psb03    = :psa03
             AND b.tc_psb04    = :psa04
           """, nativeQuery = true)
    */
	
	@Query(value = """
		    SELECT 
		        b.TC_PSBPLANT AS tcPsbplant,
		        b.TC_PSB01 AS tcPsb01,
		        b.TC_PSB02 AS tcPsb02,
		        b.TC_PSB03 AS tcPsb03,
		        b.TC_PSB04 AS tcPsb04,
		        b.TC_PSB06 AS tcPsb06,
		        b.TC_PSB05 AS tcPsb05,
		        b.TC_PSB07 AS tcPsb07,
		        b.TC_PSB08 AS tcPsb08,
		        b.TC_PSB09 AS tcPsb09,
		        b.TC_PSB10 AS tcPsb10,
		        b.TC_PSB11 AS tcPsb11,
		        b.TC_PSB12 AS tcPsb12,
		        b.TC_PSB13 AS tcPsb13,
		        b.TC_PSB14 AS tcPsb14,
		        b.TC_PSB15 AS tcPsb15,
		        b.TC_PSB16 AS tcPsb16,
		        b.TC_PSB17 AS tcPsb17,
		        b.TC_PSB18 AS tcPsb18,
		        b.TC_PSB19 AS tcPsb19,
		        b.TC_PSB20 AS tcPsb20,
		        b.TC_PSB21 AS tcPsb21,
		        b.TC_PSB22 AS tcPsb22,
		        b.TC_PSB23 AS tcPsb23,
		        b.TC_PSB13A AS tcPsb13A,
		        b.TC_PSB13B AS tcPsb13B,
		        b.IMA25 AS ima25,
		        l.LNT04 AS lnt04 
		    FROM tc_psb_file b
		    JOIN lnt_file l ON b.tc_psb01 = l.lnt06 
		                   AND b.tc_psb04 BETWEEN l.lnt17 AND l.lnt18
		    WHERE b.tc_psbplant = :plant 
		      AND b.tc_psb01 = :psa01 
		      AND b.tc_psb02 = :psa02 
		      AND b.tc_psb03 = :psa03 
		      AND b.tc_psb04 = :psa04
		    """, nativeQuery = true)
		List<TcPsbProjection> findByInvoice(
		    @Param("plant") String plant,
		    @Param("psa01") String psa01,
		    @Param("psa02") String psa02,
		    @Param("psa03") String psa03,
		    @Param("psa04") Date   psa04);
}
