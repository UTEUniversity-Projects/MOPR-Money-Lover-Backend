package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.category.CreateCategoryForm;
import com.mobile.api.form.category.UpdateCategoryForm;
import com.mobile.api.form.category.UpdateCategoryOrderingForm;
import com.mobile.api.form.category.UpdateCategoryOrderingItem;
import com.mobile.api.mapper.CategoryMapper;
import com.mobile.api.model.criteria.CategoryCriteria;
import com.mobile.api.model.entity.Category;
import com.mobile.api.model.entity.File;
import com.mobile.api.repository.BillRepository;
import com.mobile.api.repository.BudgetRepository;
import com.mobile.api.repository.CategoryRepository;
import com.mobile.api.repository.FileRepository;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<CategoryDto>> getCategoryList(
            @Valid @ModelAttribute CategoryCriteria categoryCriteria,
            Pageable pageable
    ) {
        categoryCriteria.setUserId(getCurrentUserId());
        Specification<Category> specification = categoryCriteria.getSpecification();
        Page<Category> page = categoryRepository.findAll(specification, pageable);

        PaginationDto<CategoryDto> responseDto = new PaginationDto<>(
                categoryMapper.fromEntitiesToCategoryDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List categories successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CategoryDto> getCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        return ApiMessageUtils.success(categoryMapper.fromEntityToCategoryDto(category), "Get category successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createCategory(@Valid @RequestBody CreateCategoryForm createCategoryForm) {
        Category category = categoryMapper.fromCreateCategoryFormToEntity(createCategoryForm);

        File icon = fileRepository.findById(createCategoryForm.getIconId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        category.setIcon(icon);
        categoryRepository.save(category);

        return ApiMessageUtils.success(null, "Create category successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateCategory(
            @Valid @RequestBody UpdateCategoryForm updateCategoryForm
    ) {
        Category category = categoryRepository.findById(updateCategoryForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        if (updateCategoryForm.getIconId() != null && !Objects.equals(updateCategoryForm.getIconId(), category.getIcon().getId())) {
            File icon = fileRepository.findById(updateCategoryForm.getIconId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
            // Use system file for icon
            category.setIcon(icon);
        }

        categoryMapper.updateFromUpdateCategoryForm(category, updateCategoryForm);
        categoryRepository.save(category);

        return ApiMessageUtils.success(null, "Update category successfully");
    }

    @PutMapping(value = "/client/update-ordering", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Operation(summary = "Attention", description = "Must be add all categories of user in the request")
    public ApiMessageDto<Void> updateCategoryOrdering(
            @Valid @RequestBody UpdateCategoryOrderingForm updateCategoryOrderingForm
    ) {
        Long userId = getCurrentUserId();
        List<Category> dbCategories = categoryRepository.findAllByUserId(userId);

        Map<Long, Integer> formMap = updateCategoryOrderingForm.getCategories()
                .stream()
                .collect(Collectors.toMap(UpdateCategoryOrderingItem::getCategoryId, UpdateCategoryOrderingItem::getOrdering));

        List<Category> categoriesToUpdate = new ArrayList<>();
        List<Category> categoriesToDelete = new ArrayList<>();

        for (Category category : dbCategories) {
            Long categoryId = category.getId();
            if (formMap.containsKey(categoryId)) {
                category.setOrdering(formMap.get(categoryId));
                categoriesToUpdate.add(category);
            } else {
                categoriesToDelete.add(category);
            }
        }

        if (!categoriesToUpdate.isEmpty()) {
            categoryRepository.saveAll(categoriesToUpdate);
        }
        if (!categoriesToDelete.isEmpty()) {
            categoryRepository.deleteAll(categoriesToDelete);
        }

        return ApiMessageUtils.success(null, "Update category ordering successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Operation(summary = "Attention", description = "Delete category and all bills, budgets associated with the category")
    public ApiMessageDto<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        // Delete all bills and budgets associated with the category
        billRepository.deleteAllByCategoryId(category.getId());
        budgetRepository.deleteAllByCategoryId(category.getId());
        // Delete category
        categoryRepository.delete(category);

        return ApiMessageUtils.success(null, "Delete category successfully");
    }
}
