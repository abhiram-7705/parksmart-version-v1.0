package com.cts.mfrp.parksmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.mfrp.parksmart.model.Reviews;

@Repository
public interface ReviewsRepository
        extends JpaRepository<Reviews, Integer> {

    @Query(
        value = "SELECT COUNT(*) > 0 FROM reviews WHERE booking_id = :bookingId",
        nativeQuery = true
    )
    boolean existsByBookingId(@Param("bookingId") Integer bookingId);
}