package com.cts.mfrp.parksmart.dto;

import java.util.List;

public class SpaceDetailDTO {
    private Integer spaceId;
    private String name;
    private String type;
    private String location;
    private String city;
    private double pricePerHour;
    private double latitude;
    private double longitude;
    private List<String> facilities;
    private String notes;
    private boolean active;
    private double rating;

    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public List<String> getFacilities() { return facilities; }
    public void setFacilities(List<String> facilities) { this.facilities = facilities; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
