package com.cts.mfrp.parksmart.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.mfrp.parksmart.model.WalletRequest;

@Repository
public interface WalletRequestRepository extends JpaRepository<WalletRequest, Integer> {
    List<WalletRequest> findByUserEmailOrderByRequestedAtDesc(String email);
    List<WalletRequest> findAllByOrderByRequestedAtDesc();
    Optional<WalletRequest> findByUserEmailAndStatus(String email, String status);
    long countByStatus(String status);
}
