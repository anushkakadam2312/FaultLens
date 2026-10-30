
package com.faultlens.backend.repository;

import com.faultlens.backend.entity.FaultEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaultEventRepository
        extends JpaRepository<FaultEvent, Long> {

    List<FaultEvent> findAllByOrderByStartedAtDesc();

    List<FaultEvent> findByService_IdOrderByStartedAtDesc(
            Long serviceId
    );

    Optional<FaultEvent>
    findFirstByService_IdAndStatusOrderByStartedAtDesc(
            Long serviceId,
            String status
    );

    List<FaultEvent> findByStatusOrderByStartedAtDesc(
            String status
    );
}
