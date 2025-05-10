package com.mobile.api.controller.oauth;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.constant.JwtConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.OauthTokenDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.AuthenticationException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.LoginForm;
import com.mobile.api.model.entity.Account;
import com.mobile.api.repository.jpa.AccountRepository;
import com.mobile.api.repository.jpa.TokenRepository;
import com.mobile.api.security.jwt.JwtProperties;
import com.mobile.api.service.TokenService;
import com.mobile.api.service.WebClientService;
import com.mobile.api.utils.ApiMessageUtils;
import com.mobile.api.utils.CodeGeneratorUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController extends BaseController {
    @Autowired
    private WebClientService webClientService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private WebClient baseClient;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<OauthTokenDto> login(@Valid @RequestBody LoginForm loginForm) {
        Account account = accountRepository.findByEmail(loginForm.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        authenticateViaOAuth2(account.getUsername(), loginForm.getPassword());

        return ApiMessageUtils.success(runOauthWorkflow(account.getEmail(), account.getUsername()), "Login successfully");
    }

    @DeleteMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> logout() {
        tokenRepository.deleteAllByEmailAndKind(getCurrentEmail(), BaseConstant.TOKEN_KIND_ACCESS_TOKEN);
        return ApiMessageUtils.success(null, "Logout successfully");
    }

    @GetMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<OauthTokenDto> getAccessToken() {
        return ApiMessageUtils.success(runOauthWorkflow(getCurrentEmail(), getCurrentUsername()), "Get access token successfully");
    }

    private void authenticateViaOAuth2(String username, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);

        ClientResponse response = baseClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchangeToMono(Mono::just)
                .block();

        if (response == null || !response.statusCode().is2xxSuccessful()) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_LOGIN_FAILED);
        }
        webClientService.clearUserCookies(username);
        webClientService.storeUserCookies(username, response);
    }

    private OauthTokenDto runOauthWorkflow(String email, String clientId) {
        String codeVerifier = CodeGeneratorUtils.generateCodeVerifier();
        String codeChallenge = CodeGeneratorUtils.generateCodeChallenge(codeVerifier);
        String state = UUID.randomUUID().toString();

        String authorizationCode = requestAuthorizationCode(clientId, codeChallenge, state);

        return exchangeCodeForToken(email, clientId, authorizationCode, codeVerifier);
    }

    private String requestAuthorizationCode(String clientId, String codeChallenge, String state) {
        String authorizeUri = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&code_challenge=%s&code_challenge_method=S256",
                JwtConstant.OAUTH2_URI_AUTHORIZATION,
                clientId,
                jwtProperties.getBaseUrl() + JwtConstant.OAUTH2_URI_REDIRECT,
                "openid profile email offline_access",
                state,
                codeChallenge
        );

        Map<String, Object> body = webClientService.userGetRequest(baseClient, clientId, authorizeUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (body != null && Boolean.TRUE.equals(body.get("success"))) {
            return body.get("authorization_code").toString();
        }

        throw new AuthenticationException(ErrorCode.AUTHENTICATION_OAUTH2_AUTHORIZATION_CODE);
    }

    private OauthTokenDto exchangeCodeForToken(String email, String clientId, String authorizationCode, String codeVerifier) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("code", authorizationCode);
        formData.add("redirect_uri", jwtProperties.getBaseUrl() + JwtConstant.OAUTH2_URI_REDIRECT);
        formData.add("code_verifier", codeVerifier);

        Map<String, Object> body = webClientService.userPostRequest(baseClient, clientId, JwtConstant.OAUTH2_URI_TOKEN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (body == null) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_OAUTH2_ACCESS_TOKEN);
        }

        OauthTokenDto oauthTokenDto = mapToOauthTokenDto(body);

        tokenRepository.deleteAllByEmailAndKind(email, BaseConstant.TOKEN_KIND_ACCESS_TOKEN);
        tokenService.createToken(
                email,
                oauthTokenDto.getAccessToken(),
                BaseConstant.TOKEN_KIND_ACCESS_TOKEN,
                Instant.now().plus(oauthTokenDto.getExpiresIn(), ChronoUnit.MINUTES)
        );

        return oauthTokenDto;
    }

    private OauthTokenDto mapToOauthTokenDto(Map<String, Object> body) {
        return new OauthTokenDto(
                (String) body.get("access_token"),
                (String) body.get("refresh_token"),
                (String) body.get("id_token"),
                (String) body.get("token_type"),
                ((Number) body.get("expires_in")).longValue(),
                Arrays.asList(((String) body.get("scope")).split(" "))
        );
    }
}
