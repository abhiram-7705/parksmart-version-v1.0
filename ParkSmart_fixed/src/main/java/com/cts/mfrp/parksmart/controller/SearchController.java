package com.cts.mfrp.parksmart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.mfrp.parksmart.dto.LocationSuggestionDTO;
import com.cts.mfrp.parksmart.dto.ParkingCardDTO;
import com.cts.mfrp.parksmart.dto.SpaceSearchRequestDTO;
import com.cts.mfrp.parksmart.service.SearchService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/search")
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	

	@PostMapping("/location/suggest")
	public ResponseEntity<List<LocationSuggestionDTO>> suggestLocations(
	        @RequestBody Map<String, String> body) {

	    String query = body.get("query");

	    return ResponseEntity.ok(
	            searchService.getLocationSuggestions(query)
	    );
	}
	@PostMapping("/spaces/search")
	public ResponseEntity<List<ParkingCardDTO>> searchSpaces(
	        @Valid @RequestBody SpaceSearchRequestDTO request) {

	    return ResponseEntity.ok(searchService.searchSpaces(request));
	}


}
