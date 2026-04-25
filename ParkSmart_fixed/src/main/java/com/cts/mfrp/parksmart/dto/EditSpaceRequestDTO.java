package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;

public class EditSpaceRequestDTO {
    @NotNull
    private Integer spaceId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank
    @Pattern(regexp = "PUBLIC|PRIVATE")
    private String type;

    @NotNull @Positive
    private Double pricePerHour;

    private boolean cctv;
    private boolean evCharging;
    private boolean guarded;
    private boolean coveredFence;
    private String notes;

    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
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
