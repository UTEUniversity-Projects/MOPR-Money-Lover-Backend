package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.Tag;
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
public class TagCriteria extends BaseCriteria<Tag> {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;

    @Override
    public Specification<Tag> getSpecification() {
        return (Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
