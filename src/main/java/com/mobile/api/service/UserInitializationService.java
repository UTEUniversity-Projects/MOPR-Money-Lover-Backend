package com.mobile.api.service;

import com.mobile.api.constant.InitConstant;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.*;
import com.mobile.api.repository.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        List<Wallet> wallets = new ArrayList<>();
        
        // Find VND currency or any available currency
        Currency vndCurrency = currencyRepository.findById(8372371040731136L)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CURRENCY_NOT_FOUND));

        // Create Primary Wallet
        File primaryIcon = fileRepository.findById(8370354130157568L)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        Wallet primaryWallet = new Wallet(InitConstant.WALLET_PRIMARY, user, vndCurrency, true, true, true, primaryIcon);
        wallets.add(primaryWallet);

        // Create Secondary Wallet
        File secondaryIcon = fileRepository.findById(8370354133401600L)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        Wallet secondaryWallet = new Wallet(InitConstant.WALLET_SECONDARY, user, vndCurrency, false, true, true, secondaryIcon);
        wallets.add(secondaryWallet);
        
        // Save all wallets to database
        walletRepository.saveAll(wallets);
    }

    /**
     * Create default expense and income categories for the user
     * @param user The user who owns the categories
     */
    private void createDefaultCategories(User user) {
        // Prepare list of categories
        List<Category> categories = new ArrayList<>();

        // EXPENSE CATEGORIES
        // Create Food category
        File foodIcon = fileRepository.findById(8370354132516868L)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        Category foodCategory = new Category(InitConstant.CATEGORY_FOOD, user, true, 1, foodIcon);
        categories.add(foodCategory);

        // INCOME CATEGORIES

        // Save all categories to database
        categoryRepository.saveAll(categories);
    }
}