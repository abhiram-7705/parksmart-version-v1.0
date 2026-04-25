package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationSuggestionDTO {
	
	@NotBlank
    private String label;
	@NotBlank
    private Double latitude;
	@NotBlank
    private Double longitude;
	

    public LocationSuggestionDTO(String label, Double latitude, Double longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

    

}
