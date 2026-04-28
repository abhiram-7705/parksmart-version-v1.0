package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class PromoCodeCreateDTO {
    @NotBlank(message = "Code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,20}$", message = "Code must be 3-20 uppercase letters/numbers only")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "1.0", message = "Discount must be at least 1%")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    private Double discountPercentage;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
}
