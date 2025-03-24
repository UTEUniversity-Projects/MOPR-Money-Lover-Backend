package com.mobile.api.repository;

import com.mobile.api.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTopByTokenAndKindOrderByCreatedDateDesc(String token, Integer kind);

    boolean existsByTokenAndKind(String token, Integer kind);

    @Modifying
    @Query("DELETE Token t WHERE t.expiryTime < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
}

