package com.mobile.base.model.criteria;

import com.mobile.base.model.criteria.base.BaseCriteria;
import com.mobile.base.model.entity.User;
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
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserCriteria extends BaseCriteria<User> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String phone;
    private Integer kind;
    private Boolean isSuperAdmin;
    private Integer gender;
    private LocalDateTime birthday;

    @Override
    public Specification<User> getSpecification() {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (StringUtils.hasText(username)) {
                predicates.add(cb.like(cb.lower(root.get("account").get("username")), "%" + username.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(cb.lower(root.get("account").get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(phone)) {
                predicates.add(cb.like(cb.lower(root.get("account").get("phone")), "%" + phone.toLowerCase() + "%"));
            }
            if (kind != null) {
                predicates.add(cb.equal(root.get("account").get("kind"), kind));
            }
            if (isSuperAdmin != null) {
                predicates.add(cb.equal(root.get("account").get("isSuperAdmin"), isSuperAdmin));
            }
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }
            if (birthday != null) {
                predicates.add(cb.equal(root.get("birthday"), birthday));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
