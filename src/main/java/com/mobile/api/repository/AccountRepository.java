package com.mobile.api.repository;

import com.mobile.api.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByGroupId(Long id);

    Optional<Account> findByUsernameOrEmail(String username, String email);

    Optional<Account> findByEmail(String email);

    @Modifying
    @Query("UPDATE Account a SET a.resetPwdCode = NULL, a.resetPwdTime = NULL WHERE a.resetPwdTime < :now")
    void deleteExpiredOTPs(@Param("now") LocalDateTime now);
}
