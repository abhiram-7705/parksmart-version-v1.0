package com.cts.mfrp.parksmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.mfrp.parksmart.model.Reviews;

@Repository
public interface ReviewsRepository
        extends JpaRepository<Reviews, Integer> {

    @Query("""
        SELECT COUNT(r) > 0 FROM Reviews r
        JOIN Bookings b ON (b.user.userId = r.user.userId AND b.parkingSlot.parkingSpace.spaceId = r.parkingSpace.spaceId)
        WHERE b.bookingId = :bookingId
        """)
    boolean existsByBookingId(@Param("bookingId") Integer bookingId);
}