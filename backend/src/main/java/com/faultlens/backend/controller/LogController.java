
package com.faultlens.backend.controller;

import com.faultlens.backend.dto.CreateLogRequest;
import com.faultlens.backend.dto.LogResponse;
import com.faultlens.backend.entity.Log;
import com.faultlens.backend.service.LogService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogResponse createLog(
            @Valid @RequestBody CreateLogRequest request) {
        Log log = logService.createLog(request);
        return LogResponse.fromEntity(log);
    }

    @GetMapping
    public List<LogResponse> getAllLogs() {
        return logService.getAllLogs()
                .stream()
                .map(LogResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public LogResponse getLogById(@PathVariable Long id) {
        return LogResponse.fromEntity(logService.getLogById(id));
    }

    @GetMapping("/service/{serviceId}")
    public List<LogResponse> getLogsByService(
            @PathVariable Long serviceId) {
        return logService.getLogsByService(serviceId)
                .stream()
                .map(LogResponse::fromEntity)
                .toList();
    }
}

