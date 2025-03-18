package com.mobile.base.exception;

import com.mobile.base.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCode.SYSTEM_RESOURCE_NOT_FOUND;
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), null, false, false); // Disable Stack trace
        this.errorCode = errorCode;
    }
}
