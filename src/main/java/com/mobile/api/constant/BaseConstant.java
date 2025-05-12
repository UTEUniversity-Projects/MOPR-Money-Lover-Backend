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
     * COOKIES_REDIS constants
     */
    public static final String COOKIE_KEY_PREFIX = "cookie:";

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
     * FILE_SCOPE constants
     */
    public static final String FILE_SCOPE_CATEGORY_ICONS = "category_icons";
    public static final String FILE_SCOPE_INTERNAL_FLAGS = "international_flags";

    /**
     * FILE_ELEMENTS constants
     */
    public static final Integer FILE_ELEMENTS_MAXIMUM = 10;

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
     * PERIOD_TYPE constants
     */
    public static final Integer PERIOD_TYPE_WEEK = 1;
    public static final Integer PERIOD_TYPE_MONTH = 2;
    public static final Integer PERIOD_TYPE_QUARTER = 3;
    public static final Integer PERIOD_TYPE_YEAR = 4;
    public static final Integer PERIOD_TYPE_CUSTOM = 5;

    /**
     * Bây giờ, tôi cần bạn viết CategoryStatisticsService để lọc cho 1 category cụ thể trong đoạn [startDate, endDate], kết quả trả về cần có các thông tin: 1. CategoryDto (dto của category) 2. Tổng cộng (dựa theo các bill thuộc về category này trong đoạn [startDate, endDate]) 3. Trung bình hàng ngày (lấy tổng cộng chia đều cho số ngày trong [startDate, endDate]) 4. Tiến hành phân chia, ví dụ periodType nhận được là PERIOD_TYPE_WEEK thì chia theo ngày (7 ngày), PERIOD_TYPE_MONTH thì chia theo tuần, PERIOD_TYPE_QUARTER chia theo tháng, PERIOD_TYPE_YEAR chia theo quý, PERIOD_TYPE_CUSTOM thì chia theo khoảng cận gần nó nhấn, ví dụ nó > 1 năm thì chia theo năm, nếu chỉ hơn 1 quý thì theo quý, hơn 1 tháng thì chia theo tháng, hơn 1 tuần thì theo tuần, ... Kèm với mỗi đoạn phân chia là tổng theo đoạn đó Yêu cầu này khá phức tạp, hãy làm cẩn thận
     *
     */

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
