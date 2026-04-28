package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

public class SpaceApprovalDTO {
    private Integer approvalId;
    private Integer spaceId;
    private String ownerName;
    private String ownerEmail;
    private String status;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;
    private String name;
    private String type;
    private double price;
    private String location;
    private String city;
    private double latitude;
    private double longitude;
    private boolean cctv;
    private boolean ev;
    private boolean guarded;
    private boolean covered;
    private String notes;

    public Integer getApprovalId() { return approvalId; }
    public void setApprovalId(Integer approvalId) { this.approvalId = approvalId; }
    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public boolean isCctv() { return cctv; }
    public void setCctv(boolean cctv) { this.cctv = cctv; }
    public boolean isEv() { return ev; }
    public void setEv(boolean ev) { this.ev = ev; }
    public boolean isGuarded() { return guarded; }
    public void setGuarded(boolean guarded) { this.guarded = guarded; }
    public boolean isCovered() { return covered; }
    public void setCovered(boolean covered) { this.covered = covered; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
