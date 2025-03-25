package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.TokenDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.user.RegistrationForm;
import com.mobile.api.form.user.RequestRegisterForm;
import com.mobile.api.model.OtpCode;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.Group;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.repository.GroupRepository;
import com.mobile.api.repository.UserRepository;
import com.mobile.api.security.jwt.JwtUtils;
import com.mobile.api.service.*;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RecaptchaService recaptchaService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OauthService oauthService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/request-register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<TokenDto> requestRegister(
            @Valid @RequestBody RequestRegisterForm requestRegisterForm,
            BindingResult bindingResult
    ) {
        // Validate reCAPTCHA
        if (!recaptchaService.validateCaptcha(requestRegisterForm.getRecaptchaResponse())) {
            throw new BusinessException(ErrorCode.BUSINESS_INVALID_RECAPTCHA);
        }
        // Validate information
        if (accountRepository.existsByEmail(requestRegisterForm.getEmail())) {
            throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }
        if (accountRepository.existsByUsername(requestRegisterForm.getUsername())) {
            throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
        }

        // Create and Send OTP
        OtpCode otpCode = otpService.createOtp(requestRegisterForm.getEmail(), BaseConstant.OTP_CODE_KIND_REGISTER);
        emailService.sendOTPEmail("Your OTP Code for Register",
                otpCode.getEmail(),
                otpCode.getCode(),
                otpService.getOtpExpiryMinutes());

        // Create TOKEN-OTP
        String tokenValue = jwtUtils.generateRegisterToken(
                requestRegisterForm.getEmail(), requestRegisterForm.getUsername(),
                passwordEncoder.encode(requestRegisterForm.getPassword()));
        TokenDto tokenDto = tokenService.createToken(
                requestRegisterForm.getEmail(),
                tokenValue,
                BaseConstant.TOKEN_KIND_REGISTER,
                jwtDecoder.decode(tokenValue).getExpiresAt());

        return ApiMessageUtils.success(tokenDto, "OTP send to email successfully");
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> register(
            @Valid @RequestBody RegistrationForm registrationForm,
            BindingResult bindingResult
    ) {
        Jwt jwt = jwtDecoder.decode(registrationForm.getToken());
        String email = jwt.getSubject();
        String username = jwt.getClaimAsString("username");
        String password = jwt.getClaimAsString("password");

        // Verify TOKEN
        tokenService.verifyToken(email, registrationForm.getToken(), BaseConstant.TOKEN_KIND_REGISTER);
        otpService.verifyOtp(email, registrationForm.getOtp(), BaseConstant.OTP_CODE_KIND_REGISTER);

        // Create ACCOUNT
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        account.setVerified(true);
        Group group = groupRepository.findFirstByKind(BaseConstant.GROUP_KIND_USER)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        account.setGroup(group);
        Account savedAccount = accountRepository.save(account);

        // Create USER
        User user = new User();
        user.setAccount(savedAccount);
        userRepository.save(user);

        // Create CLIENT
        oauthService.registerClientForUser(username, password);

        return ApiMessageUtils.success(null, "Register successfully");
    }
}
