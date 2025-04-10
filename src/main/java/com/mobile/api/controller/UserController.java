package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.TokenDto;
import com.mobile.api.dto.user.UserDto;
import com.mobile.api.dto.user.UserAdminDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.user.*;
import com.mobile.api.mapper.UserMapper;
import com.mobile.api.model.OtpCode;
import com.mobile.api.model.criteria.UserCriteria;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.File;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Group;
import com.mobile.api.repository.jpa.AccountRepository;
import com.mobile.api.repository.jpa.FileRepository;
import com.mobile.api.repository.jpa.GroupRepository;
import com.mobile.api.repository.jpa.UserRepository;
import com.mobile.api.security.custom.CustomRegisteredClientRepository;
import com.mobile.api.security.jwt.JwtUtils;
import com.mobile.api.service.EmailService;
import com.mobile.api.service.FileService;
import com.mobile.api.service.OtpService;
import com.mobile.api.service.TokenService;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomRegisteredClientRepository customRegisteredClientRepository;
    @Autowired
    private FileRepository fileRepository;

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

        return ApiMessageUtils.success(responseDto, "List users successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_GET')")
    public ApiMessageDto<UserAdminDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiMessageUtils.success(userMapper.fromEntityToUserAdminDto(user), "Get user successfully");
    }

    @GetMapping(value = "/client/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<UserDto> getUserClient() {
        Long id = getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiMessageUtils.success(userMapper.fromEntityToUserDto(user), "Get user successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_UDP')")
    public ApiMessageDto<Void> updateUser(
            @Valid @RequestBody UpdateUserAdminForm updateUserAdminForm
    ) {
        User user = userRepository.findById(updateUserAdminForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Update ACCOUNT
        Account account = user.getAccount();
        RegisteredClient registeredClient = customRegisteredClientRepository.findByClientId(account.getUsername());
        assert registeredClient != null;
        if (!Objects.equals(account.getUsername(), updateUserAdminForm.getUsername())) {
            if (accountRepository.existsByUsername(updateUserAdminForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            account.setUsername(updateUserAdminForm.getUsername());
            customRegisteredClientRepository.updateClientId(registeredClient.getId(),
                    updateUserAdminForm.getUsername());
        }
        if (updateUserAdminForm.getAvatarId() != null && !updateUserAdminForm.getAvatarId().equals(account.getAvatar().getId())) {
            File avatar = fileRepository.findById(updateUserAdminForm.getAvatarId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));

            fileService.deleteFile(account.getAvatar().getId());
            account.setAvatar(avatar);
        }
        account.setPhone(updateUserAdminForm.getPhone());

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

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateUserClient(@Valid @RequestBody UpdateUserForm updateUserForm) {
        Long id = getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Update ACCOUNT
        Account account = user.getAccount();
        RegisteredClient registeredClient = customRegisteredClientRepository.findByClientId(account.getUsername());
        assert registeredClient != null;
        if (!Objects.equals(account.getUsername(), updateUserForm.getUsername())) {
            if (accountRepository.existsByUsername(updateUserForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            account.setUsername(updateUserForm.getUsername());
            customRegisteredClientRepository.updateClientId(registeredClient.getId(),
                    updateUserForm.getUsername());
        }
        if (updateUserForm.getAvatarId() != null && !updateUserForm.getAvatarId().equals(account.getAvatar().getId())) {
            File avatar = fileRepository.findById(updateUserForm.getAvatarId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));

            fileService.deleteFile(account.getAvatar().getId());
            account.setAvatar(avatar);
        }
        account.setPhone(updateUserForm.getPhone());
        accountRepository.save(account);

        // Update USER
        userMapper.updateFromUpdateUserForm(user, updateUserForm);
        userRepository.save(user);

        return ApiMessageUtils.success(null, "Update user successfully");
    }

    @PostMapping(value = "/client/request-update-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<TokenDto> requestUpdatePassword(
            @Valid @RequestBody RequestUpdatePasswordForm requestUpdatePasswordForm
    ) {
        if (Objects.equals(requestUpdatePasswordForm.getOldPassword(), requestUpdatePasswordForm.getNewPassword())) {
            throw new BusinessException(ErrorCode.ACCOUNT_INVALID_NEW_PASSWORD);
        }

        Account account = accountRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        if (!passwordEncoder.matches(requestUpdatePasswordForm.getOldPassword(), account.getPassword())) {
            throw new BusinessException(ErrorCode.ACCOUNT_INVALID_OLD_PASSWORD);
        }

        // Create and Send OTP
        OtpCode otpCode = otpService.createOtp(getCurrentEmail(), BaseConstant.OTP_CODE_KIND_UPDATE_PASSWORD);
        emailService.sendOTPEmail("Your OTP Code for Update Password",
                otpCode.getEmail(),
                otpCode.getCode(),
                otpService.getOtpExpiryMinutes());

        // Create TOKEN-UPDATE-PASSWORD
        String tokenValue = jwtUtils.generateUpdatePasswordToken(
                getCurrentEmail(),
                passwordEncoder.encode(requestUpdatePasswordForm.getOldPassword()),
                passwordEncoder.encode(requestUpdatePasswordForm.getNewPassword()));
        TokenDto tokenDto = tokenService.createToken(
                getCurrentEmail(),
                tokenValue,
                BaseConstant.TOKEN_KIND_UPDATE_PASSWORD,
                jwtDecoder.decode(tokenValue).getExpiresAt());

        return ApiMessageUtils.success(tokenDto, "OTP Code send to email successfully");
    }

    @PutMapping(value = "/client/update-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> updatePassword(@Valid @RequestBody UpdatePasswordForm updatePasswordForm) {
        Jwt jwt = jwtDecoder.decode(updatePasswordForm.getToken());
        String email = jwt.getSubject();
        String newPassword = jwt.getClaimAsString("newPassword");

        tokenService.verifyToken(email, updatePasswordForm.getToken(), BaseConstant.TOKEN_KIND_UPDATE_PASSWORD);
        otpService.verifyOtp(email, updatePasswordForm.getOtp(), BaseConstant.OTP_CODE_KIND_UPDATE_PASSWORD);

        // Update Password
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        RegisteredClient registeredClient = customRegisteredClientRepository.findByClientId(account.getUsername());
        assert registeredClient != null;
        account.setPassword(newPassword);
        customRegisteredClientRepository.updateClientSecret(registeredClient.getId(), newPassword);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Password updated successfully");
    }

    @PostMapping(value = "/client/request-update-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<TokenDto> requestUpdateEmail(
            @Valid @RequestBody RequestUpdateEmailForm requestUpdateEmailForm
    ) {
        if (!Objects.equals(requestUpdateEmailForm.getOldEmail(), getCurrentEmail())) {
            throw new BusinessException(ErrorCode.ACCOUNT_INVALID_OLD_EMAIL);
        }
        if (Objects.equals(requestUpdateEmailForm.getOldEmail(), requestUpdateEmailForm.getNewEmail())) {
            throw new BusinessException(ErrorCode.ACCOUNT_INVALID_NEW_EMAIL);
        }

        // Create and Send OTP
        OtpCode otpCode = otpService.createOtp(requestUpdateEmailForm.getOldEmail(), BaseConstant.OTP_CODE_KIND_UPDATE_EMAIL);
        emailService.sendOTPEmail("Your OTP Code for Update Email",
                otpCode.getEmail(),
                otpCode.getCode(),
                otpService.getOtpExpiryMinutes());

        // Create TOKEN-UPDATE-EMAIL
        String tokenValue = jwtUtils.generateUpdateEmailToken(requestUpdateEmailForm.getOldEmail(), requestUpdateEmailForm.getNewEmail());
        TokenDto tokenDto = tokenService.createToken(
                getCurrentEmail(),
                tokenValue,
                BaseConstant.TOKEN_KIND_UPDATE_EMAIL,
                jwtDecoder.decode(tokenValue).getExpiresAt());

        return ApiMessageUtils.success(tokenDto, "OTP Code send to old email successfully");
    }

    @PutMapping(value = "/client/update-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> updateEmail(@Valid @RequestBody UpdateEmailForm updateEmailForm) {
        Jwt jwt = jwtDecoder.decode(updateEmailForm.getToken());
        String oldEmail = jwt.getClaimAsString("oldEmail");
        String newEmail = jwt.getClaimAsString("newEmail");

        tokenService.verifyToken(oldEmail, updateEmailForm.getToken(), BaseConstant.TOKEN_KIND_UPDATE_EMAIL);
        otpService.verifyOtp(oldEmail, updateEmailForm.getOtp(), BaseConstant.OTP_CODE_KIND_UPDATE_EMAIL);

        // Update Email
        Account account = accountRepository.findByEmail(oldEmail)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.setEmail(newEmail);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Email updated successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USE_DEL')")
    @Hidden
    public ApiMessageDto<Void> deleteUser(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));

        // Delete all associated with USER
        accountRepository.deleteById(id);
        // Delete USER
        userRepository.deleteById(id);
        return ApiMessageUtils.success(null, "Delete user successfully");
    }
}
