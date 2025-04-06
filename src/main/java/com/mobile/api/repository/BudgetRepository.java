package com.mobile.api.repository;

import com.mobile.api.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>, JpaSpecificationExecutor<Budget> {
    List<Budget> findAllByCategoryId(Long categoryId);

    boolean existsByCategoryId(Long categoryId);
}
