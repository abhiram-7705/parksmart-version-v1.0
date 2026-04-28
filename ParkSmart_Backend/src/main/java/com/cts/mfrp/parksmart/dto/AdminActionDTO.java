package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.NotNull;

public class AdminActionDTO {
    @NotNull(message = "ID is required")
    private Integer id;
    private String reason; // optional rejection reason

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
