package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.TokenDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.user.ResetPasswordForm;
import com.mobile.api.form.user.RequestResetPasswordForm;
import com.mobile.api.model.OtpCode;
import com.mobile.api.model.entity.Account;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.security.custom.CustomRegisteredClientRepository;
import com.mobile.api.security.jwt.JwtUtils;
import com.mobile.api.service.EmailService;
import com.mobile.api.service.OtpService;
import com.mobile.api.service.TokenService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PasswordController extends BaseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomRegisteredClientRepository customRegisteredClientRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/request-reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<TokenDto> requestResetPassword(
            @Valid @RequestBody RequestResetPasswordForm requestResetPasswordForm
    ) {
        if (!accountRepository.existsByEmail(requestResetPasswordForm.getEmail())) {
            throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // Create and Send OTP
        OtpCode otpCode = otpService.createOtp(requestResetPasswordForm.getEmail(), BaseConstant.OTP_CODE_KIND_RESET_PASSWORD);
        emailService.sendOTPEmail("Your OTP Code for Reset Password",
                otpCode.getEmail(),
                otpCode.getCode(),
                otpService.getOtpExpiryMinutes());

        // Create TOKEN-RESET-PASSWORD
        String tokenValue = jwtUtils.generateResetPasswordToken(requestResetPasswordForm.getEmail());
        TokenDto tokenDto = tokenService.createToken(
                requestResetPasswordForm.getEmail(),
                tokenValue,
                BaseConstant.TOKEN_KIND_RESET_PASSWORD,
                jwtDecoder.decode(tokenValue).getExpiresAt());

        return ApiMessageUtils.success(tokenDto, "OTP sent to email successfully");
    }

    @PutMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> resetPassword(
            @Valid @RequestBody ResetPasswordForm resetPasswordForm
    ) {
        Jwt jwt = jwtDecoder.decode(resetPasswordForm.getToken());
        String email = jwt.getClaimAsString("email");

        tokenService.verifyToken(email, resetPasswordForm.getToken(), BaseConstant.TOKEN_KIND_RESET_PASSWORD);
        otpService.verifyOtp(email, resetPasswordForm.getOtp(), BaseConstant.OTP_CODE_KIND_RESET_PASSWORD);

        // Reset Password
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        RegisteredClient registeredClient = customRegisteredClientRepository.findByClientId(account.getUsername());
        assert registeredClient != null;
        if (!passwordEncoder.matches(resetPasswordForm.getNewPassword(), account.getPassword())) {
            account.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));
            customRegisteredClientRepository.updateClientSecret(
                    registeredClient.getId(), passwordEncoder.encode(resetPasswordForm.getNewPassword()));
        }
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Password reset successfully");
    }
}
