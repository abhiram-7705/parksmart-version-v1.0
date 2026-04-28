package com.cts.mfrp.parksmart.dto;

import jakarta.validation.constraints.*;

public class WalletRequestCreateDTO {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.0", message = "Minimum amount is ₹10")
    @DecimalMax(value = "2000.0", message = "Maximum amount is ₹2000")
    private Double amount;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
