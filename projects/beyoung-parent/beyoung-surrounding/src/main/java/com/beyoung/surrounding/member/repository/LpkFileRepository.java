package com.beyoung.surrounding.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.app.entity.LPK_FILE;

@Repository("memoryLpkFileRepository")
public interface LpkFileRepository extends JpaRepository<LPK_FILE, String> {

}