package com.cts.mfrp.parksmart.dto;

public class SlotDTO {
    private Integer slotId;
    private String slotNumber;
    private boolean isAvailable;
    private String status; // FREE, BLOCKED, OCCUPIED

    public Integer getSlotId() { return slotId; }
    public void setSlotId(Integer slotId) { this.slotId = slotId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
