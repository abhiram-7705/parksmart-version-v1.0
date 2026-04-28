package com.cts.mfrp.parksmart.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cts.mfrp.parksmart.model.ContactRequests;

@Repository
public interface ContactRequestsRepository extends JpaRepository<ContactRequests, Integer> {

    @Query("SELECT c FROM ContactRequests c WHERE c.parkingSpace.spaceId = :spaceId ORDER BY c.requestedAt DESC")
    List<ContactRequests> findBySpaceId(@Param("spaceId") Integer spaceId);

    @Query("SELECT c FROM ContactRequests c JOIN c.parkingSpace ps WHERE ps.owner.email = :ownerEmail ORDER BY c.requestedAt DESC")
    List<ContactRequests> findByOwnerEmail(@Param("ownerEmail") String ownerEmail);

    @Query("SELECT COUNT(c) FROM ContactRequests c JOIN c.parkingSpace ps WHERE ps.owner.email = :ownerEmail AND c.isRead = false")
    long countUnreadByOwnerEmail(@Param("ownerEmail") String ownerEmail);

    @Modifying
    @Query("UPDATE ContactRequests c SET c.isRead = true WHERE c.requestId = :id")
    void markAsRead(@Param("id") Integer id);
}
