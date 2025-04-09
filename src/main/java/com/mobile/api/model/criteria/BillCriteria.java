package com.mobile.api.model.criteria;

import com.mobile.api.model.criteria.base.BaseCriteria;
import com.mobile.api.model.entity.Bill;
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
public class BillCriteria extends BaseCriteria<Bill> {
    @Serial
    private static final long serialVersionUID = 1L;

    private Double minAmount;
    private Double maxAmount;
    private String note;
    private Boolean isIncludedReport;
    private Long walletId;
    private Long categoryId;
    private Long tagId;
    private Long eventId;
    private Long reminderId;

    @Override
    public Specification<Bill> getSpecification() {
        return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(getBaseSpecification().toPredicate(root, query, cb));

            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }
            if (StringUtils.hasText(note)) {
                predicates.add(cb.like(root.get("note"), "%" + note + "%"));
            }
            if (isIncludedReport != null) {
                predicates.add(cb.equal(root.get("isIncludedReport"), isIncludedReport));
            }
            if (walletId != null) {
                predicates.add(cb.equal(root.get("wallet").get("id"), walletId));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (tagId != null) {
                Tag tag = new Tag();
                tag.setId(tagId);
                predicates.add(cb.isMember(tag, root.get("tags")));
            }
            if (eventId != null) {
                predicates.add(cb.equal(root.get("event").get("id"), eventId));
            }
            if (reminderId != null) {
                predicates.add(cb.equal(root.get("reminder").get("id"), reminderId));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
