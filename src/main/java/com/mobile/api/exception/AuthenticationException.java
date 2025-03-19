package com.mobile.api.exception;

import com.mobile.api.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationException(String message) {
        super(message);
        this.errorCode = ErrorCode.SYSTEM_AUTHENTICATION_ERROR;
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage(), null, false, false); // Disable Stack trace
        this.errorCode = errorCode;
    }
}
