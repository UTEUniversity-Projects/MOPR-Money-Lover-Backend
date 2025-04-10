package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    boolean existsByCurrencyId(Long id);

    @Modifying
    @Query("UPDATE Wallet w SET w.isPrimary = false WHERE w.user.id = :userId")
    void resetPrimaryWalletByUserId(@Param("userId") Long userId);
}
