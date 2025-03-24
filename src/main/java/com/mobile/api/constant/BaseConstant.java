package com.mobile.api.constant;

public class BaseConstant {
    /**
     * STATUS constants
     */
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    /**
     * GROUP constants
     */
    public static final Integer GROUP_KIND_SUPER_ADMIN = 1;
    public static final Integer GROUP_KIND_ADMIN = 2;
    public static final Integer GROUP_KIND_MANAGER = 3;
    public static final Integer GROUP_KIND_INTERNAL = 4;
    public static final Integer GROUP_KIND_USER = 5;

    /**
     * USER_KIND constants
     */
    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_MANAGER = 2;
    public static final Integer USER_KIND_USER = 3;

    /**
     * USER_GENDER constants
     */
    public static final Integer USER_GENDER_MALE = 1;
    public static final Integer USER_GENDER_FEMALE = 2;
    public static final Integer USER_GENDER_UNKNOWN = 3;

    /**
     * OTP_CODE_KIND constants
     */
    public static final Integer OTP_CODE_KIND_REGISTER = 1;
    public static final Integer OTP_CODE_KIND_RESET_PASSWORD = 2;
    public static final Integer OTP_CODE_KIND_UPDATE_PASSWORD = 3;
    public static final Integer OTP_CODE_KIND_UPDATE_EMAIL = 4;

    /**
     * TOKEN_KIND constants
     */
    public static final Integer TOKEN_KIND_AUTHORIZATION = 1;
    public static final Integer TOKEN_KIND_REGISTER = 2;
    public static final Integer TOKEN_KIND_RESET_PASSWORD = 3;
    public static final Integer TOKEN_KIND_UPDATE_PASSWORD = 4;
    public static final Integer TOKEN_KIND_UPDATE_EMAIL = 5;
}
