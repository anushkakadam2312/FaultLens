
package com.faultlens.backend.controller;

import com.faultlens.backend.dto.FaultResponse;
import com.faultlens.backend.service.FaultDetectionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fault-detection")
public class FaultDetectionController {

    private final FaultDetectionService faultDetectionService;

    public FaultDetectionController(
            FaultDetectionService faultDetectionService) {
        this.faultDetectionService = faultDetectionService;
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<FaultResponse> detectFault(
            @PathVariable Long serviceId) {
        return ResponseEntity.ok(
                faultDetectionService.detectFault(serviceId)
        );
    }
}

