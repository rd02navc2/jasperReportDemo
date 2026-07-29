package com.beyond.surrounding.spos.repository;

import com.beyond.surrounding.spos.entity.TC_PSC_FILE;
import com.beyond.surrounding.spos.entity.TC_PSC_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TCPSCFILERepository extends JpaRepository<TC_PSC_FILE, TC_PSC_FILE_ComposeKey> {
    
    @Query(value = """
            SELECT * FROM TC_PSC_FILE 
            WHERE TC_PSCPLANT = :tcPscPlant 
              AND TC_PSC01 = :tcPsc01 
              AND TC_PSC02 = :tcPsc02 
              AND TC_PSC03 = :tcPsc03
            """, 
           nativeQuery = true)
    List<TC_PSC_FILE> findByCustomKey(
        @Param("tcPscPlant") String tcPscPlant, 
        @Param("tcPsc01") String tcPsc01, 
        @Param("tcPsc02") String tcPsc02, 
        @Param("tcPsc03") String tcPsc03
    );
}