package com.beyoung.surrounding.pos.repository;

import com.beyoung.surrounding.pos.entity.TP;
import com.beyoung.surrounding.pos.entity.TP_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TPRepository extends JpaRepository<TP, TP_ComposeKey> {
    List<TP> findBySalDateAndStoreNoAndPosNoAndTrnNo(String salDate, String storeNo, String posNo, String trnNo);
}