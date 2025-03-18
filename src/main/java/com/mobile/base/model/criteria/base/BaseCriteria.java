package com.mobile.base.model.criteria.base;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BaseCriteria<T> implements Serializable {
    private Long id;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer status;

    public Specification<T> getBaseSpecification() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (createdDate != null) {
                predicates.add(cb.equal(root.get("createdDate"), createdDate));
            }
            if (modifiedDate != null) {
                predicates.add(cb.equal(root.get("modifiedDate"), modifiedDate));
            }
            if (StringUtils.hasText(createdBy)) {
                predicates.add(cb.like(cb.lower(root.get("createdBy")), "%" + createdBy.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(modifiedBy)) {
                predicates.add(cb.like(cb.lower(root.get("modifiedBy")), "%" + modifiedBy.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public abstract Specification<T> getSpecification();
}
