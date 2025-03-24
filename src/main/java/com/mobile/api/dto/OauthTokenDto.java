package com.mobile.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthTokenDto {
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private long expiresIn;
    private List<String> scopes;
}
