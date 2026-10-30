
package com.faultlens.backend.service;

import com.faultlens.backend.dto.FaultEventResponse;
import com.faultlens.backend.entity.FaultEvent;
import com.faultlens.backend.entity.MonitoredService;
import com.faultlens.backend.repository.FaultEventRepository;
import com.faultlens.backend.repository.MonitoredServiceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FaultEventService {

    private final FaultEventRepository faultEventRepository;
    private final MonitoredServiceRepository monitoredServiceRepository;

    public FaultEventService(
            FaultEventRepository faultEventRepository,
            MonitoredServiceRepository monitoredServiceRepository
    ) {
        this.faultEventRepository = faultEventRepository;
        this.monitoredServiceRepository = monitoredServiceRepository;
    }

    @Transactional
    public FaultEventResponse recordDetection(
            Long serviceId,
            long errorCount,
            int windowSeconds
    ) {
        MonitoredService service = monitoredServiceRepository
                .findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monitored service not found"
                ));

        LocalDateTime now = LocalDateTime.now();

        FaultEvent event = faultEventRepository
                .findFirstByService_IdAndStatusOrderByStartedAtDesc(
                        serviceId, "OPEN"
                )
                .orElse(null);

        if (event == null) {
            event = new FaultEvent();
            event.setService(service);
            event.setStartedAt(now);
            event.setStatus("OPEN");
        }

        event.setErrorCount(errorCount);
        event.setWindowSeconds(windowSeconds);
        event.setLastDetectedAt(now);

        return toResponse(faultEventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<FaultEventResponse> getAllEvents() {
        return faultEventRepository.findAllByOrderByStartedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FaultEventResponse getEventById(Long id) {
        FaultEvent event = faultEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fault event not found"
                ));

        return toResponse(event);
    }

    @Transactional(readOnly = true)
    public List<FaultEventResponse> getEventsByService(
            Long serviceId
    ) {
        if (!monitoredServiceRepository.existsById(serviceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Monitored service not found"
            );
        }

        return faultEventRepository
                .findByService_IdOrderByStartedAtDesc(serviceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FaultEventResponse resolveEvent(Long id) {
        FaultEvent event = faultEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fault event not found"
                ));

       if ("RESOLVED".equals(event.getStatus())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Fault event is already resolved"
                );
                }

        event.setStatus("RESOLVED");
        event.setResolvedAt(LocalDateTime.now());

        return toResponse(faultEventRepository.save(event));
    }
    
@Transactional(readOnly = true)
public List<FaultEventResponse> getEventsByStatus(String status) {

    if (!status.equalsIgnoreCase("OPEN")
            && !status.equalsIgnoreCase("RESOLVED")) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid status. Use OPEN or RESOLVED"
        );
    }

    return faultEventRepository
            .findByStatusOrderByStartedAtDesc(
                    status.toUpperCase()
            )
            .stream()
            .map(this::toResponse)
            .toList();
}


    private FaultEventResponse toResponse(FaultEvent event) {
        return new FaultEventResponse(
                event.getId(),
                event.getService().getId(),
                event.getService().getName(),
                event.getErrorCount(),
                event.getWindowSeconds(),
                event.getStatus(),
                event.getStartedAt(),
                event.getLastDetectedAt(),
                event.getResolvedAt()
        );
    }
}
