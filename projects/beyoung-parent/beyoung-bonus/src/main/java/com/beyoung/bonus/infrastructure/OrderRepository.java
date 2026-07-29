package com.beyoung.bonus.infrastructure;

import com.beyoung.bonus.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // 繼承 JpaRepository 即擁有 save() 等基本功能
}