package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

public class TimelineBookingDTO {
    private String vehicleNumber;
    private LocalDateTime arrival;
    private LocalDateTime leaving;
    private String status;

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public LocalDateTime getArrival() { return arrival; }
    public void setArrival(LocalDateTime arrival) { this.arrival = arrival; }
    public LocalDateTime getLeaving() { return leaving; }
    public void setLeaving(LocalDateTime leaving) { this.leaving = leaving; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
