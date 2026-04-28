package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

public class PromoCodeDTO {
    private Integer promoId;
    private String code;
    private double discountPercentage;
    private boolean isActive;
    private LocalDateTime expiryDate;

    public Integer getPromoId() { return promoId; }
    public void setPromoId(Integer promoId) { this.promoId = promoId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double d) { this.discountPercentage = d; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
}
