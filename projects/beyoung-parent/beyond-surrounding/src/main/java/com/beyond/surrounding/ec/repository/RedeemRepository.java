package com.beyond.surrounding.ec.repository;

import com.beyond.surrounding.ec.entity.TC_LRJ_FILE;
import com.beyond.surrounding.ec.entity.TC_LRJ_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RedeemRepository extends JpaRepository<TC_LRJ_FILE, TC_LRJ_FILE_ComposeKey> {

	@Query(value = """
            SELECT j.* FROM tc_lri_file i, tc_lrj_file j
            WHERE i.tc_lri02 = j.tc_lrj01
              AND NOW() BETWEEN i.tc_lri06 AND i.tc_lri07
              AND i.tc_lriacti = 'Y'
              AND i.tc_lri05 = 'EC'
              AND j.tc_lrj02 = '606'
              AND j.tc_lrjacti = 'Y'
            """, nativeQuery = true)
    List<TC_LRJ_FILE> findRules();

    
    @Query(value = """
            SELECT * FROM tc_lrj_file 
            WHERE tc_lrj01 = 'RULE_EC_001' 
            LIMIT 1
            """, nativeQuery = true)
    TC_LRJ_FILE getRule();
    
}