package com.beyond.surrounding.ts.repository;

import com.beyond.surrounding.ts.entity.CUSTOMER_SERVICE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerServiceRepository extends JpaRepository<CUSTOMER_SERVICE, Long> {
    
    // 自動生成：select content from CUSTOMER_SERVICE where content like ?
    List<CUSTOMER_SERVICE> findByContentContaining(String content);
    
}