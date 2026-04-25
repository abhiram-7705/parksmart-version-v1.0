package com.cts.mfrp.parksmart.dto;

import java.time.LocalDate;

public class TimelineRequestDTO {
    private Integer spaceId;
    private LocalDate date;
    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
