package com.mobile.api.service;

import com.mobile.api.security.custom.CustomRegisteredClientRepository;
import com.mobile.api.security.jwt.JwtProperties;
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
public class OauthService {
    private final CustomRegisteredClientRepository registeredClientRepository;
    private final JwtProperties jwtProperties;

    public OauthService(CustomRegisteredClientRepository registeredClientRepository, JwtProperties jwtProperties) {
        this.registeredClientRepository = registeredClientRepository;
        this.jwtProperties = jwtProperties;
    }

    public void registerClientForUser(String username) {
        List<String> scopes = List.of("openid", "profile", "email", "offline_access");

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(username)
                .clientIdIssuedAt(Instant.now())
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(jwtProperties.getRedirectUri())
                .scopes(scope -> scope.addAll(scopes))
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
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
