package com.cts.mfrp.parksmart.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

/**
 * Unified request DTO for slot availability check and slot hold.
 * slotIds is only required for the hold endpoint.
 */
public class SlotRequestDTO {

    private Integer spaceId;

    @NotBlank(message = "Arrival time is required")
    private String arrival;

    @NotBlank(message = "Leaving time is required")
    private String leaving;

    // Only required for hold endpoint
    private List<Integer> slotIds;

    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    public String getLeaving() { return leaving; }
    public void setLeaving(String leaving) { this.leaving = leaving; }
    public List<Integer> getSlotIds() { return slotIds; }
    public void setSlotIds(List<Integer> slotIds) { this.slotIds = slotIds; }
}
