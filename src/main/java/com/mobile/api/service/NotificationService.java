package com.mobile.api.service;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.model.entity.Budget;
import com.mobile.api.model.entity.Notification;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Wallet;
import com.mobile.api.repository.jpa.NotificationRepository;
import com.mobile.api.utils.PeriodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void scanToCreateNotification(User user, Budget budget) {
        String categoryName = budget.getCategory().getName();
        BigDecimal budgetBalance = budget.getAmount().subtract(budget.getSpentAmount());
        Integer periodType = budget.getPeriodType();

        if (budgetBalance.compareTo(BigDecimal.ZERO) < 0) {
            createOverBudget(user, budget.getWallet(), categoryName, budgetBalance, periodType);
        } else if (budgetBalance.compareTo(BigDecimal.ZERO) == 0) {
            createSpendBudget(user, budget.getWallet(), categoryName, budgetBalance, periodType);
        } else if (budgetBalance.divide(budget.getAmount(), 9, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(0.1D)) < 0) {
            createSpendingAlert(user, budget.getWallet(), categoryName, budgetBalance, periodType);
        }
    }

    public void createSpendingAlert(User user, Wallet wallet, String categoryName, BigDecimal budgetBalance, Integer periodType) {
        String content = String.format("Cẩn thận sắp vượt ngân sách! Bạn chỉ còn lại %.2fđ trong ngân sách %s %s.",
                budgetBalance, categoryName, PeriodUtils.definePeriodName(periodType));

        Notification notification = new Notification(
                user, wallet, content,
                BaseConstant.NOTIFICATION_TYPE_BUDGET, BaseConstant.NOTIFICATION_SCOPE_INDIVIDUAL
        );
        notificationRepository.save(notification);
    }

    public void createSpendBudget(User user, Wallet wallet, String categoryName, BigDecimal budgetBalance, Integer periodType) {
        String content = String.format("Ồ, bạn đã chi tiêu %.2f%% ngân sách %s %s rồi đấy.",
                budgetBalance, categoryName, PeriodUtils.definePeriodName(periodType));

        Notification notification = new Notification(
                user, wallet, content,
                BaseConstant.NOTIFICATION_TYPE_BUDGET, BaseConstant.NOTIFICATION_SCOPE_INDIVIDUAL
        );
        notificationRepository.save(notification);
    }

    public void createOverBudget(User user, Wallet wallet, String categoryName, BigDecimal budgetBalance, Integer periodType) {
        String content = String.format("Ôi không! Bạn đã chi tiêu vượt %.2fđ trong ngân sách %s %s.",
                budgetBalance, categoryName, PeriodUtils.definePeriodName(periodType));

        Notification notification = new Notification(
                user, wallet, content,
                BaseConstant.NOTIFICATION_TYPE_BUDGET, BaseConstant.NOTIFICATION_SCOPE_INDIVIDUAL
        );
        notificationRepository.save(notification);
    }
}
