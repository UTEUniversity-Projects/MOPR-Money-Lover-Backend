package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByGroupId(Long id);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);
}
