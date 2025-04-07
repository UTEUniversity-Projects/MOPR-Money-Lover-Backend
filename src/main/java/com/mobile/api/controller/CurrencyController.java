package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.currency.CurrencyAdminDto;
import com.mobile.api.dto.currency.CurrencyDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.form.currency.CreateCurrencyAdminForm;
import com.mobile.api.form.currency.UpdateCurrencyAdminForm;
import com.mobile.api.mapper.CurrencyMapper;
import com.mobile.api.model.criteria.CurrencyCriteria;
import com.mobile.api.model.entity.Currency;
import com.mobile.api.model.entity.File;
import com.mobile.api.repository.CurrencyRepository;
import com.mobile.api.repository.FileRepository;
import com.mobile.api.repository.WalletRepository;
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
@RequestMapping("/api/v1/currency")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CurrencyController extends BaseController {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CurrencyMapper currencyMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('CUR_LIS')")
    public ApiMessageDto<PaginationDto<CurrencyAdminDto>> getCurrencyListAdmin(
            @Valid @ModelAttribute CurrencyCriteria currencyCriteria,
            Pageable pageable
    ) {
        Specification<Currency> specification = currencyCriteria.getSpecification();
        Page<Currency> page = currencyRepository.findAll(specification, pageable);

        PaginationDto<CurrencyAdminDto> responseDto = new PaginationDto<>(
                currencyMapper.fromEntitiesToCurrencyAdminDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List currencies successfully");
    }

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<CurrencyDto>> getCurrencyList(
            @Valid @ModelAttribute CurrencyCriteria currencyCriteria,
            Pageable pageable
    ) {
        Specification<Currency> specification = currencyCriteria.getSpecification();
        Page<Currency> page = currencyRepository.findAll(specification, pageable);

        PaginationDto<CurrencyDto> responseDto = new PaginationDto<>(
                currencyMapper.fromEntitiesToCurrencyDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List currencies successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('CUR_CRE')")
    public ApiMessageDto<Void> createCurrency(@Valid @RequestBody CreateCurrencyAdminForm createCurrencyAdminForm) {
        Currency currency = new Currency();

        if (currencyRepository.existsByName(createCurrencyAdminForm.getName())) {
            throw new BusinessException(ErrorCode.CURRENCY_NAME_EXISTED);
        }
        if (currencyRepository.existsByCode(createCurrencyAdminForm.getCode())) {
            throw new BusinessException(ErrorCode.CURRENCY_CODE_EXISTED);
        }
        currency.setName(createCurrencyAdminForm.getName());
        currency.setCode(createCurrencyAdminForm.getCode());

        File icon = fileRepository.findById(createCurrencyAdminForm.getIconId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND));
        currency.setIcon(icon);

        currencyRepository.save(currency);
        return ApiMessageUtils.success(null, "Create currency successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('CUR_UPD')")
    public ApiMessageDto<Void> updateCurrency(@Valid @RequestBody UpdateCurrencyAdminForm updateCurrencyAdminForm) {
        Currency currency = currencyRepository.findById(updateCurrencyAdminForm.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CURRENCY_NOT_FOUND));

        if (!Objects.equals(currency.getName(), updateCurrencyAdminForm.getName())) {
            if (currencyRepository.existsByName(updateCurrencyAdminForm.getName())) {
                throw new BusinessException(ErrorCode.CURRENCY_NAME_EXISTED);
            }
            currency.setName(updateCurrencyAdminForm.getName());
        }
        if (!Objects.equals(currency.getCode(), updateCurrencyAdminForm.getCode())) {
            if (currencyRepository.existsByCode(updateCurrencyAdminForm.getCode())) {
                throw new BusinessException(ErrorCode.CURRENCY_CODE_EXISTED);
            }
            currency.setCode(updateCurrencyAdminForm.getCode());
        }
        if (!Objects.equals(currency.getIcon().getId(), updateCurrencyAdminForm.getIconId())) {
            File icon = fileRepository.findById(updateCurrencyAdminForm.getIconId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND));
            currency.setIcon(icon);
        }

        currencyRepository.save(currency);
        return ApiMessageUtils.success(null, "Update currency successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAuthority('CUR_DEL')")
    public ApiMessageDto<Void> deleteCurrency(@PathVariable Long id) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CURRENCY_NOT_FOUND));
        if (walletRepository.existsByCurrencyId(currency.getId())) {
            throw new BusinessException(ErrorCode.CURRENCY_CANT_DELETE);
        }

        currencyRepository.delete(currency);
        return ApiMessageUtils.success(null, "Delete currency successfully");
    }
}
