package com.cts.mfrp.parksmart.dto;

public class OwnerSlotDTO {
    private Integer slotId;
    private String slotNumber;
    private String status;
    private String vehicleNumber;
    private boolean hasActiveBookings;

    public Integer getSlotId() { return slotId; }
    public void setSlotId(Integer slotId) { this.slotId = slotId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public boolean isHasActiveBookings() { return hasActiveBookings; }
    public void setHasActiveBookings(boolean hasActiveBookings) { this.hasActiveBookings = hasActiveBookings; }
}
