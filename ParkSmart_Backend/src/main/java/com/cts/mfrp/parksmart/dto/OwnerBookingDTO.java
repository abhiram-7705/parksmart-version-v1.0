package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

public class OwnerBookingDTO {
    private Integer bookingId;
    private String slotNumber;
    private String vehicleNumber;
    private LocalDateTime arrival;
    private LocalDateTime leaving;
    private LocalDateTime bookedAt;
    private double amount;
    private boolean refundable;
    private String status;

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public LocalDateTime getArrival() { return arrival; }
    public void setArrival(LocalDateTime arrival) { this.arrival = arrival; }
    public LocalDateTime getLeaving() { return leaving; }
    public void setLeaving(LocalDateTime leaving) { this.leaving = leaving; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isRefundable() { return refundable; }
    public void setRefundable(boolean refundable) { this.refundable = refundable; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
