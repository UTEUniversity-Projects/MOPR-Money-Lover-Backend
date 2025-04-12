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
     * TOKEN_KIND constants
     */
    public static final Integer TOKEN_KIND_ACCESS_TOKEN = 1;
    public static final Integer TOKEN_KIND_REFRESH_TOKEN = 2;
    public static final Integer TOKEN_KIND_REGISTER = 3;
    public static final Integer TOKEN_KIND_RESET_PASSWORD = 4;
    public static final Integer TOKEN_KIND_UPDATE_PASSWORD = 5;
    public static final Integer TOKEN_KIND_UPDATE_EMAIL = 6;

    /**
     * CODE_LENGTH constants
     */
    public static final Integer CODE_LENGTH_OTP_CODE = 6;

    /**
     * OTP_CODE_KIND constants
     */
    public static final Integer OTP_CODE_KIND_REGISTER = 1;
    public static final Integer OTP_CODE_KIND_RESET_PASSWORD = 2;
    public static final Integer OTP_CODE_KIND_UPDATE_PASSWORD = 3;
    public static final Integer OTP_CODE_KIND_UPDATE_EMAIL = 4;

    /**
     * FILE_TYPE constants
     */
    public static final String FILE_TYPE_IMAGE = "IMAGE";
    public static final String FILE_TYPE_VIDEO = "VIDEO";
    public static final String FILE_TYPE_AUDIO = "AUDIO";
    public static final String FILE_TYPE_DOCUMENT = "DOCUMENT";
    public static final String FILE_TYPE_ARCHIVE = "ARCHIVE";

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
     * NOTIFICATION_TYPE constants
     */
    public static final Integer NOTIFICATION_TYPE_BUDGET = 1;
    public static final Integer NOTIFICATION_TYPE_REMINDER = 2;
    public static final Integer NOTIFICATION_TYPE_EVENT = 3;
    public static final Integer NOTIFICATION_TYPE_WARNING = 4;

    /**
     * NOTIFICATION_SCOPE constants
     */
    public static final Integer NOTIFICATION_SCOPE_INDIVIDUAL = 1;
    public static final Integer NOTIFICATION_SCOPE_GROUP = 2;
    public static final Integer NOTIFICATION_SCOPE_ALL = 3;
}
