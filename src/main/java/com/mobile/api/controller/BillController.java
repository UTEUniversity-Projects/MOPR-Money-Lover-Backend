package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.bill.BillDto;
import com.mobile.api.dto.bill.BillStatisticsDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.bill.CreateBillForm;
import com.mobile.api.form.bill.UpdateBillForm;
import com.mobile.api.mapper.BillMapper;
import com.mobile.api.model.criteria.BillCriteria;
import com.mobile.api.model.entity.*;
import com.mobile.api.repository.jpa.*;
import com.mobile.api.service.BillStatisticsService;
import com.mobile.api.service.FileService;
import com.mobile.api.service.NotificationService;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/bill")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillController extends BaseController {
    @Autowired
    private FileService fileService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private BillStatisticsService billStatisticsService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<BillDto>> getBillList(
            @Valid @ModelAttribute BillCriteria billCriteria,
            Pageable pageable
    ) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "date")
            );
        }

        billCriteria.setUserId(getCurrentUserId());
        Specification<Bill> specification = billCriteria.getSpecification();
        Page<Bill> page = billRepository.findAll(specification, pageable);

        PaginationDto<BillDto> responseDto = new PaginationDto<>(
                billMapper.fromEntitiesToBillDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List bills successfully");
    }

    @GetMapping(value = "/client/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BillStatisticsDto> billStatisticsClient(
            @Valid @ModelAttribute BillCriteria billCriteria,
            Pageable pageable
    ) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "date")
            );
        }

        billCriteria.setUserId(getCurrentUserId());
        return ApiMessageUtils.success(
                billStatisticsService.getStatistics(billCriteria, pageable),
                "Get bill statistics successfully"
        );
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BillDto> getBill(@PathVariable Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        return ApiMessageUtils.success(billMapper.fromEntityToBillDto(bill), "Get bill successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> createBill(@Valid @RequestBody CreateBillForm createBillForm) {
        Bill bill = billMapper.fromCreateBillFormToEntity(createBillForm);

        // Validate user, wallet and category
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        bill.setUser(user);
        Wallet wallet = walletRepository.findById(createBillForm.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));
        bill.setWallet(wallet);
        Category category = categoryRepository.findById(createBillForm.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        bill.setCategory(category);

        // Validate tags
        if (createBillForm.getTagIds() != null && !createBillForm.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(createBillForm.getTagIds());
            bill.setTags(tags);
        }

        // Validate event, reminder and picture
        if (createBillForm.getEventId() != null) {
            Event event = eventRepository.findById(createBillForm.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));
            bill.setEvent(event);
        }
        if (createBillForm.getReminderId() != null) {
            Reminder reminder = reminderRepository.findById(createBillForm.getReminderId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));
            bill.setReminder(reminder);
        }
        if (createBillForm.getPictureId() != null) {
            File picture = fileRepository.findById(createBillForm.getPictureId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
            bill.setPicture(picture);
        }

        // Save the bill
        billRepository.save(bill);

        // Update wallet balance
        if (bill.getCategory().getIsExpense()) {
            wallet.setBalance(wallet.getBalance().subtract(bill.getAmount()));
        } else {
            wallet.setBalance(wallet.getBalance().add(bill.getAmount()));
        }

        // Update budget
        List<Budget> budgets = budgetRepository.findAllBudgetByUserAndPeriod(getCurrentUserId(), category.getId(), bill.getDate());
        if (budgets != null && !budgets.isEmpty()) {
            for (Budget budget : budgets) {
                budget.setSpentAmount(budget.getSpentAmount().add(bill.getAmount()));
                notificationService.scanToCreateNotification(user, budget);
            }
            budgetRepository.saveAll(budgets);
        }

        return ApiMessageUtils.success(null, "Create bill successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> updateBill(@Valid @RequestBody UpdateBillForm updateBillForm) {
        Bill bill = billRepository.findById(updateBillForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BILL_NOT_FOUND));

        // Remove old amount from wallet
        if (bill.getCategory().getIsExpense()) {
            bill.getWallet().setBalance(bill.getWallet().getBalance().add(bill.getAmount()));
        } else {
            bill.getWallet().setBalance(bill.getWallet().getBalance().subtract(bill.getAmount()));
        }

        // Remove old amount from budget
        List<Budget> oldBudgets = budgetRepository.findAllBudgetByUserAndPeriod(getCurrentUserId(), bill.getCategory().getId(), bill.getDate());
        if (oldBudgets != null && !oldBudgets.isEmpty()) {
            for (Budget budget : oldBudgets) {
                budget.setSpentAmount(budget.getSpentAmount().subtract(bill.getAmount()));
            }
            budgetRepository.saveAll(oldBudgets);
        }

        // Update category
        if (!Objects.equals(bill.getCategory().getId(), updateBillForm.getCategoryId())) {
            Category category = categoryRepository.findById(updateBillForm.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
            bill.setCategory(category);
        }
        // Update wallet
        if (!Objects.equals(bill.getWallet().getId(), updateBillForm.getWalletId())) {
            Wallet wallet = walletRepository.findById(updateBillForm.getWalletId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));
            bill.setWallet(wallet);
        }

        // Update tags
        if (updateBillForm.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(updateBillForm.getTagIds());
            bill.setTags(tags);
        }

        // Update event, reminder and picture
        if (updateBillForm.getEventId() != null
                && (bill.getEvent() == null || !Objects.equals(bill.getEvent().getId(), updateBillForm.getEventId()))) {
            Event event = eventRepository.findById(updateBillForm.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));
            bill.setEvent(event);
        }
        if (updateBillForm.getReminderId() != null
                && (bill.getReminder() == null || !Objects.equals(bill.getReminder().getId(), updateBillForm.getReminderId()))) {
            Reminder reminder = reminderRepository.findById(updateBillForm.getReminderId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));
            bill.setReminder(reminder);
        }
        if (updateBillForm.getPictureId() != null
                && (bill.getPicture() == null || !Objects.equals(bill.getPicture().getId(), updateBillForm.getPictureId()))) {
            File picture = fileRepository.findById(updateBillForm.getPictureId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
            bill.setPicture(picture);
        }

        // Update bill details
        billMapper.updateFromUpdateBillForm(bill, updateBillForm);
        // Update wallet balance
        if (bill.getCategory().getIsExpense()) {
            bill.getWallet().setBalance(bill.getWallet().getBalance().subtract(bill.getAmount()));
        } else {
            bill.getWallet().setBalance(bill.getWallet().getBalance().add(bill.getAmount()));
        }
        // Save the updated bill
        billRepository.save(bill);

        // Update budgets spent amount
        List<Budget> newBudgets = budgetRepository.findAllBudgetByUserAndPeriod(getCurrentUserId(), bill.getCategory().getId(), bill.getDate());
        if (newBudgets != null && !newBudgets.isEmpty()) {
            for (Budget budget : newBudgets) {
                budget.setSpentAmount(budget.getSpentAmount().add(bill.getAmount()));
                notificationService.scanToCreateNotification(bill.getUser(), budget);
            }
            budgetRepository.saveAll(newBudgets);
        }

        return ApiMessageUtils.success(null, "Update bill successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Attention", description = "This API will delete the bill and its associated reminder and picture")
    public ApiMessageDto<Void> deleteBill(@PathVariable Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BILL_NOT_FOUND));

        // Delete reminder and picture if they exist
        if (bill.getReminder() != null) {
            reminderRepository.delete(bill.getReminder());
        }
        if (bill.getPicture() != null) {
            fileService.deleteFile(bill.getPicture().getId());
        }
        // Delete the bill
        billRepository.delete(bill);
        return ApiMessageUtils.success(null, "Delete bill successfully");
    }
}
