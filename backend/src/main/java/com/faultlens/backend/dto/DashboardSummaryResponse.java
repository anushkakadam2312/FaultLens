package com.faultlens.backend.dto;

public class DashboardSummaryResponse {

    private long totalServices;
    private long totalLogs;
    private long totalOpenAlerts;
    private long totalResolvedAlerts;

    public DashboardSummaryResponse(long totalServices, long totalLogs,
            long totalOpenAlerts, long totalResolvedAlerts) {
        this.totalServices = totalServices;
        this.totalLogs = totalLogs;
        this.totalOpenAlerts = totalOpenAlerts;
        this.totalResolvedAlerts = totalResolvedAlerts;
    }

    public long getTotalServices() { return totalServices; }
    public long getTotalLogs() { return totalLogs; }
    public long getTotalOpenAlerts() { return totalOpenAlerts; }
    public long getTotalResolvedAlerts() { return totalResolvedAlerts; }
}