
package com.faultlens.backend.dto;

import com.faultlens.backend.entity.Log;
import java.time.LocalDateTime;

public class LogResponse {

    private Long id;
    private Long serviceId;
    private String level;
    private String message;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;

    public LogResponse() {
    }

    public LogResponse(
            Long id,
            Long serviceId,
            String level,
            String message,
            LocalDateTime timestamp,
            LocalDateTime createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
        this.createdAt = createdAt;
    }

    public static LogResponse fromEntity(Log log) {
        return new LogResponse(
                log.getId(),
                log.getService().getId(),
                log.getLevel(),
                log.getMessage(),
                log.getTimestamp(),
                log.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

