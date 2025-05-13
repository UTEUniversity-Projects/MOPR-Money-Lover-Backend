package com.mobile.api.service;

import com.mobile.api.constant.InitConstant;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.*;
import com.mobile.api.model.entity.Currency;
import com.mobile.api.repository.jpa.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for initializing default data for new users
 * Provides basic wallet and categories to help users get started
 */
@Service
public class UserInitializationService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private FileRepository fileRepository;

    // Cache to store icons
    private final Map<Long, File> iconCache = new ConcurrentHashMap<>();
    // Default currency
    private Currency vndCurrency;

    /**
     * Initialize required data after service creation
     * Retrieves and caches all necessary icons and default currency
     */
    @PostConstruct
    public void init() {
        // Load all required icons at once and cache them
        loadAndCacheIcons();

        // Get default currency (VND)
        vndCurrency = currencyRepository.findById(InitConstant.CURRENCY_VND)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));
    }

    /**
     * Load all required icons and store them in the cache
     */
    private void loadAndCacheIcons() {
        // Fetch all required icons in a single query using the constant list
        List<File> icons = fileRepository.findAllById(InitConstant.ALL_REQUIRED_ICON_IDS);

        // Create a map of icons by ID
        Map<Long, File> fetchedIcons = icons.stream()
                .collect(Collectors.toMap(File::getId, icon -> icon));

        // Store icons in the cache
        iconCache.putAll(fetchedIcons);

        // If we're missing any icons, set up a fallback
        if (!icons.isEmpty() && iconCache.size() < InitConstant.ALL_REQUIRED_ICON_IDS.size()) {
            File defaultIcon = icons.get(0);
            for (Long iconId : InitConstant.ALL_REQUIRED_ICON_IDS) {
                iconCache.putIfAbsent(iconId, defaultIcon);
            }
        }
    }

    /**
     * Initialize default data for a newly registered user
     * @param user The new user entity
     */
    @Transactional
    public void initializeUserData(User user) {
        // Create default wallet
        createDefaultWallet(user);

        // Create default categories
        createDefaultCategories(user);
    }

    /**
     * Create a default wallet for the user
     * @param user The user who owns the wallet
     */
    private void createDefaultWallet(User user) {
        List<Wallet> wallets = List.of(
                // Primary Wallet
                new Wallet(
                        InitConstant.WALLET_PRIMARY,
                        user,
                        vndCurrency,
                        true,
                        true,
                        true,
                        iconCache.get(InitConstant.ICON_WALLET_PRIMARY)
                )
        );
        
        // Save all wallets to database
        walletRepository.saveAll(wallets);
    }

    /**
     * Create default expense and income categories for the user
     * @param user The user who owns the categories
     */
    private void createDefaultCategories(User user) {
        // Prepare list of categories
        List<Category> categories = Arrays.asList(
                // EXPENSE CATEGORIES
                new Category(InitConstant.CATEGORY_FOOD, user, true, 1, iconCache.get(InitConstant.ICON_CATEGORY_FOOD)),
                new Category(InitConstant.CATEGORY_INSURANCE, user, true, 2, iconCache.get(InitConstant.ICON_CATEGORY_INSURANCE)),
                new Category(InitConstant.CATEGORY_OTHER_EXPENSES, user, true, 3, iconCache.get(InitConstant.ICON_CATEGORY_OTHER_EXPENSES)),
                new Category(InitConstant.CATEGORY_MOVE, user, true, 4, iconCache.get(InitConstant.ICON_CATEGORY_MOVE)),
                new Category(InitConstant.CATEGORY_VEHICLE_MAINTENANCE, user, true, 5, iconCache.get(InitConstant.ICON_CATEGORY_VEHICLE_MAINTENANCE)),
                new Category(InitConstant.CATEGORY_FAMILY, user, true, 6, iconCache.get(InitConstant.ICON_CATEGORY_FAMILY)),
                new Category(InitConstant.CATEGORY_FAMILY_SERVICE, user, true, 7, iconCache.get(InitConstant.ICON_CATEGORY_FAMILY_SERVICE)),
                new Category(InitConstant.CATEGORY_HOME_REPAIR_AND_DECORATION, user, true, 8, iconCache.get(InitConstant.ICON_CATEGORY_HOME_REPAIR_AND_DECORATION)),
                new Category(InitConstant.CATEGORY_PET, user, true, 9, iconCache.get(InitConstant.ICON_CATEGORY_PET)),
                new Category(InitConstant.CATEGORY_ENTERTAINMENT, user, true, 10, iconCache.get(InitConstant.ICON_CATEGORY_ENTERTAINMENT)),
                new Category(InitConstant.CATEGORY_ONLINE_SERVICES, user, true, 11, iconCache.get(InitConstant.ICON_CATEGORY_ONLINE_SERVICES)),
                new Category(InitConstant.CATEGORY_PLAY, user, true, 12, iconCache.get(InitConstant.ICON_CATEGORY_PLAY)),
                new Category(InitConstant.CATEGORY_EDUCATION, user, true, 13, iconCache.get(InitConstant.ICON_CATEGORY_EDUCATION)),
                new Category(InitConstant.CATEGORY_BILL_AND_UTILITIES, user, true, 14, iconCache.get(InitConstant.ICON_CATEGORY_BILL_AND_UTILITIES)),
                new Category(InitConstant.CATEGORY_GAS_BILL, user, true, 15, iconCache.get(InitConstant.ICON_CATEGORY_GAS_BILL)),
                new Category(InitConstant.CATEGORY_INTERNET_BILL, user, true, 16, iconCache.get(InitConstant.ICON_CATEGORY_INTERNET_BILL)),
                new Category(InitConstant.CATEGORY_WATER_BILL, user, true, 17, iconCache.get(InitConstant.ICON_CATEGORY_WATER_BILL)),
                new Category(InitConstant.CATEGORY_OTHER_SERVICES_BILL, user, true, 18, iconCache.get(InitConstant.ICON_CATEGORY_OTHER_SERVICES_BILL)),
                new Category(InitConstant.CATEGORY_TV_BILL, user, true, 19, iconCache.get(InitConstant.ICON_CATEGORY_TV_BILL)),
                new Category(InitConstant.CATEGORY_ELECTRICITY_BILL, user, true, 20, iconCache.get(InitConstant.ICON_CATEGORY_ELECTRICITY_BILL)),
                new Category(InitConstant.CATEGORY_PHONE_BILL, user, true, 21, iconCache.get(InitConstant.ICON_CATEGORY_PHONE_BILL)),
                new Category(InitConstant.CATEGORY_RENT_HOUSE, user, true, 22, iconCache.get(InitConstant.ICON_CATEGORY_RENT_HOUSE)),
                new Category(InitConstant.CATEGORY_SHOPPING, user, true, 23, iconCache.get(InitConstant.ICON_CATEGORY_SHOPPING)),
                new Category(InitConstant.CATEGORY_BEATIFY, user, true, 24, iconCache.get(InitConstant.ICON_CATEGORY_BEATIFY)),
                new Category(InitConstant.CATEGORY_PERSONAL_BELONGINGS, user, true, 25, iconCache.get(InitConstant.ICON_CATEGORY_PERSONAL_BELONGINGS)),
                new Category(InitConstant.CATEGORY_HOUSEHOLD_APPLIANCES, user, true, 26, iconCache.get(InitConstant.ICON_CATEGORY_HOUSEHOLD_APPLIANCES)),
                new Category(InitConstant.CATEGORY_GIFTS_AND_DONATIONS, user, true, 27, iconCache.get(InitConstant.ICON_CATEGORY_GIFTS_AND_DONATIONS)),
                new Category(InitConstant.CATEGORY_HEALTH, user, true, 28, iconCache.get(InitConstant.ICON_CATEGORY_HEALTH)),
                new Category(InitConstant.CATEGORY_PHYSICAL_EXAMINATION, user, true, 29, iconCache.get(InitConstant.ICON_CATEGORY_PHYSICAL_EXAMINATION)),
                new Category(InitConstant.CATEGORY_SPORTS, user, true, 30, iconCache.get(InitConstant.ICON_CATEGORY_SPORTS)),
                new Category(InitConstant.CATEGORY_TRANSFER_MONEY, user, true, 31, iconCache.get(InitConstant.ICON_CATEGORY_TRANSFER_MONEY)),
                new Category(InitConstant.CATEGORY_PAY_INTEREST, user, true, 32, iconCache.get(InitConstant.ICON_CATEGORY_PAY_INTEREST)),
                new Category(InitConstant.CATEGORY_INVESTMENT, user, true, 33, iconCache.get(InitConstant.ICON_CATEGORY_INVESTMENT)),
                new Category(InitConstant.CATEGORY_LOAN, user, true, 34, iconCache.get(InitConstant.ICON_CATEGORY_LOAN)),
                new Category(InitConstant.CATEGORY_DEBT_PAYMENT, user, true, 35, iconCache.get(InitConstant.ICON_CATEGORY_DEBT_PAYMENT)),

                // INCOME CATEGORIES
                new Category(InitConstant.CATEGORY_SALARY, user, false, 1, iconCache.get(InitConstant.ICON_CATEGORY_SALARY)),
                new Category(InitConstant.CATEGORY_PROFIT, user, false, 2, iconCache.get(InitConstant.ICON_CATEGORY_PROFIT)),
                new Category(InitConstant.CATEGORY_OTHER_INCOME, user, false, 3, iconCache.get(InitConstant.ICON_CATEGORY_OTHER_INCOME)),
                new Category(InitConstant.CATEGORY_RECEIVED_MONEY, user, false, 4, iconCache.get(InitConstant.ICON_CATEGORY_RECEIVED_MONEY)),
                new Category(InitConstant.CATEGORY_DEBT_COLLECTION, user, false, 5, iconCache.get(InitConstant.ICON_CATEGORY_DEBT_COLLECTION)),
                new Category(InitConstant.CATEGORY_BORROW, user, false, 6, iconCache.get(InitConstant.ICON_CATEGORY_BORROW))
        );

        // Save all categories to database
        categoryRepository.saveAll(categories);
    }
}