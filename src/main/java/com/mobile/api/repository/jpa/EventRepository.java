package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Modifying
    @Query("UPDATE Event e SET e.isCompleted = :isCompleted WHERE e.id = :id")
    int updateIsCompletedById(@Param("id") Long id, @Param("isCompleted") Boolean isCompleted);

    void deleteAllByWalletId(Long walletId);
}
