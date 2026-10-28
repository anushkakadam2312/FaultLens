
package com.faultlens.backend.controller;

import com.faultlens.backend.dto.FaultEventResponse;
import com.faultlens.backend.service.FaultEventService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fault-events")
public class FaultEventController {

    private final FaultEventService faultEventService;

    public FaultEventController(FaultEventService faultEventService) {
        this.faultEventService = faultEventService;
    }

    @GetMapping
    public ResponseEntity<List<FaultEventResponse>> getAllEvents() {
        return ResponseEntity.ok(
                faultEventService.getAllEvents()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaultEventResponse> getEventById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                faultEventService.getEventById(id)
        );
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<FaultEventResponse>> getEventsByService(
            @PathVariable Long serviceId
    ) {
        return ResponseEntity.ok(
                faultEventService.getEventsByService(serviceId)
        );
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<FaultEventResponse> resolveEvent(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                faultEventService.resolveEvent(id)
        );
    }
}
