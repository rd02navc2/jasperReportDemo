package com.beyoung.parking.infrastructure;

import com.beyoung.parking.domain.entity.ParkingCouponLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingCouponLogRepository extends JpaRepository<ParkingCouponLog, Long> {
}