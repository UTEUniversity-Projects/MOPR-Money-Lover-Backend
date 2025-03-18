package com.mobile.base.model.criteria;

import com.mobile.base.model.criteria.base.BaseCriteria;
import com.mobile.base.model.entity.Permission;
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
public class PermissionCriteria extends BaseCriteria<Permission> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String action;
    private String description;
    private String nameGroup;
    private String pCode;
    private Boolean showMenu;

    @Override
    public Specification<Permission> getSpecification() {
        return (Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (StringUtils.hasText(action)) {
                predicates.add(cb.like(cb.lower(root.get("action")), "%" + action.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(description)) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(nameGroup)) {
                predicates.add(cb.like(cb.lower(root.get("nameGroup")), "%" + nameGroup.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(pCode)) {
                predicates.add(cb.like(cb.lower(root.get("pCode")), "%" + pCode.toLowerCase() + "%"));
            }
            if (showMenu != null) {
                predicates.add(cb.equal(root.get("showMenu"), showMenu));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
