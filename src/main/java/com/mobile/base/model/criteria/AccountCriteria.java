package com.mobile.base.model.criteria;

import com.mobile.base.model.criteria.base.BaseCriteria;
import com.mobile.base.model.entity.Account;
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
public class AccountCriteria extends BaseCriteria<Account> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String phone;
    private Boolean isSuperAdmin;

    @Override
    public Specification<Account> getSpecification() {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (StringUtils.hasText(username)) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(phone)) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
            }
            if (isSuperAdmin != null) {
                predicates.add(cb.equal(root.get("isSuperAdmin"), isSuperAdmin));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
