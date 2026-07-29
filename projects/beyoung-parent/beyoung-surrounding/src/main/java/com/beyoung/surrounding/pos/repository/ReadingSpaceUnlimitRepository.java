package com.beyoung.surrounding.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.beyoung.surrounding.pos.entity.READING_SPACE_UNLIMIT;


//員工免扣點白名單
public interface ReadingSpaceUnlimitRepository extends JpaRepository<READING_SPACE_UNLIMIT, String> {
 boolean existsByUserId(String userId); 
}