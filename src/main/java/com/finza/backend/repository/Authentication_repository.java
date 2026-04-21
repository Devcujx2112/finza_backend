package com.finza.backend.repository;

import com.finza.backend.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Authentication_repository extends JpaRepository<Authentication, Long> {

    // Tìm token theo refreshToken string
    Optional<Authentication> findByRefreshToken(String refreshToken);

    // Tìm token theo Account
    Optional<Authentication> findByAccount_UserId(Long userId);
}
