package com.cts.mfrp.parksmart.dto;

import java.time.LocalDateTime;

public class ContactRequestResponseDTO {
    private Integer requestId;
    private String userName;
    private String userEmail;
    private String name;
    private long phone;
    private String email;
    private String message;
    private String preferredContact;
    private String preferredTime;
    private LocalDateTime requestedAt;
    private boolean isRead;
    private Integer spaceId;
    private String spaceName;

    public Integer getRequestId() { return requestId; }
    public void setRequestId(Integer requestId) { this.requestId = requestId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public long getPhone() { return phone; }
    public void setPhone(long phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPreferredContact() { return preferredContact; }
    public void setPreferredContact(String preferredContact) { this.preferredContact = preferredContact; }
    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
    public Integer getSpaceId() { return spaceId; }
    public void setSpaceId(Integer spaceId) { this.spaceId = spaceId; }
    public String getSpaceName() { return spaceName; }
    public void setSpaceName(String spaceName) { this.spaceName = spaceName; }
}
