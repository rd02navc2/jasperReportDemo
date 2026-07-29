package com.beyond.surrounding.pos2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LQE_FILE;
import java.util.List;

@Repository
public interface MemberPos2Repository extends JpaRepository<LQE_FILE, String> {

	@Query(value = """
            SELECT 
                lpk.lpk01, lpk.lpk03, lpk.lpk04, lpk.lpk18, 
                lpj.lpj03, lpj.lpj12, lpj.ta_lpj01, lpj.ta_lpj02, lpj.ta_lpj03, 
                lpk.lpkud02, lpk.lpk14
            FROM lpk_file lpk, lpj_file lpj
            WHERE lpk.lpk01 = lpj.lpj01
              AND (lpj.lpj01 = (SELECT tmp.lpj01 FROM lpj_file tmp WHERE tmp.lpj03 = :cardID) 
                   OR lpk.lpk18 = :cardID 
                   OR lpk.lpk03 = :cardID) 
              AND SUBSTR(lpj.lpj03, 1, 1) NOT IN ('0', '1')
              AND lpj.lpj09 = '2' 
              AND lpj.ta_lpj04 = 'Y'
            """, nativeQuery = true)
    List<Object[]> findMemberByCardIDRaw(@Param("cardID") String cardID);
    
    
}