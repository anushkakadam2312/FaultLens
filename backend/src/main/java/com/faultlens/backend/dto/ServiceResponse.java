package com.faultlens.backend.dto;

import com.faultlens.backend.entity.Environment;

import java.time.LocalDateTime;

public class ServiceResponse {

    private Long id;
    private String name;
    private String description;
    private Environment environment;
    private LocalDateTime createdAt;

    public ServiceResponse() {
    }

    public ServiceResponse(
            Long id,
            String name,
            String description,
            Environment environment,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.environment = environment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
