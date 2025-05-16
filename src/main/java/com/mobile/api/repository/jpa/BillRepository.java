package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    void deleteAllByEventId(Long eventId);

    void deleteAllByCategoryId(Long categoryId);

    void deleteAllByWalletId(Long walletId);
}
