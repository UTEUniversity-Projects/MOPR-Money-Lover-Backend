package com.mobile.api.controller.oauth;

import com.mobile.api.constant.BaseConstant;
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
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for handling authentication & OAuth2 token exchange
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController extends BaseController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<OauthTokenDto> login(@Valid @RequestBody LoginForm loginForm) {
        try {
            Account account = accountRepository.findByEmail(loginForm.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

            String sessionId = authenticateViaOAuth2(account.getUsername(), loginForm.getPassword());

            String codeVerifier = CodeGeneratorUtils.generateCodeVerifier();
            String codeChallenge = CodeGeneratorUtils.generateCodeChallenge(codeVerifier);
            String state = UUID.randomUUID().toString();

            // Save verifier by state in DB/cache/session if needed
            String authorizationCode = requestAuthorizationCode(account.getUsername(), sessionId, codeChallenge, state);

            OauthTokenDto tokenResponse = exchangeCodeForToken(account.getUsername(), authorizationCode, codeVerifier);

            return ApiMessageUtils.success(tokenResponse, "Login successfully");
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_LOGIN_FAILED);
        }
    }

    @DeleteMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> logout() {
        tokenRepository.deleteAllByEmailAndKind(getCurrentEmail(), BaseConstant.TOKEN_KIND_REFRESH_TOKEN);
        return ApiMessageUtils.success(null, "Logout successfully");
    }

    private String authenticateViaOAuth2(String username, String password) {
        String loginUrl = jwtProperties.getBaseUrl() + "/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(loginUrl, request, Void.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getHeaders().containsKey(HttpHeaders.SET_COOKIE)) {
            String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            if (setCookieHeader != null) {
                for (String cookie : setCookieHeader.split(";")) {
                    if (cookie.startsWith("JSESSIONID=")) {
                        return cookie;
                    }
                }
            }
        }

        throw new AuthenticationException(ErrorCode.AUTHENTICATION_OAUTH2_LOGIN_FAILED);
    }

    private String requestAuthorizationCode(String clientId, String sessionId, String codeChallenge, String state) {
        String authorizeUrl = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&code_challenge=%s&code_challenge_method=S256",
                jwtProperties.getBaseUrl() + jwtProperties.getAuthorizationUri(),
                clientId,
                jwtProperties.getBaseUrl() + jwtProperties.getRedirectUri(),
                "openid profile email offline_access",
                state,
                codeChallenge
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                authorizeUrl, HttpMethod.GET, requestEntity, Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            if (Boolean.TRUE.equals(body.get("success"))) {
                return body.get("authorization_code").toString();
            }
        }

        throw new AuthenticationException(ErrorCode.AUTHENTICATION_OAUTH2_AUTHORIZATION_CODE);
    }

    private OauthTokenDto exchangeCodeForToken(String client_id, String code, String codeVerifier) {
        String tokenUrl = jwtProperties.getBaseUrl() + jwtProperties.getTokenUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("code", code);
        params.add("redirect_uri", jwtProperties.getBaseUrl() + jwtProperties.getRedirectUri());
        params.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_OAUTH2_ACCESS_TOKEN);
        }

        Map<String, Object> body = response.getBody();
        OauthTokenDto oauthTokenDto = new OauthTokenDto(
                (String) body.get("access_token"),
                (String) body.get("refresh_token"),
                (String) body.get("id_token"),
                (String) body.get("token_type"),
                ((Number) body.get("expires_in")).longValue(),
                Arrays.asList(((String) body.get("scope")).split(" "))
        );

        Account account = accountRepository.findByUsername("super_admin")
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        tokenRepository.deleteAllByEmailAndKind(account.getEmail(), BaseConstant.TOKEN_KIND_REFRESH_TOKEN);
        tokenService.createToken(
                account.getEmail(),
                oauthTokenDto.getRefreshToken(),
                BaseConstant.TOKEN_KIND_REFRESH_TOKEN,
                Instant.now().plus(oauthTokenDto.getExpiresIn(), ChronoUnit.MINUTES)
        );

        return oauthTokenDto;
    }
}
