package com.mobile.api.repository;

import com.mobile.api.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByEmailAndKindOrderByCreatedDateDesc(String email, Integer kind);

    @Modifying
    @Query("DELETE OtpCode oc WHERE oc.expiryTime < :now")
    void deleteExpiredOTPs(@Param("now") Instant now);
}
