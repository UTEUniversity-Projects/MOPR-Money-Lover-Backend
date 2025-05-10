package com.mobile.api.controller;

import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.bill.BillDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.bill.CreateBillForm;
import com.mobile.api.form.bill.UpdateBillForm;
import com.mobile.api.mapper.BillMapper;
import com.mobile.api.model.criteria.BillCriteria;
import com.mobile.api.model.entity.*;
import com.mobile.api.repository.jpa.*;
import com.mobile.api.service.FileService;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/bill")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BillController {
    @Autowired
    private FileService fileService;
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

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<BillDto>> getBillList(
            @Valid @ModelAttribute BillCriteria billCriteria,
            Pageable pageable
    ) {
        Specification<Bill> specification = billCriteria.getSpecification();
        Page<Bill> page = billRepository.findAll(specification, pageable);

        PaginationDto<BillDto> responseDto = new PaginationDto<>(
                billMapper.fromEntitiesToBillDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List bills successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BillDto> getBill(@PathVariable Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        return ApiMessageUtils.success(billMapper.fromEntityToBillDto(bill), "Get bill successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createBill(@Valid @RequestBody CreateBillForm createBillForm) {
        Bill bill = billMapper.fromCreateBillFormToEntity(createBillForm);

        // Validate wallet and category
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
        // Update balance in wallet
        wallet.setBalance(wallet.getBalance() + (category.getIsExpense() ? 1.0 : -1.0) * bill.getAmount());
        walletRepository.save(wallet);

        return ApiMessageUtils.success(null, "Create bill successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateBill(@Valid @RequestBody UpdateBillForm updateBillForm) {
        Bill bill = billRepository.findById(updateBillForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BILL_NOT_FOUND));

        // Update category
        if (!Objects.equals(bill.getCategory().getId(), updateBillForm.getCategoryId())) {
            Category category = categoryRepository.findById(updateBillForm.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
            bill.setCategory(category);
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

        // Save the bill
        billMapper.updateFromUpdateBillForm(bill, updateBillForm);
        billRepository.save(bill);
        // Update balance in wallet
        Wallet wallet = bill.getWallet();
        wallet.setBalance(wallet.getBalance() + (bill.getCategory().getIsExpense() ? 1.0 : -1.0) * bill.getAmount());
        walletRepository.save(wallet);

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
