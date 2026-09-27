package com.faultlens.backend.repository;

import com.faultlens.backend.entity.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoredServiceRepository
        extends JpaRepository<MonitoredService, Long> {

    Optional<MonitoredService> findByName(String name);

    boolean existsByName(String name);
}