package com.mobile.api.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.api.constant.BaseConstant;
import com.mobile.api.constant.JwtConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Instant;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtils {
    private final JwtEncoder jwtEncoder;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    @Value("${jwt.expiry.minutes}")
    private int jwtExpiryMinutes;

    public JwtUtils(JwtEncoder jwtEncoder, ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
    }

    /**
     * Create JWT for register account.
     */
    public String generateRegisterToken(String email, String username, String password) {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer(JwtConstant.ISSUER_REGISTER)
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plus(jwtExpiryMinutes, ChronoUnit.MINUTES))
                        .subject(email)
                        .claim("email", email)
                        .claim("username", username)
                        .claim("password", password)
                        .claim("otpKind", BaseConstant.OTP_CODE_KIND_REGISTER)
                        .claim("tokenKind", BaseConstant.TOKEN_KIND_REGISTER)
                        .build()
        )).getTokenValue();
    }

    /**
     * Create JWT for register account.
     */
    public String generateResetPasswordToken(String email) {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer(JwtConstant.ISSUER_REGISTER)
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plus(jwtExpiryMinutes, ChronoUnit.MINUTES))
                        .subject(email)
                        .claim("email", email)
                        .claim("otpKind", BaseConstant.OTP_CODE_KIND_RESET_PASSWORD)
                        .claim("tokenKind", BaseConstant.TOKEN_KIND_RESET_PASSWORD)
                        .build()
        )).getTokenValue();
    }

    /**
     * Create JWT for user update password.
     */
    public String generateUpdatePasswordToken(String email, String oldPassword, String newPassword) {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer(JwtConstant.ISSUER_UPDATE_PASSWORD)
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plus(jwtExpiryMinutes, ChronoUnit.MINUTES))
                        .subject(email)
                        .claim("oldPassword", oldPassword)
                        .claim("newPassword", newPassword)
                        .claim("otpKind", BaseConstant.OTP_CODE_KIND_UPDATE_PASSWORD)
                        .claim("tokenKind", BaseConstant.TOKEN_KIND_UPDATE_PASSWORD)
                        .build()
        )).getTokenValue();
    }

    /**
     * Create JWT for user update email.
     */
    public String generateUpdateEmailToken(String oldEmail, String newEmail) {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer(JwtConstant.ISSUER_UPDATE_EMAIL)
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plus(jwtExpiryMinutes, ChronoUnit.MINUTES))
                        .subject(oldEmail)
                        .claim("oldEmail", oldEmail)
                        .claim("newEmail", newEmail)
                        .claim("otpKind", BaseConstant.OTP_CODE_KIND_UPDATE_EMAIL)
                        .claim("tokenKind", BaseConstant.TOKEN_KIND_UPDATE_EMAIL)
                        .build()
        )).getTokenValue();
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
