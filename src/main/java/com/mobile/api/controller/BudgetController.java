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
import com.mobile.api.model.criteria.CategoryCriteria;
import com.mobile.api.model.entity.Budget;
import com.mobile.api.model.entity.Category;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Wallet;
import com.mobile.api.repository.jpa.BudgetRepository;
import com.mobile.api.repository.jpa.CategoryRepository;
import com.mobile.api.repository.jpa.UserRepository;
import com.mobile.api.repository.jpa.WalletRepository;
import com.mobile.api.service.CategoryStatisticsService;
import com.mobile.api.utils.ApiMessageUtils;
import com.mobile.api.utils.PeriodUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/budget")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BudgetController extends BaseController {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetMapper budgetMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryStatisticsService categoryStatisticsService;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<BudgetDto>> getBudgetList(
            @Valid @ModelAttribute BudgetCriteria budgetCriteria,
            Pageable pageable
    ) {
        budgetCriteria.setUserId(getCurrentUserId());
        Specification<Budget> specification = budgetCriteria.getSpecification();
        Page<Budget> page = budgetRepository.findAll(specification, pageable);

        // Map Budget entities to BudgetDto and enrich them with category statistics
        CategoryCriteria categoryCriteria = new CategoryCriteria();
        categoryCriteria.setUserId(getCurrentUserId());
        List<BudgetDto> budgetDtoList = page.getContent().stream()
                .map(budget -> {
                    // Map the basic fields
                    BudgetDto budgetDto = budgetMapper.fromEntityToBudgetDto(budget);

                    // Enrich with category statistics
                    categoryCriteria.setId(budget.getCategory().getId());
                    budgetDto.setCategoryStatistics(
                            categoryStatisticsService.getStatistics(
                                    categoryCriteria,
                                    budget.getStartDate(),
                                    budget.getEndDate()
                            )
                    );

                    return budgetDto;
                })
                .collect(Collectors.toList());

        // Create pagination response
        PaginationDto<BudgetDto> responseDto = new PaginationDto<>(
                budgetDtoList,
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List budgets successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BudgetDto> getBudget(@PathVariable Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));
        BudgetDto budgetDto = budgetMapper.fromEntityToBudgetDto(budget);
        CategoryCriteria categoryCriteria = new CategoryCriteria();
        categoryCriteria.setId(budget.getCategory().getId());
        categoryCriteria.setUserId(getCurrentUserId());
        budgetDto.setCategoryStatistics(
                categoryStatisticsService.getStatistics(
                        categoryCriteria,
                        budget.getStartDate(),
                        budget.getEndDate()
                )
        );
        return ApiMessageUtils.success(budgetDto, "Get budget successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createBudget(@Valid @RequestBody CreateBudgetForm createBudgetForm) {
        Budget budget = budgetMapper.fromCreateBudgetFormToEntity(createBudgetForm);

        // Validate user
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        budget.setUser(user);
        // Validate category
        Category category = categoryRepository.findById(createBudgetForm.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        budget.setCategory(category);
        // Validate wallet
        Wallet wallet = walletRepository.findById(createBudgetForm.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));
        budget.setWallet(wallet);

        // Validate period type
        if (PeriodUtils.isValidPeriod(createBudgetForm.getPeriodType(), createBudgetForm.getStartDate(), createBudgetForm.getEndDate())) {
            throw new ResourceNotFoundException(ErrorCode.BUDGET_PERIOD_TYPE_INVALID);
        }
        budget.setPeriodType(createBudgetForm.getPeriodType());
        budget.setStartDate(createBudgetForm.getStartDate());
        budget.setEndDate(createBudgetForm.getEndDate());

        budget.setSpentAmount(BigDecimal.ZERO);
        budgetRepository.save(budget);
        return ApiMessageUtils.success(null, "Create budget successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateBudget(@Valid @RequestBody UpdateBudgetForm updateBudgetForm) {
        Budget budget = budgetRepository.findById(updateBudgetForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BUDGET_NOT_FOUND));

        // Validate category
        if (!Objects.equals(budget.getCategory().getId(), updateBudgetForm.getCategoryId())) {
            Category category = categoryRepository.findById(updateBudgetForm.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
            budget.setCategory(category);
        }
        // Validate wallet
        if (!Objects.equals(budget.getWallet().getId(), updateBudgetForm.getWalletId())) {
            Wallet wallet = walletRepository.findById(updateBudgetForm.getWalletId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));
            budget.setWallet(wallet);
        }

        // Validate period type
        if (!Objects.equals(budget.getPeriodType(), updateBudgetForm.getPeriodType())) {
            if (PeriodUtils.isValidPeriod(updateBudgetForm.getPeriodType(), updateBudgetForm.getStartDate(), updateBudgetForm.getEndDate())) {
                throw new ResourceNotFoundException(ErrorCode.BUDGET_PERIOD_TYPE_INVALID);
            }
            budget.setPeriodType(updateBudgetForm.getPeriodType());
            budget.setStartDate(updateBudgetForm.getStartDate());
            budget.setEndDate(updateBudgetForm.getEndDate());
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
