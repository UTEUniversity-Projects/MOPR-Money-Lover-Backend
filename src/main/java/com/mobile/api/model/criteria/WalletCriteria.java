package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.Wallet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WalletCriteria extends BaseCriteria<Wallet> {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long currencyId;
    private String name;
    private Double minBalance;
    private Double maxBalance;
    private Boolean isPrimary;
    private Boolean turnOnNotifications;
    private Boolean chargeToTotal;

    @Override
    public Specification<Wallet> getSpecification() {
        return (Root<Wallet> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (currencyId != null) {
                predicates.add(cb.equal(root.get("currency").get("id"), currencyId));
            }
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (minBalance != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), minBalance));
            }
            if (maxBalance != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), maxBalance));
            }
            if (isPrimary != null) {
                predicates.add(cb.equal(root.get("isPrimary"), isPrimary));
            }
            if (turnOnNotifications != null) {
                predicates.add(cb.equal(root.get("turnOnNotifications"), turnOnNotifications));
            }
            if (chargeToTotal != null) {
                predicates.add(cb.equal(root.get("chargeToTotal"), chargeToTotal));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
