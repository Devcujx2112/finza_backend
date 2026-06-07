package com.finza.backend.repository;

import com.finza.backend.entity.Account;
import com.finza.backend.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.AuthProvider;
import java.util.Optional;

@Repository
public interface Account_repository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Account> findByProviderAndProviderId(
            SocialType provider,
            String providerId
    );
}

