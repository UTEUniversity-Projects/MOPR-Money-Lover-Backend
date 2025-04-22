package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.account.AccountAdminDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.mapper.AccountMapper;
import com.mobile.api.model.criteria.AccountCriteria;
import com.mobile.api.model.entity.Account;
import com.mobile.api.repository.jpa.AccountRepository;
import com.mobile.api.repository.jpa.GroupRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController extends BaseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ACC_LIS')")
    public ApiMessageDto<PaginationDto<AccountAdminDto>> getAccountList(
            @Valid @ModelAttribute AccountCriteria accountCriteria,
            Pageable pageable
    ) {
        Specification<Account> specification = accountCriteria.getSpecification();
        Page<Account> page = accountRepository.findAll(specification, pageable);

        PaginationDto<AccountAdminDto> responseDto = new PaginationDto<>(
                accountMapper.fromEntitiesToAccountAdminDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "Get account list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ACC_GET')")
    public ApiMessageDto<AccountAdminDto> getAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        return ApiMessageUtils.success(accountMapper.fromEntityToAccountAdminDto(account), "Get account successfully");
    }
}
