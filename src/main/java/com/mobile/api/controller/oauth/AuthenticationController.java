package com.mobile.api.controller.oauth;

import com.mobile.api.dto.TokenDto;
import com.mobile.api.form.LoginForm;
import com.mobile.api.security.jwt.JwtProperties;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

/**
 * Controller for handling authentication & OAuth2 token exchange
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final RestTemplate restTemplate;
    private final JwtProperties jwtProperties;

    public AuthenticationController(RestTemplate restTemplate, JwtProperties jwtProperties) {
        this.restTemplate = restTemplate;
        this.jwtProperties = jwtProperties;
    }

    /**
     * API endpoint for user login
     * This method sends a request to OAuth2's default /login endpoint and returns the access token.
     *
     * @return Access token response or an error message if authentication fails.
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult) {
        try {
            // Authenticate using OAuth2's /login endpoint
            String sessionId = authenticateViaOAuth2(loginForm.getUsername(), loginForm.getPassword());

            // Request Authorization Code from OAuth2 Server using session
            String authorizationCode = requestAuthorizationCode(loginForm.getUsername(), sessionId);

            // Exchange Authorization Code for an Access Token
            TokenDto tokenResponse = exchangeCodeForToken(authorizationCode, loginForm.getUsername(), loginForm.getPassword());
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    /**
     * Authenticates user by sending credentials to OAuth2 /login endpoint
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return The session ID from the authentication response
     */
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

        throw new RuntimeException("OAuth2 Login failed, unable to retrieve session.");
    }

    /**
     * Requests an Authorization Code from the OAuth2 authorization endpoint.
     *
     * @param clientId  The client ID (username in this case)
     * @param sessionId The session ID from OAuth2 login
     * @return The authorization code retrieved from the redirect URL.
     */
    private String requestAuthorizationCode(String clientId, String sessionId) {
        String authorizeUrl = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=openid profile email&state=random_string",
                jwtProperties.getBaseUrl() + jwtProperties.getAuthorizationUri(),
                clientId,
                jwtProperties.getBaseUrl() + jwtProperties.getRedirectUri()
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

        throw new RuntimeException("Unable to retrieve Authorization Code");
    }

    /**
     * Exchanges the Authorization Code for an Access Token.
     *
     * @param code         The authorization code obtained from the authorization endpoint.
     * @param clientId     The client ID.
     * @param clientSecret The client secret (can be encrypted or stored securely).
     * @return Access token response.
     */
    private TokenDto exchangeCodeForToken(String code, String clientId, String clientSecret) {
        String tokenUrl = jwtProperties.getBaseUrl() + jwtProperties.getTokenUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", jwtProperties.getBaseUrl() + jwtProperties.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to retrieve Access Token");
        }

        Map<String, Object> body = response.getBody();
        return new TokenDto(
                (String) body.get("access_token"),
                (String) body.get("refresh_token"),
                (String) body.get("id_token"),
                (String) body.get("token_type"),
                ((Number) body.get("expires_in")).longValue(),
                Arrays.asList(((String) body.get("scope")).split(" "))
        );
    }
}
