package com.cts.mfrp.parksmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.mfrp.parksmart.model.ParkingSlots;

@Repository
public interface ParkingSlotsRepository extends JpaRepository<ParkingSlots, Integer> {

    @Query("SELECT s FROM ParkingSlots s WHERE s.parkingSpace.spaceId = :spaceId")
    List<ParkingSlots> findBySpaceId(@Param("spaceId") Integer spaceId);
}
