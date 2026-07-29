package com.beyond.surrounding.exchange.repository;

import com.beyond.surrounding.app.entity.LPQ_FILE;
import com.beyond.surrounding.app.entity.LPR_FILE;
import com.beyond.surrounding.app.entity.TC_PSA_FILE; //  確保 import 的是您這一個 Entity
import com.beyond.surrounding.app.entity.TC_PSA_FILE_ComposeKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
//  關鍵修正：將主鍵 ID 型態由 TcPsaFileId 改為 Integer
public interface ExchangeRepository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {

	@Query(value = """
        SELECT LPQ01, LPQ03, LPQPLANT, LPQ00, LPQ13, 
               LPQ02, LPQ04, LPQ05, LPQ06, LPQ07, LPQ08, LPQ09, 
               LPQ10, LPQ11, LPQACTI, LPQCRAT, LPQDATE, 
               LPQGRUP, LPQMODU, LPQUSER, LPQORIU, LPQORIG, 
               LPQLEGAL, LPQPOS, LPQ12, LPQ14, LPQ15, LPQ16, 
               LPQ17, LPQ18, LPQ19, LPQ20, TA_LPQ01
        FROM LPQ_FILE 
        WHERE LPQ00 = '0' 
        AND LPQ01 LIKE 'APP%' 
        AND LPQ03 = :cardType 
        AND LPQPLANT = :plant 
        AND LPQ08 = 'Y' 
        AND LPQ15 = 'Y' 
        AND CURRENT_DATE BETWEEN LPQ04 AND LPQ05
        """, nativeQuery = true)
	List<LPQ_FILE> findExchangeSetting(@Param("cardType") String cardType, @Param("plant") String plant);

	@Query(value = """
		    SELECT lpr01, lpr02, lpr03, lpr05, lpr06, lpr00, lpr09, lprplant, lpr08, lpr04, lpr07, lprlegal, lprpos, ta_lpr01, ta_lpr02, ta_lpr03
		    FROM lpr_file 
		    WHERE lpr01 = :lpq01 
		    AND lprplant = :plant 
		    AND lpr00 = '0' 
		    AND lpr09 = :cardType
		    """, nativeQuery = true)
    List<LPR_FILE> findExchangeDetails(@Param("lpq01") String lpq01, @Param("plant") String plant, @Param("cardType") String cardType);
	
}