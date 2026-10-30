package com.faultlens.backend.controller;

import com.faultlens.backend.dto.*;
import com.faultlens.backend.service.DashboardService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // 1. Dashboard Summary
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    // 2. Service Health
    @GetMapping("/services")
    public List<ServiceHealthResponse> getServiceHealth() {
        return dashboardService.getServiceHealth();
    }

    // 3. Alert Analytics
    @GetMapping("/alerts")
    public AlertAnalyticsResponse getAlertAnalytics() {
        return dashboardService.getAlertAnalytics();
    }

    // 4. Log Analytics
    @GetMapping("/logs")
    public LogAnalyticsResponse getLogAnalytics() {
        return dashboardService.getLogAnalytics();
    }
}