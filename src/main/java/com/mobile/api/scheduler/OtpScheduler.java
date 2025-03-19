package com.mobile.api.scheduler;

import com.mobile.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class OtpScheduler {
    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredOTPs() {
        accountRepository.deleteExpiredOTPs(LocalDateTime.now());
    }
}
