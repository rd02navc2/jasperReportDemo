package com.beyoung.surrounding.spos.repository;

import com.beyoung.surrounding.spos.entity.TC_PSA_FILE;
import com.beyoung.surrounding.spos.entity.TC_PSA_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TCPSAFILERepository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {
    
    @Query(value = """
            SELECT * FROM TC_PSA_FILE 
            WHERE TC_PSAPLANT = :tcPsaPlant 
              AND TC_PSA01 = :tcPsa01 
              AND TC_PSA02 = :tcPsa02 
              AND TC_PSA03 = :tcPsa03 
              AND TC_PSA04 = :tcPsa04
            """, 
           nativeQuery = true)
    Optional<TC_PSA_FILE> findByCustomKey(
        @Param("tcPsaPlant") String tcPsaPlant, 
        @Param("tcPsa01") String tcPsa01, 
        @Param("tcPsa02") String tcPsa02, 
        @Param("tcPsa03") String tcPsa03, 
        @Param("tcPsa04") String tcPsa04
    );
}