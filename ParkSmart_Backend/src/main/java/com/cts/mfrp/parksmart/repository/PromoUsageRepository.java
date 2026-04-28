package com.cts.mfrp.parksmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.mfrp.parksmart.model.PromoUsage;

@Repository
public interface PromoUsageRepository extends JpaRepository<PromoUsage, Integer> {
    boolean existsByUserEmailAndPromoCode(String email, String code);
}
