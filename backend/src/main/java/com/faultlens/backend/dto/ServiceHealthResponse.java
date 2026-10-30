package com.faultlens.backend.dto;

public class ServiceHealthResponse {

    private Long serviceId;
    private String serviceName;
    private String environment;
    private boolean healthy;
    private long errorCount;
    private String status;

    public ServiceHealthResponse(Long serviceId, String serviceName,
            String environment, boolean healthy, long errorCount, String status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.environment = environment;
        this.healthy = healthy;
        this.errorCount = errorCount;
        this.status = status;
    }

    public Long getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getEnvironment() { return environment; }
    public boolean isHealthy() { return healthy; }
    public long getErrorCount() { return errorCount; }
    public String getStatus() { return status; }
}