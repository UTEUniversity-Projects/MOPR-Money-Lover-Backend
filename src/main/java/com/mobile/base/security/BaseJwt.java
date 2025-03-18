package com.mobile.base.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;

public class BaseJwt {

    private static Optional<Jwt> getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.ofNullable((Jwt) jwtAuth.getCredentials());
        }
        return Optional.empty();
    }

    public static Long getCurrentUserId() {
        return (Long) getJwt().map(jwt -> jwt.getClaim("user_id")).orElse(null);
    }

    public static String getCurrentUsername() {
        return getJwt().map(jwt -> jwt.getClaimAsString("username")).orElse("anonymous");
    }

    public static String getCurrentEmail() {
        return getJwt().map(jwt -> jwt.getClaimAsString("email")).orElse(null);
    }

    public static Boolean getIsSuperAdmin() {
        return getJwt().map(jwt -> jwt.getClaimAsBoolean("is_super_admin")).orElse(false);
    }

    public static Map<String, Object> getAllClaims() {
        return getJwt().map(Jwt::getClaims).orElse(Map.of());
    }
}
