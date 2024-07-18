package com.mgmtp.cfu.specification;

import com.mgmtp.cfu.entity.CourseReview;
import org.springframework.data.jpa.domain.Specification;

public class CourseReviewSpecifications {
    public static Specification<CourseReview> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<CourseReview> hasRating(int rating) {
        return (root, query, cb) -> cb.equal(root.get("rating"), rating);
    }

    public static Specification<CourseReview> getRatingSpec(int rating) {
        if (rating == 0) {
            return Specification.where(null);
        }

        return hasRating(rating);
    }
}
