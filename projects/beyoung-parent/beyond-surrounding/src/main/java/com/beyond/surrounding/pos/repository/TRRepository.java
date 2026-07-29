package com.beyond.surrounding.pos.repository;

import com.beyond.surrounding.pos.entity.TR;
import com.beyond.surrounding.pos.entity.TR_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TRRepository extends JpaRepository<TR, TR_ComposeKey> {
    List<TR> findBySalDateAndStoreNoAndPosNoAndTrnNo(String salDate, String storeNo, String posNo, String trnNo);
}