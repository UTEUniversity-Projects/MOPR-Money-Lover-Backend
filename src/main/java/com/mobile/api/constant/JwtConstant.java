package com.mobile.api.constant;

public class JwtConstant {
    /**
     * ISSUER constants
     */
    public static final String ISSUER_OTP = "otp-service";
    public static final String ISSUER_REGISTER_SERVICE = "register-service";
    public static final String ISSUER_PASSWORD_SERVICE = "password-service";
    public static final String ISSUER_EMAIL_SERVICE = "email-service";

    /**
     * OAUTH2_URI constants
     */
    public static final String OAUTH2_URI_TOKEN = "/api/oauth2/token";
    public static final String OAUTH2_URI_AUTHORIZATION = "/api/oauth2/authorize";
    public static final String OAUTH2_URI_TOKEN_INTROSPECTION = "/api/oauth2/introspect";
    public static final String OAUTH2_URI_TOKEN_REVOCATION = "/api/oauth2/revoke";
    public static final String OAUTH2_URI_REDIRECT = "/api/oauth2/callback";
    public static final String OAUTH2_URI_CONSENT_PAGE = "/api/oauth2/consent";
    public static final String OAUTH2_URI_LOGIN = "/login";
    public static final String OAUTH2_URI_LOGOUT = "/logout";
    public static final String OAUTH2_URI_ERROR = "/error";
    public static final String OAUTH2_URI_CALLBACK = "/home";
}
