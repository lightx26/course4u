package com.mgmtp.cfu.specification;

import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.enums.CategoryStatus;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {
    public static Specification<Category> hasStatus(CategoryStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Category> nameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Category> sortByNameAsc() {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("name")));
            return query.getRestriction();
        };
    }
}
