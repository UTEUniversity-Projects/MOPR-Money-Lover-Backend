package com.mobile.api.scheduler;

import com.mobile.api.repository.OtpRepository;
import com.mobile.api.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class OtpScheduler {
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredOTPs() {
        otpRepository.deleteExpiredOTPs(Instant.now());
        tokenRepository.deleteExpiredTokens(Instant.now());
    }
}
