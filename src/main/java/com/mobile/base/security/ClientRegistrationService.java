package com.mobile.base.security;

import com.mobile.base.security.custom.CustomRegisteredClientRepository;
import com.mobile.base.security.jwt.JwtProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ClientRegistrationService {
    private final CustomRegisteredClientRepository registeredClientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    public ClientRegistrationService(CustomRegisteredClientRepository registeredClientRepository, JwtProperties jwtProperties) {
        this.registeredClientRepository = registeredClientRepository;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registerClientForUser(String username, String password) {
        List<String> scopes = List.of("openid", "profile", "email");

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(username)
                .clientSecret(passwordEncoder.encode(password))
                .clientIdIssuedAt(Instant.now())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(jwtProperties.getRedirectUri())
                .scopes(scope -> scope.addAll(scopes))
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(false)
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMillis(jwtProperties.getAccessTokenExpiration()))
                        .refreshTokenTimeToLive(Duration.ofMillis(jwtProperties.getRefreshTokenExpiration()))
                        .reuseRefreshTokens(true)
                        .build())
                .build();

        registeredClientRepository.save(registeredClient);
    }
}
