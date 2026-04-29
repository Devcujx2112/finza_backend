package com.finza.backend.repository;

import com.finza.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Account_repository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
