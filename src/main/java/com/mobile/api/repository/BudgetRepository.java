package com.mobile.api.repository;

import com.mobile.api.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>, JpaSpecificationExecutor<Budget> {
    void deleteAllByCategoryId(Long id);

    @Modifying
    @Query("DELETE FROM Budget b WHERE b.period.id IN :periodIds")
    void deleteAllFollowPeriodIds(List<Long> periodIds);
}
