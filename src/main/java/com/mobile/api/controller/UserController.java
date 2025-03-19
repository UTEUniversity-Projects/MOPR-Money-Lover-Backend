package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.user.UserDto;
import com.mobile.api.dto.user.UserAdminDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.user.CreateUserAdminForm;
import com.mobile.api.form.user.UpdatePasswordForm;
import com.mobile.api.form.user.UpdateUserAdminForm;
import com.mobile.api.form.user.UpdateUserForm;
import com.mobile.api.mapper.UserMapper;
import com.mobile.api.model.criteria.UserCriteria;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Group;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.repository.GroupRepository;
import com.mobile.api.repository.UserRepository;
import com.mobile.api.service.EmailService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_LIS')")
    public ApiMessageDto<PaginationDto<UserAdminDto>> getUserList(
            @Valid @ModelAttribute UserCriteria userCriteria,
            Pageable pageable
    ) {
        Specification<User> specification = userCriteria.getSpecification();
        Page<User> page = userRepository.findAll(specification, pageable);

        PaginationDto<UserAdminDto> responseDto = new PaginationDto<>(
                userMapper.fromEntitiesToUserAdminDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "Get user list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_GET')")
    public ApiMessageDto<UserAdminDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiMessageUtils.success(userMapper.fromEntityToUserAdminDto(user), "Get user successfully");
    }

    @GetMapping(value = "/client-get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<UserDto> getUserClient() {
        Long id = getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiMessageUtils.success(userMapper.fromEntityToUserDto(user), "Get user successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_CRE')")
    public ApiMessageDto<Void> createUser(
            @Valid @RequestBody CreateUserAdminForm createUserAdminForm,
            BindingResult bindingResult
    ) {
        // Create ACCOUNT
        Optional<Account> existingUser = accountRepository.findByUsernameOrEmail(
                createUserAdminForm.getUsername(), createUserAdminForm.getEmail()
        );
        if (existingUser.isPresent()) {
            if (existingUser.get().getUsername().equals(createUserAdminForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            if (existingUser.get().getEmail().equals(createUserAdminForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        Account account = new Account();
        account.setUsername(createUserAdminForm.getUsername());
        account.setPassword(passwordEncoder.encode(createUserAdminForm.getPassword()));
        account.setEmail(createUserAdminForm.getPhone());
        account.setAvatarPath(createUserAdminForm.getAvatarPath());

        Group group = groupRepository.findById(createUserAdminForm.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        if (Objects.equals(group.getKind(), BaseConstant.GROUP_KIND_SUPER_ADMIN)) {
            throw new BusinessException(ErrorCode.GROUP_NOT_ALLOWED);
        }
        account.setGroup(group);
        Account savedAccount = accountRepository.save(account);

        // Create USER
        User user = userMapper.fromCreateUserAdminForm(createUserAdminForm);
        user.setAccount(savedAccount);
        userRepository.save(user);

        return ApiMessageUtils.success(null, "Create user successfully");
    }
    
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_UDP')")
    public ApiMessageDto<Void> updateUser(
            @Valid @RequestBody UpdateUserAdminForm updateUserAdminForm,
            BindingResult bindingResult
    ) {
        User user = userRepository.findById(updateUserAdminForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Update ACCOUNT
        Account account = user.getAccount();
        if (!Objects.equals(account.getUsername(), updateUserAdminForm.getUsername())) {
            if (accountRepository.existsByUsername(updateUserAdminForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            account.setUsername(updateUserAdminForm.getUsername());
        }
        if (!Objects.equals(account.getEmail(), updateUserAdminForm.getEmail())) {
            if (accountRepository.existsByEmail(updateUserAdminForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
            account.setEmail(updateUserAdminForm.getEmail());
        }
        if (StringUtils.isNoneBlank(updateUserAdminForm.getPassword())) {
            if (!passwordEncoder.matches(updateUserAdminForm.getPassword(), account.getPassword())) {
                account.setPassword(passwordEncoder.encode(updateUserAdminForm.getPassword()));
            }
        }
        account.setPhone(updateUserAdminForm.getPhone());
        account.setAvatarPath(updateUserAdminForm.getAvatarPath());
        if (!Objects.equals(account.getGroup().getId(), updateUserAdminForm.getGroupId())) {
            Group group = groupRepository.findById(updateUserAdminForm.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
            if (Objects.equals(group.getKind(), BaseConstant.GROUP_KIND_SUPER_ADMIN)) {
                throw new BusinessException(ErrorCode.GROUP_NOT_ALLOWED);
            }
            account.setGroup(group);
        }
        accountRepository.save(account);

        // Update USER
        userMapper.updateFromUpdateUserAdminForm(user, updateUserAdminForm);
        userRepository.save(user);

        return ApiMessageUtils.success(null, "Update user successfully");
    }

    @PutMapping(value = "/client-update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateUserClient(
            @Valid @RequestBody UpdateUserForm updateUserForm,
            BindingResult bindingResult
    ) {
        Long id = getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Update ACCOUNT
        Account account = user.getAccount();
        if (!Objects.equals(account.getUsername(), updateUserForm.getUsername())) {
            if (accountRepository.existsByUsername(updateUserForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            account.setUsername(updateUserForm.getUsername());
        }
        if (!Objects.equals(account.getEmail(), updateUserForm.getEmail())) {
            if (accountRepository.existsByEmail(updateUserForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
            account.setEmail(updateUserForm.getEmail());
        }
        if (StringUtils.isNoneBlank(updateUserForm.getPassword())) {
            if (!passwordEncoder.matches(updateUserForm.getPassword(), account.getPassword())) {
                account.setPassword(passwordEncoder.encode(updateUserForm.getPassword()));
            }
        }
        account.setPhone(updateUserForm.getPhone());
        account.setAvatarPath(updateUserForm.getAvatarPath());
        accountRepository.save(account);

        // Update USER
        userMapper.updateFromUpdateUserForm(user, updateUserForm);
        userRepository.save(user);

        return ApiMessageUtils.success(null, "Update user successfully");
    }

    @PutMapping(value = "/update-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updatePassword(
            @Valid @RequestBody UpdatePasswordForm updatePasswordForm,
            BindingResult bindingResult
    ) {
        Long id = getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (StringUtils.isNoneBlank(updatePasswordForm.getPassword())
                || StringUtils.isNoneBlank(updatePasswordForm.getOldPassword())) {
            if (!StringUtils.isNoneBlank(updatePasswordForm.getOldPassword())) {
                throw new BusinessException(ErrorCode.ACCOUNT_INVALID_OLD_PASSWORD);
            } else {
                if (!StringUtils.isNoneBlank(updatePasswordForm.getPassword())) {
                    throw new BusinessException(ErrorCode.ACCOUNT_INVALID_PASSWORD);
                }
                if (!passwordEncoder.matches(updatePasswordForm.getOldPassword(), account.getPassword())) {
                    throw new BusinessException(ErrorCode.ACCOUNT_INVALID_OLD_PASSWORD);
                }
                if (!passwordEncoder.matches(updatePasswordForm.getPassword(), account.getPassword())) {
                    account.setPassword(passwordEncoder.encode(updatePasswordForm.getPassword()));
                }
            }
        }
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Update password successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_DEL')")
    public ApiMessageDto<Void> deleteUser(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Delete ACCOUNT
        accountRepository.deleteById(id);
        // Delete USER
        userRepository.deleteById(id);
        return ApiMessageUtils.success(null, "Delete user successfully");
    }
}
