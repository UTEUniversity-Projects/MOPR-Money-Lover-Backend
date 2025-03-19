package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.account.AccountAdminDto;
import com.mobile.api.dto.account.AccountDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.account.CreateAccountAdminForm;
import com.mobile.api.form.account.CreateAccountForm;
import com.mobile.api.form.account.UpdateAccountAdminForm;
import com.mobile.api.form.account.UpdateAccountForm;
import com.mobile.api.mapper.AccountMapper;
import com.mobile.api.model.criteria.AccountCriteria;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.Group;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.repository.GroupRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController extends BaseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GroupRepository groupRepository;

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

    @GetMapping(value = "/client-get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountDto> getAccountClient() {
        Long id = getCurrentUserId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return ApiMessageUtils.success(accountMapper.fromEntityToAccountDto(account), "Get account successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ACC_CRE')")
    public ApiMessageDto<Void> createAccount(
            @Valid @RequestBody CreateAccountAdminForm createAccountAdminForm,
            BindingResult bindingResult
    ) {
        Optional<Account> existingAccount = accountRepository.findByUsernameOrEmail(
                createAccountAdminForm.getUsername(), createAccountAdminForm.getEmail()
        );
        if (existingAccount.isPresent()) {
            if (existingAccount.get().getUsername().equals(createAccountAdminForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            if (existingAccount.get().getEmail().equals(createAccountAdminForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        Account account = accountMapper.fromCreateAccountAdminFormToEntity(createAccountAdminForm);
        account.setPassword(passwordEncoder.encode(createAccountAdminForm.getPassword()));

        Group group = groupRepository.findById(createAccountAdminForm.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        account.setGroup(group);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Create account successfully");
    }

    @PostMapping(value = "/client-create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createAccountClient(
            @Valid @RequestBody CreateAccountForm createAccountForm,
            BindingResult bindingResult
    ) {
        Optional<Account> existingAccount = accountRepository.findByUsernameOrEmail(
                createAccountForm.getUsername(), createAccountForm.getEmail()
        );
        if (existingAccount.isPresent()) {
            if (existingAccount.get().getUsername().equals(createAccountForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            if (existingAccount.get().getEmail().equals(createAccountForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        Account account = accountMapper.fromCreateAccountFormToEntity(createAccountForm);
        account.setPassword(passwordEncoder.encode(createAccountForm.getPassword()));

        Group group = groupRepository.findFirstByKind(BaseConstant.GROUP_KIND_USER)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        account.setGroup(group);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Create account successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ACC_UPD')")
    public ApiMessageDto<Void> updateAccount(
            @Valid @RequestBody UpdateAccountAdminForm updateAccountAdminForm,
            BindingResult bindingResult
    ) {
        Account account = accountRepository.findById(updateAccountAdminForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!Objects.equals(account.getUsername(), updateAccountAdminForm.getUsername())) {
            if (accountRepository.existsByUsername(updateAccountAdminForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
        }
        if (!Objects.equals(account.getEmail(), updateAccountAdminForm.getEmail())) {
            if (accountRepository.existsByEmail(updateAccountAdminForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        accountMapper.updateFromUpdateAccountAdminForm(account, updateAccountAdminForm);
        Group group = groupRepository.findById(updateAccountAdminForm.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        account.setGroup(group);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Update account successfully");
    }

    @PutMapping(value = "/client-update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateAccountClient(
            @Valid @RequestBody UpdateAccountForm updateAccountForm,
            BindingResult bindingResult
    ) {
        Long id = getCurrentUserId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!Objects.equals(account.getUsername(), updateAccountForm.getUsername())) {
            if (accountRepository.existsByUsername(updateAccountForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
        }
        if (!Objects.equals(account.getEmail(), updateAccountForm.getEmail())) {
            if (accountRepository.existsByEmail(updateAccountForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        accountMapper.updateFromUpdateAccountForm(account, updateAccountForm);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Update account successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ACC_DEL')")
    public ApiMessageDto<Void> deleteAccount(@PathVariable Long id) {
        return ApiMessageUtils.success(null, "Not allow delete account");
    }
}
