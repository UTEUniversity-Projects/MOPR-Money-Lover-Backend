package com.mobile.api.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "oauth2-config")
public class JwtProperties {
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private String privateKey;
    private String publicKey;
    private String baseUrl;
    private String tokenUri;
    private String authorizationUri;
    private String tokenIntrospectionUri;
    private String tokenRevocationUri;
    private String redirectUri;
    private String consentPageUri;
    private String loginUri;
    private String logoutUri;
    private String errorUri;
    private String callbackUri;
}
