package com.beyoung.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LpkFileRepository extends JpaRepository<LpkFile, String> {

}