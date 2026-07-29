package com.beyond.surrounding.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.erp.entity.TC_LRJ_FILE_ComposeKey;
import com.beyond.surrounding.erp.entity.TC_LRJ_FILE;
import java.util.List;

@Repository
public interface ErpTcLrjFileRepository extends JpaRepository<TC_LRJ_FILE, TC_LRJ_FILE_ComposeKey> {

	@Query(value = """
		    SELECT 
		        j.tc_lrj01 AS tc_lrj01, 
		        j.tc_lrj03 AS tc_lrj03, 
		        j.tc_lrj04 AS tc_lrj04, 
		        j.tc_lrj05 AS tc_lrj05
		    FROM tc_lri_file i
		    INNER JOIN tc_lrj_file j ON i.tc_lri02 = j.tc_lrj01 
		    WHERE NOW() BETWEEN i.tc_lri06 AND i.tc_lri07 
		      AND i.tc_lriacti = 'Y' 
		      AND i.tc_lri01 = :sCenter 
		      AND i.tc_lri05 = :sCardType 
		      AND j.tc_lrj02 = '606' 
		      AND j.tc_lrjacti = 'Y'
		    """, nativeQuery = true)
    List<ErpTcLrjRuleProjection> findActiveRules(@Param("sCenter") String sCenter, @Param("sCardType") String sCardType);
    
}