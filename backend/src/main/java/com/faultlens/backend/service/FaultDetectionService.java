
package com.faultlens.backend.service;

import com.faultlens.backend.dto.FaultResponse;
import com.faultlens.backend.entity.MonitoredService;
import com.faultlens.backend.repository.LogRepository;
import com.faultlens.backend.repository.MonitoredServiceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FaultDetectionService {

    private static final int WINDOW_SECONDS = 60;
    private static final long ERROR_THRESHOLD = 5;

    private final LogRepository logRepository;
    private final MonitoredServiceRepository monitoredServiceRepository;
    private final FaultEventService faultEventService;

    public FaultDetectionService(
            LogRepository logRepository,
            MonitoredServiceRepository monitoredServiceRepository,
            FaultEventService faultEventService
    ) {
        this.logRepository = logRepository;
        this.monitoredServiceRepository = monitoredServiceRepository;
        this.faultEventService = faultEventService;
    }

    @Transactional
    public FaultResponse detectFault(Long serviceId) {

        MonitoredService service = monitoredServiceRepository
                .findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monitored service not found"
                ));

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime =
                endTime.minusSeconds(WINDOW_SECONDS);

        List<com.faultlens.backend.entity.Log> errorLogs =
                logRepository.findByService_IdAndLevelAndTimestampBetween(
                        serviceId,
                        "ERROR",
                        startTime,
                        endTime
                );

        long errorCount = errorLogs.size();

        boolean faultDetected = errorCount >= ERROR_THRESHOLD;

        String message = faultDetected
                ? "High error rate detected"
                : "No fault detected";

        if (faultDetected) {
            faultEventService.recordDetection(
                    serviceId,
                    errorCount,
                    WINDOW_SECONDS
            );
        }

        return new FaultResponse(
                service.getId(),
                service.getName(),
                faultDetected,
                errorCount,
                WINDOW_SECONDS,
                message
        );
    }
}
