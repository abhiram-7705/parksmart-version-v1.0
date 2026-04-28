package com.cts.mfrp.parksmart.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cts.mfrp.parksmart.model.SpaceApprovalRequest;

@Repository
public interface SpaceApprovalRequestRepository extends JpaRepository<SpaceApprovalRequest, Integer> {
    List<SpaceApprovalRequest> findByOwnerEmailOrderBySubmittedAtDesc(String email);
    List<SpaceApprovalRequest> findAllByOrderBySubmittedAtDesc();
    List<SpaceApprovalRequest> findByStatusOrderBySubmittedAtDesc(String status);
    Optional<SpaceApprovalRequest> findBySpaceSpaceId(Integer spaceId);
    long countByStatus(String status);
}
