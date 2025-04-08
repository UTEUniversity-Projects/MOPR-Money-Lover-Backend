package com.mobile.api.repository;

import com.mobile.api.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    @Modifying
    @Query("UPDATE Bill b SET b.event = NULL WHERE b.event.id = :eventId")
    void detachEventFromBills(@Param("eventId") Long eventId);

    void deleteAllByCategoryId(Long id);

    void deleteAllByWalletId(Long id);
}
