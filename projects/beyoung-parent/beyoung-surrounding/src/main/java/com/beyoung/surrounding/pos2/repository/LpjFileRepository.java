package com.beyoung.surrounding.pos2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.pos2.entity.LpjFile;

@Repository("pos2LpjFileRepository")
public interface LpjFileRepository extends JpaRepository<LpjFile, String> {
    
    // 假設主鍵或卡號是 lpj03，直接撈出 lpj09 的字串
	@Query(value = """
	        SELECT lpj09 FROM lpj_file WHERE lpj03 = :cardID
	        """, nativeQuery = true)
	String findLpj09ByCardID(@Param("cardID") String cardID);
	
	
}

