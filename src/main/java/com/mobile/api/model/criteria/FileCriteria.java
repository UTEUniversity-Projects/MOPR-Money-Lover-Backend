package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.File;
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
public class FileCriteria extends BaseCriteria<File> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String scope;

    @Override
    public Specification<File> getSpecification() {
        return (Root<File> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (StringUtils.hasText(fileName)) {
                predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + fileName.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(scope)) {
                predicates.add(cb.equal(cb.lower(root.get("scope")), scope.toLowerCase()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
