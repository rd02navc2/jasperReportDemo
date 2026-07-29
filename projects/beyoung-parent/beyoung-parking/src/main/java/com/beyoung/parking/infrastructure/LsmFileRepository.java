package com.beyoung.parking.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LsmFileRepository extends JpaRepository<LsmFile, LsmFileId> {
}