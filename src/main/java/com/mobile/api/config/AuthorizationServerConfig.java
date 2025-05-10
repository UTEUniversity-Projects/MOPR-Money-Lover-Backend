package com.mobile.api.config;

import com.mobile.api.constant.JwtConstant;
import com.mobile.api.security.custom.CustomUserDetails;
import com.mobile.api.security.jwt.JwtClaimsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthorizationServerConfig {

    public AuthorizationServerConfig() {
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(JwtConstant.OAUTH2_URI_AUTHORIZATION));

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .tokenEndpoint(JwtConstant.OAUTH2_URI_TOKEN)
                .authorizationEndpoint(JwtConstant.OAUTH2_URI_AUTHORIZATION)
                .tokenIntrospectionEndpoint(JwtConstant.OAUTH2_URI_TOKEN_INTROSPECTION)
                .tokenRevocationEndpoint(JwtConstant.OAUTH2_URI_TOKEN_REVOCATION)
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (context.getPrincipal().getPrincipal() instanceof CustomUserDetails user) {
                context.getClaims().claims(claims -> claims.putAll(JwtClaimsUtil.extractClaims(user)));
            }
        };
    }
}
