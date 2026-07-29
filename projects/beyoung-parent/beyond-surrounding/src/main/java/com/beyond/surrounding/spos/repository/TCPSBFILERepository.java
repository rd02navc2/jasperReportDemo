package com.beyond.surrounding.spos.repository;

import com.beyond.surrounding.spos.entity.TC_PSB_FILE;
import com.beyond.surrounding.spos.entity.TC_PSB_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TCPSBFILERepository extends JpaRepository<TC_PSB_FILE, TC_PSB_FILE_ComposeKey> {
    
    @Query(value = """
            SELECT * FROM TC_PSB_FILE 
            WHERE TC_PSBPLANT = :tcPsbPlant 
              AND TC_PSB01 = :tcPsb01 
              AND TC_PSB02 = :tcPsb02 
              AND TC_PSB03 = :tcPsb03 
              AND TC_PSB04 = :tcPsb04
            """, 
           nativeQuery = true)
    List<TC_PSB_FILE> findByCustomKey(
        @Param("tcPsbPlant") String tcPsbPlant, 
        @Param("tcPsb01") String tcPsb01, 
        @Param("tcPsb02") String tcPsb02, 
        @Param("tcPsb03") String tcPsb03, 
        @Param("tcPsb04") String tcPsb04
    );
}