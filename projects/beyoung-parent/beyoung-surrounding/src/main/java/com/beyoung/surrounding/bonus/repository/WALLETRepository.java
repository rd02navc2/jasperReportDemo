package com.beyoung.surrounding.bonus.repository;

import com.beyoung.surrounding.app.entity.WALLET;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WALLETRepository extends JpaRepository<WALLET, String> {
    // JpaRepository 已經內建了 findAll() 方法，足以取代原本的 getWallet()
}