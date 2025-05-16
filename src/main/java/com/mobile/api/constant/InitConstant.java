package com.mobile.api.constant;

import java.util.Arrays;
import java.util.List;

public class InitConstant {
    /**
     * INITIALIZATION_WALLET constants
     */
    public static final String WALLET_PRIMARY = "Tiền mặt";

    /**
     * INITIALIZATION_CATEGORY EXPENSE constants
     */
    public static final String CATEGORY_FOOD = "Ăn uống";
    public static final String CATEGORY_INSURANCE = "Bảo hiểm";
    public static final String CATEGORY_OTHER_EXPENSES = "Các chi phí khác";
    public static final String CATEGORY_MOVE = "Di chuyển";
    public static final String CATEGORY_VEHICLE_MAINTENANCE = "Bảo dưỡng xe";
    public static final String CATEGORY_FAMILY = "Gia đình";
    public static final String CATEGORY_FAMILY_SERVICE = "Dịch vụ gia đình";
    public static final String CATEGORY_HOME_REPAIR_AND_DECORATION = "Sửa & trang trí nhà";
    public static final String CATEGORY_PET = "Vật nuôi";
    public static final String CATEGORY_ENTERTAINMENT = "Giải trí";
    public static final String CATEGORY_ONLINE_SERVICES = "Dịch vụ trực tuyến";
    public static final String CATEGORY_PLAY = "Vui chơi";
    public static final String CATEGORY_EDUCATION = "Giáo dục";
    public static final String CATEGORY_BILL_AND_UTILITIES = "Hóa đơn & tiện ích";
    public static final String CATEGORY_GAS_BILL = "Hóa đơn gas";
    public static final String CATEGORY_INTERNET_BILL = "Hóa đơn internet";
    public static final String CATEGORY_WATER_BILL = "Hóa đơn nước";
    public static final String CATEGORY_OTHER_SERVICES_BILL = "Hóa đơn tiện ích khác";
    public static final String CATEGORY_TV_BILL = "Hóa đơn TV";
    public static final String CATEGORY_ELECTRICITY_BILL = "Hóa đơn điện";
    public static final String CATEGORY_PHONE_BILL = "Hóa đơn điện thoại";
    public static final String CATEGORY_RENT_HOUSE = "Thuê nhà";
    public static final String CATEGORY_SHOPPING = "Mua sắm";
    public static final String CATEGORY_BEATIFY = "Làm đẹp";
    public static final String CATEGORY_PERSONAL_BELONGINGS = "Đồ dùng cá nhân";
    public static final String CATEGORY_HOUSEHOLD_APPLIANCES = "Đồ gia dụng";
    public static final String CATEGORY_GIFTS_AND_DONATIONS = "Quà tặng & quyên góp";
    public static final String CATEGORY_HEALTH = "Sức khỏe";
    public static final String CATEGORY_PHYSICAL_EXAMINATION = "Khám sức khỏe";
    public static final String CATEGORY_SPORTS = "Thể dục thể thao";
    public static final String CATEGORY_TRANSFER_MONEY = "Tiền chuyển đi";
    public static final String CATEGORY_PAY_INTEREST = "Trả lãi";
    public static final String CATEGORY_INVESTMENT = "Đầu tư";
    public static final String CATEGORY_LOAN = "Cho vay";
    public static final String CATEGORY_DEBT_PAYMENT = "Trả nợ";

    /**
     * INITIALIZATION_CATEGORY INCOME constants
     */
    public static final String CATEGORY_SALARY = "Lương";
    public static final String CATEGORY_PROFIT = "Thu lãi";
    public static final String CATEGORY_OTHER_INCOME = "Thu nhập khác";
    public static final String CATEGORY_RECEIVED_MONEY = "Tiền chuyển đến";
    public static final String CATEGORY_DEBT_COLLECTION = "Thu nợ";
    public static final String CATEGORY_BORROW = "Đi vay";

    /**
     * ICON_INITIALIZATION_CURRENCY constants
     */
    public static final Long CURRENCY_VND = 8372371040731136L; // VND

    /**
     * ICON_INITIALIZATION_WALLET constants
     */
    public static final Long ICON_WALLET_PRIMARY = 8370354133401600L; // "Tiền mặt"

    /**
     * ICON_INITIALIZATION_CATEGORY EXPENSE constants
     */
    public static final Long ICON_CATEGORY_FOOD = 8370354132516868L; // "Ăn uống"
    public static final Long ICON_CATEGORY_INSURANCE = 8370354132615171L; // "Bảo hiểm"
    public static final Long ICON_CATEGORY_OTHER_EXPENSES = 8370354131009537L; // "Các chi phí khác"
    public static final Long ICON_CATEGORY_MOVE = 8370354130485248L; // "Di chuyển"
    public static final Long ICON_CATEGORY_VEHICLE_MAINTENANCE = 8370354133041152L; // "Bảo dưỡng xe"
    public static final Long ICON_CATEGORY_FAMILY = 8370354130845697L; // "Gia đình"
    public static final Long ICON_CATEGORY_FAMILY_SERVICE = 8370354133467138L; // "Dịch vụ gia đình"
    public static final Long ICON_CATEGORY_HOME_REPAIR_AND_DECORATION = 8370354131992580L; // "Sửa & trang trí nhà"
    public static final Long ICON_CATEGORY_PET = 8370354131009536L; // "Vật nuôi"
    public static final Long ICON_CATEGORY_ENTERTAINMENT = 8370354132025345L; // "Giải trí"
    public static final Long ICON_CATEGORY_ONLINE_SERVICES = 8370354130878465L; // "Dịch vụ trực tuyến"
    public static final Long ICON_CATEGORY_PLAY = 8370354133270530L; // "Vui chơi"
    public static final Long ICON_CATEGORY_EDUCATION = 8370354131992576L; // "Giáo dục"
    public static final Long ICON_CATEGORY_BILL_AND_UTILITIES = 8370354131730432L; // "Hóa đơn & tiện ích"
    public static final Long ICON_CATEGORY_GAS_BILL = 8370354131435521L; // "Hóa đơn gas"
    public static final Long ICON_CATEGORY_INTERNET_BILL = 8370354132516867L; // "Hóa đơn internet"
    public static final Long ICON_CATEGORY_WATER_BILL = 8370354133434369L; // "Hóa đơn nước"
    public static final Long ICON_CATEGORY_OTHER_SERVICES_BILL = 8370354132975621L; // "Hóa đơn tiện ích khác"
    public static final Long ICON_CATEGORY_TV_BILL = 8370354133172224L; // "Hóa đơn TV"
    public static final Long ICON_CATEGORY_ELECTRICITY_BILL = 8370354129797121L; // "Hóa đơn điện"
    public static final Long ICON_CATEGORY_PHONE_BILL = 8370354132090881L; // "Hóa đơn điện thoại"
    public static final Long ICON_CATEGORY_RENT_HOUSE = 8370354129829888L; // "Thuê nhà"
    public static final Long ICON_CATEGORY_SHOPPING = 8370354130649088L; // "Mua sắm"
    public static final Long ICON_CATEGORY_BEATIFY = 8370354130944002L; // "Làm đẹp"
    public static final Long ICON_CATEGORY_PERSONAL_BELONGINGS = 8370354132680706L; // "Đồ dùng cá nhân"
    public static final Long ICON_CATEGORY_HOUSEHOLD_APPLIANCES = 8370354132451328L; // "Đồ gia dụng"
    public static final Long ICON_CATEGORY_GIFTS_AND_DONATIONS = 8370354131468289L; // "Quà tặng & quyên góp"
    public static final Long ICON_CATEGORY_HEALTH = 8370354131501057L; // "Sức khỏe"
    public static final Long ICON_CATEGORY_PHYSICAL_EXAMINATION = 8370354131304448L; // "Khám sức khỏe"
    public static final Long ICON_CATEGORY_SPORTS = 8370354129764353L; // "Thể dục thể thao"
    public static final Long ICON_CATEGORY_TRANSFER_MONEY = 8370354131337216L; // "Tiền chuyển đi"
    public static final Long ICON_CATEGORY_PAY_INTEREST = 8370354130976769L; // "Trả lãi"
    public static final Long ICON_CATEGORY_INVESTMENT = 8370354131861505L; // "Đầu tư"
    public static final Long ICON_CATEGORY_LOAN = 8370354131959811L; // "Cho vay"
    public static final Long ICON_CATEGORY_DEBT_PAYMENT = 8370354132025346L; // "Trả nợ"

    /**
     * ICON_INITIALIZATION_CATEGORY INCOME constants
     */
    public static final Long ICON_CATEGORY_SALARY = 8370354131664896L; // "Lương"
    public static final Long ICON_CATEGORY_PROFIT = 8370354131959812L; // "Thu lãi"
    public static final Long ICON_CATEGORY_OTHER_INCOME = 8370354132877313L; // "Thu nhập khác"
    public static final Long ICON_CATEGORY_RECEIVED_MONEY = 8370354132320256L; // "Tiền chuyển đến"
    public static final Long ICON_CATEGORY_DEBT_COLLECTION = 8370354131468290L; // "Thu nợ"
    public static final Long ICON_CATEGORY_BORROW = 8370354133041155L; // "Đi vay"


    /**
     * Consolidated list of all icon IDs needed for initialization
     * Used for efficient batch loading of icons
     */
    public static final List<Long> ALL_REQUIRED_ICON_IDS = Arrays.asList(
            // Wallet icons
            ICON_WALLET_PRIMARY,

            // Expense category icons
            ICON_CATEGORY_FOOD,
            ICON_CATEGORY_INSURANCE,
            ICON_CATEGORY_OTHER_EXPENSES,
            ICON_CATEGORY_MOVE,
            ICON_CATEGORY_VEHICLE_MAINTENANCE,
            ICON_CATEGORY_FAMILY,
            ICON_CATEGORY_FAMILY_SERVICE,
            ICON_CATEGORY_HOME_REPAIR_AND_DECORATION,
            ICON_CATEGORY_PET,
            ICON_CATEGORY_ENTERTAINMENT,
            ICON_CATEGORY_ONLINE_SERVICES,
            ICON_CATEGORY_PLAY,
            ICON_CATEGORY_EDUCATION,
            ICON_CATEGORY_BILL_AND_UTILITIES,
            ICON_CATEGORY_GAS_BILL,
            ICON_CATEGORY_INTERNET_BILL,
            ICON_CATEGORY_WATER_BILL,
            ICON_CATEGORY_OTHER_SERVICES_BILL,
            ICON_CATEGORY_TV_BILL,
            ICON_CATEGORY_ELECTRICITY_BILL,
            ICON_CATEGORY_PHONE_BILL,
            ICON_CATEGORY_RENT_HOUSE,
            ICON_CATEGORY_SHOPPING,
            ICON_CATEGORY_BEATIFY,
            ICON_CATEGORY_PERSONAL_BELONGINGS,
            ICON_CATEGORY_HOUSEHOLD_APPLIANCES,
            ICON_CATEGORY_GIFTS_AND_DONATIONS,
            ICON_CATEGORY_HEALTH,
            ICON_CATEGORY_PHYSICAL_EXAMINATION,
            ICON_CATEGORY_SPORTS,
            ICON_CATEGORY_TRANSFER_MONEY,
            ICON_CATEGORY_PAY_INTEREST,
            ICON_CATEGORY_INVESTMENT,
            ICON_CATEGORY_LOAN,
            ICON_CATEGORY_DEBT_PAYMENT,

            // Income category icons
            ICON_CATEGORY_SALARY,
            ICON_CATEGORY_PROFIT,
            ICON_CATEGORY_OTHER_INCOME,
            ICON_CATEGORY_RECEIVED_MONEY,
            ICON_CATEGORY_DEBT_COLLECTION,
            ICON_CATEGORY_BORROW
    );
}
