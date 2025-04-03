package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.ResendOtpForm;
import com.mobile.api.model.OtpCode;
import com.mobile.api.repository.OtpRepository;
import com.mobile.api.repository.TokenRepository;
import com.mobile.api.service.EmailService;
import com.mobile.api.service.OtpService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OtpController extends BaseController {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping(value = "/resend-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> resendOtp(@Valid @RequestBody ResendOtpForm resendOtpForm) {
        if (!otpService.canResendOtp(resendOtpForm.getToken())) {
            throw new BusinessException(ErrorCode.BUSINESS_OTP_RESEND_LIMIT);
        }

        Jwt jwt = jwtDecoder.decode(resendOtpForm.getToken());
        String email = jwt.getSubject();
        Long otpKindLong = (Long) jwt.getClaims().get("otpKind");
        Long tokenKindLong = (Long) jwt.getClaims().get("tokenKind");

        if (otpKindLong == null || tokenKindLong == null) {
            throw new BusinessException(ErrorCode.BUSINESS_INVALID_TOKEN);
        }

        Integer otpKind = Math.toIntExact(otpKindLong);
        Integer tokenKind = Math.toIntExact(tokenKindLong);

        // Check existed TOKEN
        if (!tokenRepository.existsByTokenAndKind(resendOtpForm.getToken(), tokenKind)) {
            throw new ResourceNotFoundException(ErrorCode.TOKEN_NOT_FOUND);
        }
        // Create and Send OTP
        OtpCode otpCode = otpService.refresh(email, otpKind);
        emailService.sendOTPEmail("Resend your OTP Code",
                otpCode.getEmail(),
                otpCode.getCode(),
                otpService.getOtpExpiryMinutes());

        return ApiMessageUtils.success(null, "OTP resend to email successfully");
    }
}
