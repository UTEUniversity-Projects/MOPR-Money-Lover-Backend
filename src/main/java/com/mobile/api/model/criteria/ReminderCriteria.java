package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.Reminder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReminderCriteria extends BaseCriteria<Reminder> {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Instant startTime;
    private Instant endTime;

    @Override
    public Specification<Reminder> getSpecification() {
        return (Root<Reminder> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("time"), startTime));
            }
            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("time"), endTime));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
