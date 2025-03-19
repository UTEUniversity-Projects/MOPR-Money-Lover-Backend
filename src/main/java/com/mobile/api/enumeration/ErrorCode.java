package com.mobile.api.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * SYSTEM error codes
     */
    SYSTEM_NO_HANDLER_FOUND("ERROR-SYSTEM-0001", "No handler found"),
    SYSTEM_RESOURCE_NOT_FOUND("ERROR-SYSTEM-0002", "Resource not found"),
    SYSTEM_INVALID_FORM("ERROR-SYSTEM-0003", "Invalid form"),
    SYSTEM_BUSINESS_ERROR("ERROR-SYSTEM-0004", "Business error"),
    SYSTEM_UNAUTHENTICATED("ERROR-SYSTEM-0005", "Unauthenticated"),
    SYSTEM_AUTHENTICATION_ERROR("ERROR-SYSTEM-0006", "Authentication error"),
    SYSTEM_ACCESS_DENIED("ERROR-SYSTEM-0007", "Access denied"),
    SYSTEM_UNKNOWN_ERROR("ERROR-SYSTEM-0008", "Unknown error"),

    /**
     * BUSINESS error codes
     */
    BUSINESS_NO_PERMISSION("ERROR-BUSINESS-0001", "No permission"),
    BUSINESS_INVALID_OTP("ERROR-BUSINESS-0002", "Invalid OTP"),
    BUSINESS_OTP_EXPIRED("ERROR-BUSINESS-0003", "OTP Expired"),

    /**
     * GROUP error codes
     */
    GROUP_NOT_FOUND("ERROR-GROUP-0001", "Group not found"),
    GROUP_NAME_EXISTED("ERROR-GROUP-0002", "Group existed by name"),
    GROUP_NOT_ALLOWED("ERROR-GROUP-0003", "Group not allowed"),
    GROUP_CANT_DELETE("ERROR-GROUP-0004", "Group can not delete"),

    /**
     * PERMISSION error codes
     */
    PERMISSION_NOT_FOUND("ERROR-PERMISSION-0001", "Permission not found"),
    PERMISSION_ACTION_EXISTED("ERROR-PERMISSION-0002", "Permission existed by action"),
    PERMISSION_CODE_EXISTED("ERROR-PERMISSION-0003", "Permission existed by code"),

    /**
     * ACCOUNT error codes
     */
    ACCOUNT_NOT_FOUND("ERROR-ACCOUNT-0001", "Account not found"),
    ACCOUNT_USERNAME_EXISTED("ERROR-ACCOUNT-0002", "Account existed by username"),
    ACCOUNT_EMAIL_EXISTED("ERROR-ACCOUNT-0003", "Account existed by email"),
    ACCOUNT_INVALID_PASSWORD("ERROR-ACCOUNT-0004", "Old password invalid"),
    ACCOUNT_INVALID_OLD_PASSWORD("ERROR-ACCOUNT-0005", "Password invalid"),

    /**
     * ACCOUNT error codes
     */
    USER_NOT_FOUND("ERROR-USER-0001", "User not found"),
    USER_USERNAME_EXISTED("ERROR-USER-0002", "User existed by username"),
    USER_EMAIL_EXISTED("ERROR-USER-0003", "User existed by email"),
    USER_NOT_MATCH_OLD_PASSWORD("ERROR-USER-0004", "Old password does not match"),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
