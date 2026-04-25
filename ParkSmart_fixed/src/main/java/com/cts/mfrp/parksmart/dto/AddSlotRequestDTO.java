package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;

public class AddSlotRequestDTO {
    @NotNull
    private Integer spaceId;

    @NotBlank(message = "Slot name is required")
    @Pattern(regexp = "^[A-Za-z]+[0-9]+$", message = "Slot name must be letters followed by numbers (e.g. A1)")
    private String slotNumber;

    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
}
