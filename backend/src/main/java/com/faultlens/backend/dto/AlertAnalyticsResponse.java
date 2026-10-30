package com.faultlens.backend.dto;

public class AlertAnalyticsResponse {

    private long totalAlerts;
    private long openAlerts;
    private long resolvedAlerts;

    public AlertAnalyticsResponse(long totalAlerts, long openAlerts,
            long resolvedAlerts) {
        this.totalAlerts = totalAlerts;
        this.openAlerts = openAlerts;
        this.resolvedAlerts = resolvedAlerts;
    }

    public long getTotalAlerts() { return totalAlerts; }
    public long getOpenAlerts() { return openAlerts; }
    public long getResolvedAlerts() { return resolvedAlerts; }
}