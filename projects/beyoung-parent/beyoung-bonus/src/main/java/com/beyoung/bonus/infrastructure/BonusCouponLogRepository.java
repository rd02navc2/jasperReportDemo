package com.beyoung.bonus.infrastructure;

import com.beyoung.bonus.domain.entity.BonusCouponLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusCouponLogRepository extends JpaRepository<BonusCouponLog, Long> {
}