package com.cts.mfrp.parksmart.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "space_approval_requests")
public class SpaceApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_id")
    private Integer approvalId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    // The pending space created (isActive=false until approved)
    @OneToOne
    @JoinColumn(name = "space_id")
    private ParkingSpaces space;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "submitted_at")
    @CreationTimestamp
    private LocalDateTime submittedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    // Snapshot of submitted data
    @Column(name = "submitted_name")
    private String submittedName;
    @Column(name = "submitted_type")
    private String submittedType;
    @Column(name = "submitted_price")
    private double submittedPrice;
    @Column(name = "submitted_location")
    private String submittedLocation;
    @Column(name = "submitted_city")
    private String submittedCity;
    @Column(name = "submitted_latitude")
    private double submittedLatitude;
    @Column(name = "submitted_longitude")
    private double submittedLongitude;
    @Column(name = "submitted_cctv")
    private boolean submittedCctv;
    @Column(name = "submitted_ev")
    private boolean submittedEv;
    @Column(name = "submitted_guarded")
    private boolean submittedGuarded;
    @Column(name = "submitted_covered")
    private boolean submittedCovered;
    @Column(name = "submitted_notes", length = 1000)
    private String submittedNotes;

    public Integer getApprovalId() { return approvalId; }
    public void setApprovalId(Integer approvalId) { this.approvalId = approvalId; }
    public Users getOwner() { return owner; }
    public void setOwner(Users owner) { this.owner = owner; }
    public ParkingSpaces getSpace() { return space; }
    public void setSpace(ParkingSpaces space) { this.space = space; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getSubmittedName() { return submittedName; }
    public void setSubmittedName(String submittedName) { this.submittedName = submittedName; }
    public String getSubmittedType() { return submittedType; }
    public void setSubmittedType(String submittedType) { this.submittedType = submittedType; }
    public double getSubmittedPrice() { return submittedPrice; }
    public void setSubmittedPrice(double submittedPrice) { this.submittedPrice = submittedPrice; }
    public String getSubmittedLocation() { return submittedLocation; }
    public void setSubmittedLocation(String submittedLocation) { this.submittedLocation = submittedLocation; }
    public String getSubmittedCity() { return submittedCity; }
    public void setSubmittedCity(String submittedCity) { this.submittedCity = submittedCity; }
    public double getSubmittedLatitude() { return submittedLatitude; }
    public void setSubmittedLatitude(double submittedLatitude) { this.submittedLatitude = submittedLatitude; }
    public double getSubmittedLongitude() { return submittedLongitude; }
    public void setSubmittedLongitude(double submittedLongitude) { this.submittedLongitude = submittedLongitude; }
    public boolean isSubmittedCctv() { return submittedCctv; }
    public void setSubmittedCctv(boolean submittedCctv) { this.submittedCctv = submittedCctv; }
    public boolean isSubmittedEv() { return submittedEv; }
    public void setSubmittedEv(boolean submittedEv) { this.submittedEv = submittedEv; }
    public boolean isSubmittedGuarded() { return submittedGuarded; }
    public void setSubmittedGuarded(boolean submittedGuarded) { this.submittedGuarded = submittedGuarded; }
    public boolean isSubmittedCovered() { return submittedCovered; }
    public void setSubmittedCovered(boolean submittedCovered) { this.submittedCovered = submittedCovered; }
    public String getSubmittedNotes() { return submittedNotes; }
    public void setSubmittedNotes(String submittedNotes) { this.submittedNotes = submittedNotes; }
}
