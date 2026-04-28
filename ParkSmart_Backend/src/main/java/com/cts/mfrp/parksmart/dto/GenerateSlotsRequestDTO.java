package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;

public class GenerateSlotsRequestDTO {
    @NotNull(message = "Space ID is required")
    private Integer spaceId;

    @NotNull(message = "Number of levels is required")
    @Min(value = 1, message = "At least 1 level required")
    @Max(value = 10, message = "Maximum 10 levels")
    private Integer levels;

    @NotNull(message = "Slots per level is required")
    @Min(value = 1, message = "At least 1 slot per level")
    @Max(value = 100, message = "Maximum 100 slots per level")
    private Integer slotsPerLevel;

    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public Integer getLevels() { return levels; }
    public void setLevels(Integer levels) { this.levels = levels; }
    public Integer getSlotsPerLevel() { return slotsPerLevel; }
    public void setSlotsPerLevel(Integer slotsPerLevel) { this.slotsPerLevel = slotsPerLevel; }
}
