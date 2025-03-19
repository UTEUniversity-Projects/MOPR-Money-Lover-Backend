package com.mobile.api.security.jwt;

import com.mobile.api.security.custom.CustomUserDetails;

import java.util.HashMap;
import java.util.Map;

public class JwtClaimsUtil {

    public static Map<String, Object> extractClaims(CustomUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("is_super_admin", user.getIsSuperAdmin());
        claims.put("permission_codes", user.getPcodeList());
        return claims;
    }
}
