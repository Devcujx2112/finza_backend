package com.finza.backend.repository;

import com.finza.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Account_repository extends JpaRepository<Account, Long> {

}
