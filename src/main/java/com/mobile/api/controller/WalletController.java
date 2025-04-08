package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.wallet.WalletDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.wallet.CreateWalletForm;
import com.mobile.api.form.wallet.UpdateWalletForm;
import com.mobile.api.mapper.WalletMapper;
import com.mobile.api.model.criteria.WalletCriteria;
import com.mobile.api.model.entity.Currency;
import com.mobile.api.model.entity.File;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Wallet;
import com.mobile.api.repository.*;
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

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/wallet")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WalletController extends BaseController {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private WalletMapper walletMapper;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<WalletDto>> getWalletList(
            @Valid @ModelAttribute WalletCriteria walletCriteria,
            Pageable pageable
    ) {
        walletCriteria.setUserId(getCurrentUserId());
        Specification<Wallet> specification = walletCriteria.getSpecification();
        Page<Wallet> page = walletRepository.findAll(specification, pageable);

        PaginationDto<WalletDto> responseDto = new PaginationDto<>(
                walletMapper.fromEntitiesToWalletDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List categories successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<WalletDto> getWallet(@PathVariable Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));

        return ApiMessageUtils.success(walletMapper.fromEntityToWalletDto(wallet), "Get wallet successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> createWallet(@Valid @RequestBody CreateWalletForm createWalletForm) {
        Wallet wallet = walletMapper.fromCreateWalletFormToEntity(createWalletForm);

        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        wallet.setUser(user);

        Currency currency = currencyRepository.findById(createWalletForm.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));
        wallet.setCurrency(currency);

        File icon = fileRepository.findById(createWalletForm.getIconId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        wallet.setIcon(icon);

        if (createWalletForm.getIsPrimary()) {
            walletRepository.resetPrimaryWalletByUserId(userId);
        }

        walletRepository.save(wallet);
        return ApiMessageUtils.success(null, "Create wallet successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> updateWallet(@Valid @RequestBody UpdateWalletForm updateWalletForm) {
        Wallet wallet = walletRepository.findById(updateWalletForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));

        if (!Objects.equals(wallet.getCurrency().getId(), updateWalletForm.getCurrencyId())) {
            Currency currency = currencyRepository.findById(updateWalletForm.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));
            wallet.setCurrency(currency);
        }
        if (!Objects.equals(wallet.getIcon().getId(), updateWalletForm.getIconId())) {
            File icon = fileRepository.findById(updateWalletForm.getIconId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
            wallet.setIcon(icon);
        }
        walletMapper.updateFromUpdateWalletForm(wallet, updateWalletForm);

        if (updateWalletForm.getIsPrimary()) {
            walletRepository.resetPrimaryWalletByUserId(getCurrentUserId());
        }

        walletRepository.save(wallet);
        return ApiMessageUtils.success(null, "Update wallet successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Operation(summary = "Attention", description = "Delete wallet and all bills, events, periods, budgets associated with the wallet")
    public ApiMessageDto<Void> deleteWallet(@PathVariable Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));

        // Check if the wallet is primary
        if (wallet.getIsPrimary()) {
            throw new BusinessException(ErrorCode.WALLET_CANT_DELETE);
        }
        // Delete all bills and events associated with the wallet
        billRepository.deleteAllByWalletId(wallet.getId());
        eventRepository.deleteAllByWalletId(wallet.getId());
        // Delete all periods and budgets associated with the wallet
        List<Long> periodIds = periodRepository.getIdsByWalletId(wallet.getId());
        if (!periodIds.isEmpty()) {
            budgetRepository.deleteAllFollowPeriodIds(periodIds);
            periodRepository.deleteAllByWalletId(wallet.getId());
        }
        // Delete the wallet
        walletRepository.delete(wallet);
        return ApiMessageUtils.success(null, "Delete wallet successfully");
    }
}
