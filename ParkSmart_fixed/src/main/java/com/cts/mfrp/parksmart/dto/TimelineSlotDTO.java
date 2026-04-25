package com.cts.mfrp.parksmart.dto;

import java.util.List;

public class TimelineSlotDTO {
    private String slotNumber;
    private List<TimelineBookingDTO> bookings;

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public List<TimelineBookingDTO> getBookings() { return bookings; }
    public void setBookings(List<TimelineBookingDTO> bookings) { this.bookings = bookings; }
}
