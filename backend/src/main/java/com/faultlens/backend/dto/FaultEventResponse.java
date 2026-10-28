
package com.faultlens.backend.dto;

import java.time.LocalDateTime;

public class FaultEventResponse {

    private Long id;
    private Long serviceId;
    private String serviceName;
    private long errorCount;
    private int windowSeconds;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime lastDetectedAt;
    private LocalDateTime resolvedAt;

    public FaultEventResponse(
            Long id,
            Long serviceId,
            String serviceName,
            long errorCount,
            int windowSeconds,
            String status,
            LocalDateTime startedAt,
            LocalDateTime lastDetectedAt,
            LocalDateTime resolvedAt
    ) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.errorCount = errorCount;
        this.windowSeconds = windowSeconds;
        this.status = status;
        this.startedAt = startedAt;
        this.lastDetectedAt = lastDetectedAt;
        this.resolvedAt = resolvedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getLastDetectedAt() {
        return lastDetectedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
}

