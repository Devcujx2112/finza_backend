package com.finza.backend.repository;

import com.finza.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface Account_repository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    Optional<Account> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String email);

}
