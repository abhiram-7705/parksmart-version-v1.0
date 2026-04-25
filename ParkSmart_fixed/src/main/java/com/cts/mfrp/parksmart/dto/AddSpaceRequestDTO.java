package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;

public class AddSpaceRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "PUBLIC|PRIVATE", message = "Type must be PUBLIC or PRIVATE")
    private String type;

    @NotNull(message = "Price per hour is required")
    @Positive(message = "Price must be positive")
    private Double pricePerHour;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private boolean cctv;
    private boolean evCharging;
    private boolean guarded;
    private boolean coveredFence;
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public boolean isCctv() { return cctv; }
    public void setCctv(boolean cctv) { this.cctv = cctv; }
    public boolean isEvCharging() { return evCharging; }
    public void setEvCharging(boolean evCharging) { this.evCharging = evCharging; }
    public boolean isGuarded() { return guarded; }
    public void setGuarded(boolean guarded) { this.guarded = guarded; }
    public boolean isCoveredFence() { return coveredFence; }
    public void setCoveredFence(boolean coveredFence) { this.coveredFence = coveredFence; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
