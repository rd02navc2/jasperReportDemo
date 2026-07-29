package com.beyoung.surrounding.spos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.spos.entity.RYD_FILE;
import com.beyoung.surrounding.spos.entity.RYD_FILE_ComposeKey;
import java.util.List;

@Repository
public interface IMAFILERepository extends JpaRepository<RYD_FILE, RYD_FILE_ComposeKey> {

    @Query(value = """
            SELECT IMA01, IMA02, IMA15, IMA127, IMA128 
            FROM IMA_FILE 
            WHERE IMA01 = :sPNO
            """, 
           nativeQuery = true)
    List<Object[]> findProductByPNORaw(@Param("sPNO") String sPNO);
    
}