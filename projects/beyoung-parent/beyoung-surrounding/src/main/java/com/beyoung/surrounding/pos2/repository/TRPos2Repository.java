package com.beyoung.surrounding.pos2.repository;

import com.beyoung.surrounding.pos2.entity.TR;
import com.beyoung.surrounding.pos2.entity.TR_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TRPos2Repository extends JpaRepository<TR, TR_ComposeKey> {
	
    List<TR> findBySalDateAndStoreNoAndPosNoAndTrnNo(String salDate, String storeNo, String posNo, String trnNo);
}