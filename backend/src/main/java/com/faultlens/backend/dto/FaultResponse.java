
package com.faultlens.backend.dto;

public class FaultResponse {

    private Long serviceId;
    private String serviceName;
    private boolean faultDetected;
    private long errorCount;
    private int windowSeconds;
    private String message;

    public FaultResponse(
            Long serviceId,
            String serviceName,
            boolean faultDetected,
            long errorCount,
            int windowSeconds,
            String message
    ) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.faultDetected = faultDetected;
        this.errorCount = errorCount;
        this.windowSeconds = windowSeconds;
        this.message = message;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isFaultDetected() {
        return faultDetected;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public String getMessage() {
        return message;
    }
}

