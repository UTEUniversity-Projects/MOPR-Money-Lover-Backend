package com.mobile.api.exception;

import com.mobile.api.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = ErrorCode.SYSTEM_BUSINESS_ERROR;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage(), null, false, false); // Disable Stack trace
        this.errorCode = errorCode;
    }
}
