package com.beyond.surrounding.pos2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.pos2.entity.TC_LRJ_FILE;
import com.beyond.surrounding.pos2.entity.TC_LRJ_FILE_ComposeKey;

@Repository
public interface RedeemPos2Repository extends JpaRepository<TC_LRJ_FILE, TC_LRJ_FILE_ComposeKey> { 

	@Query(value = """
	        SELECT r.* \
	        FROM tc_lri_file i, tc_lrj_file r \
	        WHERE i.tc_lri02 = r.tc_lrj01 \
	          AND sysdate() BETWEEN i.tc_lri06 AND i.tc_lri07 \
	          AND i.tc_lriacti = 'Y' \
	          AND i.tc_lri01 = :center \
	          AND i.tc_lri05 = :cardType \
	          AND r.tc_lrj02 = '601' \
	          AND r.tc_lrjacti = 'Y' \
	        """, nativeQuery = true)
	    List<TC_LRJ_FILE> findRulesNative(@Param("center") String center, @Param("cardType") String cardType);
	    
	
    
}