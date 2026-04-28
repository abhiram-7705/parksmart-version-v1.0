package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Unified request DTO for all booking flow endpoints:
 *   - initiate  (holdId, spaceId, arrival, leaving)
 *   - apply-promo (+ promoCode)
 *   - confirm   (+ promoCode, vehicleNumber)
 * Extra fields are simply ignored by endpoints that don't need them.
 */
public class BookingRequestDTO {

    @NotBlank(message = "Hold ID is required")
    private String holdId;

    @NotNull(message = "Space ID is required")
    private Integer spaceId;

    @NotNull(message = "Arrival is required")
    private LocalDateTime arrival;

    @NotNull(message = "Leaving is required")
    private LocalDateTime leaving;

    // Used by apply-promo and confirm
    private String promoCode;

    // Used by confirm only
    private String vehicleNumber;

    public String getHoldId() { return holdId; }
    public void setHoldId(String holdId) { this.holdId = holdId; }
    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public LocalDateTime getArrival() { return arrival; }
    public void setArrival(LocalDateTime arrival) { this.arrival = arrival; }
    public LocalDateTime getLeaving() { return leaving; }
    public void setLeaving(LocalDateTime leaving) { this.leaving = leaving; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
}
