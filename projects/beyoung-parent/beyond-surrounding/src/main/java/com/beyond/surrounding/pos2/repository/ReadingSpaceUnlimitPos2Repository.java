package com.beyond.surrounding.pos2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.beyond.surrounding.pos.entity.READING_SPACE_UNLIMIT;

//員工免扣點白名單
public interface ReadingSpaceUnlimitPos2Repository extends JpaRepository<READING_SPACE_UNLIMIT, String> {
 boolean existsByUserId(String userId); 
}