package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookingInitiateRequestDTO {

    @NotBlank(message = "hold ID needed")
    private String holdId;

    @NotNull(message = "spaceID needed")
    private Integer spaceId;

    @NotNull(message = "arrival needed")
    private LocalDateTime arrival;

    @NotNull(message = "leaving needed")
    private LocalDateTime leaving;

	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}

	public Integer getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Integer spaceId) {
		this.spaceId = spaceId;
	}

	public LocalDateTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalDateTime arrival) {
		this.arrival = arrival;
	}

	public LocalDateTime getLeaving() {
		return leaving;
	}

	public void setLeaving(LocalDateTime leaving) {
		this.leaving = leaving;
	}
    
    
    
    
}