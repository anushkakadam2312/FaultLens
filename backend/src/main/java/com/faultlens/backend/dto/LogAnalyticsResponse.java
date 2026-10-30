package com.faultlens.backend.dto;

public class LogAnalyticsResponse {

    private long totalLogs;
    private long infoLogs;
    private long warnLogs;
    private long errorLogs;
    private long debugLogs;

    public LogAnalyticsResponse(long totalLogs, long infoLogs,
            long warnLogs, long errorLogs, long debugLogs) {
        this.totalLogs = totalLogs;
        this.infoLogs = infoLogs;
        this.warnLogs = warnLogs;
        this.errorLogs = errorLogs;
        this.debugLogs = debugLogs;
    }

    public long getTotalLogs() { return totalLogs; }
    public long getInfoLogs() { return infoLogs; }
    public long getWarnLogs() { return warnLogs; }
    public long getErrorLogs() { return errorLogs; }
    public long getDebugLogs() { return debugLogs; }
}