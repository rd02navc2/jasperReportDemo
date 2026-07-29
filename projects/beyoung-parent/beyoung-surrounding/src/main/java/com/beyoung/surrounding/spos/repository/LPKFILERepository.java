package com.beyoung.surrounding.spos.repository;

import com.beyoung.surrounding.spos.entity.LPK_FILE; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LPKFILERepository extends JpaRepository<LPK_FILE, String> {

    /**
     * 對應舊專案 MembershipDAOImpl.getMemberByCardID 的 Native SQL 查詢
     */
	@Query(value = """
            SELECT lpk.lpk04, lpk.lpk18 
            FROM lpk_file lpk, lpj_file lpj 
            WHERE lpk.lpk01 = lpj.lpj01 
              AND (lpj.lpj01 = (SELECT sub.lpj01 FROM lpj_file sub WHERE sub.lpj03 = :cardID) 
                   OR lpk.lpk18 = :cardID) 
              AND lpj.lpj09 = '2' 
              AND lpj.ta_lpj04 = 'Y'
            """, 
           nativeQuery = true)
    List<Object[]> findMemberByCardIDRaw(@Param("cardID") String cardID);
}