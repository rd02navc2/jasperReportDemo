package com.beyond.surrounding.invoice.repository;

import com.beyond.surrounding.app.entity.TC_PSA_FILE;
import com.beyond.surrounding.app.entity.TC_PSA_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TcPsaRepository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {

    // 根據發票號碼 (tcPsa03) 查詢發票實體
    // 假設發票號碼欄位名稱為 tcPsa03
    Optional<TC_PSA_FILE> findByTcPsa03(String tcPsa03);
    
}