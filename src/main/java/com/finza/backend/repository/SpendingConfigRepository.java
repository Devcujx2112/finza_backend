package com.finza.backend.repository;

import com.finza.backend.entity.SpendingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpendingConfigRepository extends JpaRepository<SpendingConfig, Long> {

    Optional<SpendingConfig> findByUserId(Long userId);
}
