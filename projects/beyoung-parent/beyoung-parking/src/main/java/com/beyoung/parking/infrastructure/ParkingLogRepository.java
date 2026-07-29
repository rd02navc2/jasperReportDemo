package com.beyoung.parking.infrastructure;

import com.beyoung.parking.domain.entity.ParkingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {
}