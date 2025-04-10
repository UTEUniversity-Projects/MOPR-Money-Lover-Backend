package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.budget.BudgetDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.budget.CreateBudgetForm;
import com.mobile.api.form.budget.UpdateBudgetForm;
import com.mobile.api.mapper.BudgetMapper;
import com.mobile.api.model.criteria.BudgetCriteria;
import com.mobile.api.model.entity.Budget;
import com.mobile.api.model.entity.Category;
import com.mobile.api.model.entity.Period;
import com.mobile.api.repository.jpa.BudgetRepository;
import com.mobile.api.repository.jpa.CategoryRepository;
import com.mobile.api.repository.jpa.PeriodRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/budget")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BudgetController extends BaseController {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetMapper budgetMapper;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<BudgetDto>> getBudgetList(
            @Valid @ModelAttribute BudgetCriteria budgetCriteria,
            Pageable pageable
    ) {
        Specification<Budget> specification = budgetCriteria.getSpecification();
        Page<Budget> page = budgetRepository.findAll(specification, pageable);

        PaginationDto<BudgetDto> responseDto = new PaginationDto<>(
                budgetMapper.fromEntitiesToBudgetDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List budgets successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BudgetDto> getBudget(@PathVariable Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        return ApiMessageUtils.success(budgetMapper.fromEntityToBudgetDto(budget), "Get budget successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createBudget(@Valid @RequestBody CreateBudgetForm createBudgetForm) {
        Budget budget = budgetMapper.fromCreateBudgetFormToEntity(createBudgetForm);

        Period period = periodRepository.findById(createBudgetForm.getPeriodId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERIOD_NOT_FOUND));
        budget.setPeriod(period);

        Category category = categoryRepository.findById(createBudgetForm.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        budget.setCategory(category);

        budgetRepository.save(budget);
        return ApiMessageUtils.success(null, "Create budget successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateBudget(@Valid @RequestBody UpdateBudgetForm updateBudgetForm) {
        Budget budget = budgetRepository.findById(updateBudgetForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BUDGET_NOT_FOUND));

        if (!Objects.equals(budget.getPeriod().getId(), updateBudgetForm.getPeriodId())) {
            Period period = periodRepository.findById(updateBudgetForm.getPeriodId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERIOD_NOT_FOUND));
            budget.setPeriod(period);
        }

        if (!Objects.equals(budget.getCategory().getId(), updateBudgetForm.getCategoryId())) {
            Category category = categoryRepository.findById(updateBudgetForm.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
            budget.setCategory(category);
        }

        budgetRepository.save(budget);
        return ApiMessageUtils.success(null, "Create budget successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> deleteBudget(@PathVariable Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BUDGET_NOT_FOUND));

        budgetRepository.delete(budget);
        return ApiMessageUtils.success(null, "Delete budget successfully");
    }
}
