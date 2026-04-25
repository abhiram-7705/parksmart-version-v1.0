package com.cts.mfrp.parksmart.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.mfrp.parksmart.model.SlotHold;

@Repository
public interface SlotHoldRepository
        extends JpaRepository<SlotHold, Integer> {

    boolean existsBySlotSlotIdAndArrivalLessThanAndLeavingGreaterThanAndExpiresAtAfter(
        Integer slotId,
        LocalDateTime arrival,
        LocalDateTime leaving,
        LocalDateTime now
    );

    List<SlotHold> findByHoldGroupId(String holdGroupId);

    void deleteByHoldGroupIdAndUserEmail(String holdGroupId, String email);

    void deleteByExpiresAtBefore(LocalDateTime time);
}
