package com.mgmtp.cfu.specification;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.RegistrationStatus;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class RegistrationSpecifications {

    public static Specification<Registration> getSpecs(String status, String search, String orderBy) {
        Specification<Registration> spec = excludeStatus();

        if (!status.isEmpty() && !status.equalsIgnoreCase("all")) {
            spec = spec.and(hasStatus(RegistrationStatus.valueOf(status)));
        }

        if (!search.isBlank()) {
            spec = spec.and(hasSearch(search));
        }

        if (!orderBy.isEmpty()) {
            spec = spec.and(getOrderSpec(orderBy));
        }

        return spec;
    }

    private static Specification<Registration> getOrderSpec(String orderBy) {
        Specification<Registration> spec = Specification.where(null);
        if (orderBy.equals("id")) {
            spec = spec.and(orderByIdDesc());
        } else if (orderBy.equals("lastUpdated")) {
            spec = spec.and(orderByLastUpdatedDesc());
        }

        return spec;
    }

    private static Specification<Registration> excludeStatus() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), RegistrationStatus.DRAFT);
    }

    private static Specification<Registration> hasStatus(RegistrationStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private static Specification<Registration> hasSearch(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("user").get("username")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("user").get("fullName")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("course").get("name")), "%" + search.toLowerCase() + "%")
        );
    }

    private static Specification<Registration> orderByLastUpdatedDesc() {
        return (root, query, cb) -> {
            Expression<?> lastUpdated = root.get("lastUpdated");

            Expression<Object> nullsLast = cb.selectCase()
                    .when(cb.isNull(lastUpdated), 0)
                    .otherwise(1);

            query.orderBy(cb.desc(nullsLast), cb.desc(lastUpdated));
            return query.getRestriction();
        };
    }

    private static Specification<Registration> orderByIdDesc() {
        return (root, query, cb) -> query.orderBy(cb.desc(root.get("id"))).getRestriction();
    }
}
