
package com.faultlens.backend.repository;

import com.faultlens.backend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByService_Id(Long serviceId);
}
