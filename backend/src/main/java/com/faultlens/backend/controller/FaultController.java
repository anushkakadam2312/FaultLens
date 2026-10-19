
package com.faultlens.backend.controller;

import com.faultlens.backend.dto.FaultResponse;
import com.faultlens.backend.service.FaultDetectionService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faults")
public class FaultController {

    private final FaultDetectionService faultDetectionService;

    public FaultController(
            FaultDetectionService faultDetectionService
    ) {
        this.faultDetectionService = faultDetectionService;
    }

    @GetMapping("/service/{serviceId}")
    public FaultResponse detectFault(
            @PathVariable Long serviceId
    ) {
        return faultDetectionService.detectFault(serviceId);
    }
}

