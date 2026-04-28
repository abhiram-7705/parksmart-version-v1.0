package com.cts.mfrp.parksmart.dto;

public class AdminDashboardDTO {
    private long pendingWalletRequests;
    private long pendingSpaceApprovals;
    private long activePromoCodes;

    public long getPendingWalletRequests() { return pendingWalletRequests; }
    public void setPendingWalletRequests(long v) { this.pendingWalletRequests = v; }
    public long getPendingSpaceApprovals() { return pendingSpaceApprovals; }
    public void setPendingSpaceApprovals(long v) { this.pendingSpaceApprovals = v; }
    public long getActivePromoCodes() { return activePromoCodes; }
    public void setActivePromoCodes(long v) { this.activePromoCodes = v; }
}
