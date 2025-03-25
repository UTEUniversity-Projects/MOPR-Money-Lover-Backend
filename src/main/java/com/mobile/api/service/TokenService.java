package com.mobile.api.service;

import com.mobile.api.dto.TokenDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.mapper.TokenMapper;
import com.mobile.api.model.Token;
import com.mobile.api.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private JwtDecoder jwtDecoder;

    public TokenDto createToken(String email, String tokenValue, Integer kind, Instant expiryTime) {
        Token token = new Token();
        token.setEmail(email);
        token.setToken(tokenValue);
        token.setKind(kind);
        token.setExpiryTime(expiryTime);
        Token savedToken = tokenRepository.save(token);

        return tokenMapper.fromEntityToTokenDto(savedToken);
    }

    public void verifyToken(String email, String tokenValue, Integer kind) {
        Jwt jwt = jwtDecoder.decode(tokenValue);

        // Valid TOKEN
        if (Objects.requireNonNull(jwt.getExpiresAt()).isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.BUSINESS_INVALID_TOKEN);
        }
        // Delete TOKEN
        Token token = tokenRepository.findTopByEmailAndTokenAndKindOrderByCreatedDateDesc(email, tokenValue, kind)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TOKEN_NOT_FOUND));
        tokenRepository.delete(token);
    }
}
