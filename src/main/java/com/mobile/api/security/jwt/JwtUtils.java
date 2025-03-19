package com.mobile.api.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtils {

    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.objectMapper = new ObjectMapper();
        this.jwtProperties = jwtProperties;
    }

    /**
     * Convert grant types list String to List<AuthorizationGrantType>.
     */
    public List<AuthorizationGrantType> parseGrantTypes(String grantTypesStr) {
        return Optional.ofNullable(grantTypesStr)
                .map(types -> Arrays.stream(types.split(","))
                        .map(String::trim)
                        .map(this::mapToGrantType)
                        .distinct()
                        .toList())
                .orElse(List.of());
    }

    /**
     * Convert String to AuthorizationGrantType.
     */
    public AuthorizationGrantType mapToGrantType(String type) {
        return switch (type.trim().toLowerCase()) {
            case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
            default -> throw new IllegalArgumentException("Unsupported grant type: " + type);
        };
    }

    /**
     * Covert String to List<String>.
     */
    public List<String> parseList(String str) {
        return Optional.ofNullable(str)
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .distinct()
                        .toList())
                .orElse(List.of());
    }

    /**
     * Convert JSON to ClientSettings.
     */
    public ClientSettings parseJsonToClientSettings(String json) {
        if (json == null || json.isBlank()) {
            return getDefaultClientSettings();
        }
        try {
            Map<String, Object> settings = objectMapper.readValue(json, new TypeReference<>() {});
            return ClientSettings.builder()
                    .requireProofKey((Boolean) settings.getOrDefault("requireProofKey", false))
                    .requireAuthorizationConsent((Boolean) settings.getOrDefault("requireAuthorizationConsent", false))
                    .build();
        } catch (Exception e) {
            return getDefaultClientSettings();
        }
    }

    /**
     * Convert JSON to TokenSettings.
     */
    public TokenSettings parseJsonToTokenSettings(String json) {
        try {
            if (json == null || json.isBlank()) {
                return getDefaultTokenSettings();
            }
            Map<String, Object> settings = objectMapper.readValue(json, new TypeReference<>() {});

            return TokenSettings.builder()
                    .accessTokenTimeToLive(parseDuration(settings.get("accessTokenTimeToLive"), jwtProperties.getAccessTokenExpiration()))
                    .refreshTokenTimeToLive(parseDuration(settings.get("refreshTokenTimeToLive"), jwtProperties.getRefreshTokenExpiration()))
                    .reuseRefreshTokens(true)
                    .build();

        } catch (Exception e) {
            return getDefaultTokenSettings();
        }
    }

    /**
     * Return default ClientSettings.
     */
    public ClientSettings getDefaultClientSettings() {
        return ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(false)
                .build();
    }

    /**
     * Return default TokenSettings.
     */
    public TokenSettings getDefaultTokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMillis(jwtProperties.getAccessTokenExpiration()))
                .refreshTokenTimeToLive(Duration.ofMillis(jwtProperties.getRefreshTokenExpiration()))
                .reuseRefreshTokens(true)
                .build();
    }

    /**
     * Covert numeric value to Duration.
     */
    public Duration parseDuration(Object value, long defaultMillis) {
        if (value instanceof Number) {
            return Duration.ofSeconds(((Number) value).longValue());
        }
        return Duration.ofMillis(defaultMillis);
    }
}
