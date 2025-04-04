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
    BUSINESS_INVALID_TOKEN("ERROR-BUSINESS-0003", "Invalid Token"),
    BUSINESS_INVALID_RECAPTCHA("ERROR-BUSINESS-0004", "Invalid reCAPTCHA"),
    BUSINESS_OTP_RESEND_LIMIT("ERROR-BUSINESS-0005", "OTP resend limit"),

    /**
     * OTP error codes
     */
    OTP_NOT_FOUND("ERROR-OTP-0001", "OTP not found"),

    /**
     * TOKEN error codes
     */
    TOKEN_NOT_FOUND("ERROR-TOKEN-0001", "Token not found"),

    /**
     * FILE error codes
     */
    FILE_NOT_FOUND("ERROR-FILE-0001", "File not found"),
    FILE_SIZE_EXCEEDED("ERROR-FILE-0002", "File size exceeded"),
    FILE_TYPE_NOT_SUPPORTED("ERROR-FILE-0003", "File type not supported"),
    FILE_DELETE_ERROR_WITH_THIRD_PARTY("ERROR-FILE-0004", "File delete error with third-party"),
    FILE_DOWNLOAD_ERROR_WITH_THIRD_PARTY("ERROR-FILE-0004", "File download error with third-party"),

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
    ACCOUNT_INVALID_OLD_PASSWORD("ERROR-ACCOUNT-0004", "Old password invalid"),
    ACCOUNT_INVALID_NEW_PASSWORD("ERROR-ACCOUNT-0005", "New password must be different from old password"),
    ACCOUNT_INVALID_OLD_EMAIL("ERROR-ACCOUNT-0006", "Old email must be logged in"),
    ACCOUNT_INVALID_NEW_EMAIL("ERROR-ACCOUNT-0007", "New email must be different from old email"),

    /**
     * USER error codes
     */
    USER_NOT_FOUND("ERROR-USER-0001", "User not found"),
    USER_USERNAME_EXISTED("ERROR-USER-0002", "User existed by username"),
    USER_EMAIL_EXISTED("ERROR-USER-0003", "User existed by email"),
    USER_NOT_MATCH_OLD_PASSWORD("ERROR-USER-0004", "Old password does not match"),

    /**
     * CATEGORY error codes
     */
    CATEGORY_NOT_FOUND("ERROR-CATEGORY-0001", "Category not found"),
    CATEGORY_CANT_DELETE("ERROR-CATEGORY-0002", "Category can not delete"),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
