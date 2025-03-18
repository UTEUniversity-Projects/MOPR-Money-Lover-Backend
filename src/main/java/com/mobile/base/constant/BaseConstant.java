package com.mobile.base.constant;

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
}
