package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>, JpaSpecificationExecutor<Period> {
    @Query("SELECT p.id FROM Period p WHERE p.wallet.id = :walletId")
    List<Long> getIdsByWalletId(@Param("walletId") Long walletId);

    void deleteAllByWalletId(Long walletId);
}
