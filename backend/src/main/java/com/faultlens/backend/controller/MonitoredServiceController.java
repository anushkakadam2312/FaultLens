package com.faultlens.backend.controller;

import com.faultlens.backend.dto.CreateServiceRequest;
import com.faultlens.backend.dto.ServiceResponse;
import com.faultlens.backend.service.MonitoredServiceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class MonitoredServiceController {

    private final MonitoredServiceService monitoredServiceService;

    public MonitoredServiceController(
            MonitoredServiceService monitoredServiceService
    ) {
        this.monitoredServiceService = monitoredServiceService;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody CreateServiceRequest request
    ) {

        ServiceResponse response =
                monitoredServiceService.createService(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {

        return ResponseEntity.ok(
                monitoredServiceService.getAllServices()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                monitoredServiceService.getServiceById(id)
        );
    }
}