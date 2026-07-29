package com.beyond.surrounding.member.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LSM_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE_ComposeKey;

@Repository("memberLsmFileRepository")
public interface LsmFileRepository extends JpaRepository<LSM_FILE, LSM_FILE_ComposeKey> {

	@Query(value = """
            SELECT COALESCE(SUM(l.lsm08), 0.0) 
            FROM lsm_file l
            INNER JOIN lpj_file lpj ON lpj.lpj03 = l.lsm01
            WHERE lpj.lpj01 = :memberID 
              AND l.lsm05 BETWEEN :startDate AND :endDate
            """, nativeQuery = true)
    Double sumPointsByMemberID(
            @Param("memberID")  String memberID, 
            @Param("startDate") Date startDate, 
            @Param("endDate")   Date endDate
    );
}