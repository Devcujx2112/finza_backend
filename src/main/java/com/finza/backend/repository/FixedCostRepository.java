package com.finza.backend.repository;

import com.finza.backend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedCostRepository
        extends JpaRepository<Budget, Long> {

}