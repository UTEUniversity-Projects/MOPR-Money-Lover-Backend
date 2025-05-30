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
    SYSTEM_INVALID_PARAMETER("ERROR-SYSTEM-0009", "Invalid parameter"),

    /**
     * AUTHENTICATION error codes
     */
    AUTHENTICATION_LOGIN_FAILED("ERROR-AUTHENTICATION-0001", "Login failed"),
    AUTHENTICATION_LOGOUT_FAILED("ERROR-AUTHENTICATION-0002", "Logout failed"),
    AUTHENTICATION_OAUTH2_LOGIN_FAILED("ERROR-AUTHENTICATION-0003", "OAuth2 Login failed"),
    AUTHENTICATION_OAUTH2_AUTHORIZATION_CODE("ERROR-AUTHENTICATION-0004", "Unable to retrieve Authorization Code"),
    AUTHENTICATION_OAUTH2_ACCESS_TOKEN("ERROR-AUTHENTICATION-0005", "Failed to retrieve Access Token"),

    /**
     * BUSINESS error codes
     */
    BUSINESS_PERMISSION_DENIED("ERROR-BUSINESS-0001", "No permission"),
    BUSINESS_INVALID_OTP("ERROR-BUSINESS-0002", "Invalid OTP"),
    BUSINESS_INVALID_TOKEN("ERROR-BUSINESS-0003", "Invalid Token"),
    BUSINESS_INVALID_RECAPTCHA("ERROR-BUSINESS-0004", "Invalid reCAPTCHA"),
    BUSINESS_OTP_RESEND_LIMIT("ERROR-BUSINESS-0005", "OTP resend limit"),
    BUSINESS_ELEMENTS_EXCEEDED_LIMIT("ERROR-BUSINESS-0006", "Elements exceeded limit"),

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
    FILE_DELETE_ERROR_WITH_THIRD_PARTY("ERROR-FILE-0004", "File delete error"),
    FILE_DOWNLOAD_ERROR_WITH_THIRD_PARTY("ERROR-FILE-0005", "File download error"),
    FILE_SCOPE_NOT_SUPPORTED("ERROR-FILE-0006", "File scope not supported"),

    /**
     * CATEGORY error codes
     */
    CATEGORY_NOT_FOUND("ERROR-CATEGORY-0001", "Category not found"),
    CATEGORY_CANT_DELETE("ERROR-CATEGORY-0002", "Category can not delete"),

    /**
     * CURRENCY error codes
     */
    CURRENCY_NOT_FOUND("ERROR-CURRENCY-0001", "Currency not found"),
    CURRENCY_NAME_EXISTED("ERROR-CURRENCY-0002", "Currency existed by name"),
    CURRENCY_CODE_EXISTED("ERROR-CURRENCY-0003", "Currency existed by code"),
    CURRENCY_CANT_DELETE("ERROR-CURRENCY-0004", "Currency can not delete"),

    /**
     * WALLET error codes
     */
    WALLET_NOT_FOUND("ERROR-WALLET-0001", "Wallet not found"),
    WALLET_CANT_DELETE("ERROR-WALLET-0002", "Wallet can not delete"),

    /**
     * TAG error codes
     */
    TAG_NOT_FOUND("ERROR-TAG-0001", "Tag not found"),

    /**
     * EVENT error codes
     */
    EVENT_NOT_FOUND("ERROR-EVENT-0001", "Event not found"),
    EVENT_INVALID_DATE_RANGE("ERROR-EVENT-0002", "Event invalid date range"),

    /**
     * REMINDER error codes
     */
    REMINDER_NOT_FOUND("ERROR-REMINDER-0001", "Reminder not found"),

    /**
     * PERIOD error codes
     */
    PERIOD_NOT_FOUND("ERROR-PERIOD-0001", "Period not found"),

    /**
     * BUDGET error codes
     */
    BUDGET_NOT_FOUND("ERROR-BUDGET-0001", "Period not found"),
    BUDGET_PERIOD_TYPE_INVALID("ERROR-BUDGET-0002", "Period type custom invalid"),

    /**
     * BILL error codes
     */
    BILL_NOT_FOUND("ERROR-BILL-0001", "Bill not found"),

    /**
     * NOTIFICATION error codes
     */
    NOTIFICATION_NOT_FOUND("ERROR-NOTIFICATION-0001", "Notification not found"),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
