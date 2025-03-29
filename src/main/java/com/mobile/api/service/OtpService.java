package com.mobile.api.service;

import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.OtpCode;
import com.mobile.api.repository.OtpRepository;
import com.mobile.api.utils.OtpUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Getter
    @Value("${otp.expiry.minutes}")
    private int otpExpiryMinutes;
    @Value("${spring.data.redis.otp-timeout}")
    private int otpTimeout;

    public OtpCode createOtp(String email, Integer kind) {
        OtpCode otpCode = new OtpCode();
        otpCode.setEmail(email);
        otpCode.setCode(OtpUtils.generateOTP(6));
        otpCode.setKind(kind);
        otpCode.setExpiryTime(Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES));

        return otpRepository.save(otpCode);
    }

    public OtpCode refresh(String email, Integer kind) {
        OtpCode otpCode = otpRepository.findTopByEmailAndKindOrderByCreatedDateDesc(email, kind).orElse(null);

        if (otpCode != null && otpCode.getExpiryTime().isAfter(Instant.now())) {
            otpCode.setCode(OtpUtils.generateOTP(6));
            otpCode.setExpiryTime(Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES));
            otpRepository.save(otpCode);
            return otpCode;
        }
        return createOtp(email, kind);
    }

    public void verifyOtp(String email, String otp, Integer kind) {
        OtpCode otpCode = otpRepository.findTopByEmailAndKindOrderByCreatedDateDesc(email, kind)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.OTP_NOT_FOUND));

        if (Objects.equals(otpCode.getCode(), otp) && otpCode.getExpiryTime().isAfter(Instant.now())) {
            otpRepository.delete(otpCode);
        } else {
            throw new BusinessException(ErrorCode.BUSINESS_INVALID_OTP);
        }
    }

    public boolean canResendOtp(String token) {
        String redisKey = "otp:resend:" + token;
        String lastRequestTime = valueOperations.get(redisKey);

        if (lastRequestTime != null) {
            long lastTime = Long.parseLong(lastRequestTime);
            long currentTime = Instant.now().getEpochSecond();
            long diff = currentTime - lastTime;

            if (diff < otpTimeout) {
                return false;
            }
        }
        // Update the last request time in Redis
        valueOperations.set(
                redisKey,
                String.valueOf(Instant.now().getEpochSecond()),
                otpTimeout,
                TimeUnit.SECONDS);
        return true;
    }
}
