
package com.faultlens.backend.service;

import com.faultlens.backend.dto.CreateLogRequest;
import com.faultlens.backend.entity.Log;
import com.faultlens.backend.entity.MonitoredService;
import com.faultlens.backend.repository.LogRepository;
import com.faultlens.backend.repository.MonitoredServiceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class LogService {

    private static final Set<String> VALID_LEVELS =
            Set.of("INFO", "WARN", "ERROR", "DEBUG");

    private final LogRepository logRepository;
    private final MonitoredServiceRepository monitoredServiceRepository;

    public LogService(
            LogRepository logRepository,
            MonitoredServiceRepository monitoredServiceRepository) {
        this.logRepository = logRepository;
        this.monitoredServiceRepository = monitoredServiceRepository;
    }

    @Transactional
    public Log createLog(CreateLogRequest request) {

        if (request.getLevel() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Log level is required");
        }

        String level = request.getLevel()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (!VALID_LEVELS.contains(level)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid log level. Use INFO, WARN, ERROR, or DEBUG");
        }

        MonitoredService service = monitoredServiceRepository
                .findById(request.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monitored service not found"));

        Log log = new Log();
        log.setService(service);
        log.setLevel(level);
        log.setMessage(request.getMessage().trim());

        if (request.getTimestamp() != null) {
            log.setTimestamp(request.getTimestamp());
        } else {
            log.setTimestamp(LocalDateTime.now());
        }

        return logRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Log getLogById(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Log not found"));
    }

    @Transactional(readOnly = true)
    public List<Log> getLogsByService(Long serviceId) {

        if (!monitoredServiceRepository.existsById(serviceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Monitored service not found");
        }

        return logRepository.findByService_Id(serviceId);
    }
}
