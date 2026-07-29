package com.beyond.surrounding.spos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.spos.entity.IMA_FILE;

import java.util.List;

@Repository
public interface IMAFILERepository extends JpaRepository<IMA_FILE, String> { 
    @Query(value = """
            SELECT IMA01, IMA02, IMA15, IMA127, IMA128 
            FROM IMA_FILE 
            WHERE IMA01 = :sPNO
            """, 
           nativeQuery = true)
    List<Object[]> findProductByPNORaw(@Param("sPNO") String sPNO);
}