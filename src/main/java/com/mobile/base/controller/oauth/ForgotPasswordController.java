package com.mobile.base.controller.oauth;

import com.mobile.base.controller.base.BaseController;
import com.mobile.base.dto.ApiMessageDto;
import com.mobile.base.enumeration.ErrorCode;
import com.mobile.base.exception.BusinessException;
import com.mobile.base.exception.ResourceNotFoundException;
import com.mobile.base.form.ForgotPasswordForm;
import com.mobile.base.form.RequestForgotPasswordForm;
import com.mobile.base.model.entity.Account;
import com.mobile.base.repository.AccountRepository;
import com.mobile.base.service.EmailService;
import com.mobile.base.utils.ApiMessageUtils;
import com.mobile.base.utils.OtpUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ForgotPasswordController extends BaseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/request-forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> requestForgotPassword(
            @Valid @RequestBody RequestForgotPasswordForm requestForgotPasswordForm,
            BindingResult bindingResult
    ) {
        Account account = accountRepository.findByEmail(requestForgotPasswordForm.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        String otp = OtpUtils.generateOTP(6);
        account.setResetPwdCode(otp);
        account.setResetPwdTime(LocalDateTime.now().plusMinutes(10));
        accountRepository.save(account);

        emailService.sendOTP(requestForgotPasswordForm.getEmail(), otp, 10);

        return ApiMessageUtils.success(null, "OTP sent to email successfully.");
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordForm forgotPasswordForm,
            BindingResult bindingResult
    ) {
        Account account = accountRepository.findByEmail(forgotPasswordForm.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (account.getResetPwdCode() == null || !account.getResetPwdCode().equals(forgotPasswordForm.getOtp())) {
            throw new BusinessException(ErrorCode.BUSINESS_INVALID_OTP);
        }
        if (account.getResetPwdTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BUSINESS_OTP_EXPIRED);
        }

        account.setPassword(passwordEncoder.encode(forgotPasswordForm.getNewPassword()));
        account.setResetPwdCode(null);
        account.setResetPwdTime(null);
        accountRepository.save(account);

        return ApiMessageUtils.success(null, "Password reset successfully.");
    }
}
