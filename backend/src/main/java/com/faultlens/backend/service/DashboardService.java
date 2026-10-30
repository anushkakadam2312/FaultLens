package com.faultlens.backend.service;

import com.faultlens.backend.dto.*;
import com.faultlens.backend.entity.*;
import com.faultlens.backend.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final MonitoredServiceRepository serviceRepository;
    private final LogRepository logRepository;
    private final FaultEventRepository faultEventRepository;

    public DashboardService(
            MonitoredServiceRepository serviceRepository,
            LogRepository logRepository,
            FaultEventRepository faultEventRepository) {

        this.serviceRepository = serviceRepository;
        this.logRepository = logRepository;
        this.faultEventRepository = faultEventRepository;
    }

    // 1. Dashboard Summary

    public DashboardSummaryResponse getSummary() {

        long totalServices = serviceRepository.count();
        long totalLogs = logRepository.count();

        List<FaultEvent> events = faultEventRepository.findAll();

        long openAlerts = events.stream()
                .filter(e -> "OPEN".equals(e.getStatus()))
                .count();

        long resolvedAlerts = events.stream()
                .filter(e -> "RESOLVED".equals(e.getStatus()))
                .count();

        return new DashboardSummaryResponse(
                totalServices,
                totalLogs,
                openAlerts,
                resolvedAlerts
        );
    }

    // 2. Service Health Overview

    public List<ServiceHealthResponse> getServiceHealth() {

        List<MonitoredService> services = serviceRepository.findAll();
        List<Log> logs = logRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusSeconds(60);

        return services.stream().map(service -> {

            long errorCount = logs.stream()
                    .filter(log -> log.getService() != null
                            && log.getService().getId().equals(service.getId()))
                    .filter(log -> "ERROR".equals(log.getLevel()))
                    .filter(log -> log.getTimestamp() != null
                            && !log.getTimestamp().isBefore(startTime)
                            && !log.getTimestamp().isAfter(now))
                    .count();

            boolean healthy = errorCount < 5;

            return new ServiceHealthResponse(
                    service.getId(),
                    service.getName(),
                    service.getEnvironment().name(),
                    healthy,
                    errorCount,
                    healthy ? "HEALTHY" : "FAULT"
            );

        }).toList();
    }

    // 3. Alert Analytics

    public AlertAnalyticsResponse getAlertAnalytics() {

        List<FaultEvent> events = faultEventRepository.findAll();

        long totalAlerts = events.size();

        long openAlerts = events.stream()
                .filter(e -> "OPEN".equals(e.getStatus()))
                .count();

        long resolvedAlerts = events.stream()
                .filter(e -> "RESOLVED".equals(e.getStatus()))
                .count();

        return new AlertAnalyticsResponse(
                totalAlerts,
                openAlerts,
                resolvedAlerts
        );
    }

    // 4. Log Analytics

    public LogAnalyticsResponse getLogAnalytics() {

        List<Log> logs = logRepository.findAll();

        long totalLogs = logs.size();

        long infoLogs = logs.stream()
                .filter(log -> "INFO".equals(log.getLevel()))
                .count();

        long warnLogs = logs.stream()
                .filter(log -> "WARN".equals(log.getLevel()))
                .count();

        long errorLogs = logs.stream()
                .filter(log -> "ERROR".equals(log.getLevel()))
                .count();

        long debugLogs = logs.stream()
                .filter(log -> "DEBUG".equals(log.getLevel()))
                .count();

        return new LogAnalyticsResponse(
                totalLogs,
                infoLogs,
                warnLogs,
                errorLogs,
                debugLogs
        );
    }
}