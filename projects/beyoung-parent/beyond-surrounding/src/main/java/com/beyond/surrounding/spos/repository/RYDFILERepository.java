package com.beyond.surrounding.spos.repository;

import com.beyond.surrounding.spos.entity.RYD_FILE;
import com.beyond.surrounding.spos.entity.RYD_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RYDFILERepository extends JpaRepository<RYD_FILE, RYD_FILE_ComposeKey> {
    // 這樣一來，內建的 findById() 就會要求傳入 RYD_FILE_ComposeKey 物件囉！
}