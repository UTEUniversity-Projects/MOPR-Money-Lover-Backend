package com.mobile.base.exception;

import com.mobile.base.enumeration.ErrorCode;
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
