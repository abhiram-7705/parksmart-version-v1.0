package com.cts.mfrp.parksmart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.mfrp.parksmart.dto.LocationSuggestionDTO;
import com.cts.mfrp.parksmart.model.ParkingSpaces;

@Repository
public interface ParkingSpacesRepository extends JpaRepository<ParkingSpaces, Integer>{
	

	@Query("""
	    SELECT new com.cts.mfrp.parksmart.dto.LocationSuggestionDTO(
	        CONCAT(p.location, ', ', p.city),
	        p.latitude,
	        p.longitude
	    )
	    FROM ParkingSpaces p
	    WHERE LOWER(p.location) LIKE LOWER(CONCAT('%', :query, '%'))
	       OR LOWER(p.city) LIKE LOWER(CONCAT('%', :query, '%'))
	""")
	List<LocationSuggestionDTO> findLocationSuggestionsWithCoords(
	        @Param("query") String query
	);


	List<ParkingSpaces> findByIsActiveTrue();

	List<ParkingSpaces> findByOwnerUserId(int userId);

	Optional<ParkingSpaces> findBySpaceIdAndOwnerUserId(Integer spaceId, int userId);
	
	
}
