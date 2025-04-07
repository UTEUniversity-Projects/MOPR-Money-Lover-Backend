package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.Currency;
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
public class CurrencyCriteria extends BaseCriteria<Currency> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String code;

    @Override
    public Specification<Currency> getSpecification() {
        return (Root<Currency> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(code)) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
