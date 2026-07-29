package com.beyoung.parking.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.Optional;

public interface LrqFileRepository extends JpaRepository<LrqFile, LrqFileId> {

    @Query(value = "SELECT lrq03 FROM lrq_file " +
                   "WHERE lrqacti = 'Y' " +
                   "AND :today BETWEEN lrq10 AND lrq11 " +
                   "AND lrq02 = :lrq02 AND lrqplant = :center", nativeQuery = true)
    Optional<Integer> findLrq03(@Param("lrq02") String lrq02, @Param("center") String center, @Param("today") Date today);
}