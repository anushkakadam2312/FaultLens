package com.faultlens.backend.service;

import com.faultlens.backend.dto.CreateServiceRequest;
import com.faultlens.backend.dto.ServiceResponse;
import com.faultlens.backend.entity.MonitoredService;
import com.faultlens.backend.repository.MonitoredServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
import java.util.stream.Collectors;

@Service
public class MonitoredServiceService {

    private final MonitoredServiceRepository monitoredServiceRepository;

    public MonitoredServiceService(
            MonitoredServiceRepository monitoredServiceRepository
    ) {
        this.monitoredServiceRepository = monitoredServiceRepository;
    }

    public ServiceResponse createService(CreateServiceRequest request) {

        if (monitoredServiceRepository.existsByName(request.getName())) {
            throw new RuntimeException("Service with this name already exists");
        }

        MonitoredService monitoredService = new MonitoredService();

        monitoredService.setName(request.getName());
        monitoredService.setDescription(request.getDescription());
        monitoredService.setEnvironment(request.getEnvironment());

        MonitoredService savedService =
                monitoredServiceRepository.save(monitoredService);

        return mapToResponse(savedService);
    }

    public List<ServiceResponse> getAllServices() {

        return monitoredServiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceResponse getServiceById(Long id) {

        MonitoredService monitoredService =
                monitoredServiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Service not found"));

        return mapToResponse(monitoredService);
    }

    private ServiceResponse mapToResponse(
            MonitoredService monitoredService
    ) {

        return new ServiceResponse(
                monitoredService.getId(),
                monitoredService.getName(),
                monitoredService.getDescription(),
                monitoredService.getEnvironment(),
                monitoredService.getCreatedAt()
        );
    }
}