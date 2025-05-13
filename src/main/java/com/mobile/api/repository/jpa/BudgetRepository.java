package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>, JpaSpecificationExecutor<Budget> {
    void deleteAllByCategoryId(Long id);

    @Modifying
    @Query("SELECT b FROM Budget b " +
            "WHERE b.user.id = :userId " +
            "   AND b.category.id = :categoryId " +
            "   AND b.startDate <= :date AND (b.endDate IS NULL OR b.endDate >= :date)")
    List<Budget> findAllBudgetByUserAndPeriod(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("date") Instant date);
}
