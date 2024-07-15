package com.mgmtp.cfu.specification;

import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.enums.RegistrationStatus;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CourseSpecifications {
    public static Specification<Course> nameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Course> hasStatus(CourseStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<Course> hasCategories(List<Long> categoryIds) {
        return (root, query, builder) -> (
                root.join("categories").get("id").in(categoryIds)
        );
    }

    public static Specification<Course> hasRatingGreaterThan(double rating) {
        return (root, query, builder) -> {
            Subquery<Double> ratingSubquery = getRatingSubquery(builder, root, query);

            query.where(builder.greaterThanOrEqualTo(ratingSubquery, rating));

            return query.getRestriction();
        };
    }

    public static Specification<Course> hasLevels(List<CourseLevel> levels) {
        return (root, query, builder) -> (
                root.get("level").in(levels)
        );
    }

    public static Specification<Course> hasPlatforms(List<String> platforms) {
        return (root, query, builder) -> (
                root.get("platform").in(platforms)
        );
    }

    public static Specification<Course> sortByCreatedDateDesc() {
        return (root, query, builder) -> {
            query.orderBy(builder.desc(root.get("createdDate")));
            return query.getRestriction();
        };
    }

    public static Specification<Course> sortByEnrollmentCountDesc(List<RegistrationStatus> acceptedStatuses) {
        return (root, query, cb) -> {
            // Join course and registration
            Subquery<Long> enrollmentCountSubquery = query.subquery(Long.class);
            Root<Registration> registrationSubqueryRoot = enrollmentCountSubquery.from(Registration.class);
            Join<Registration, Course> courseSubqueryJoin = registrationSubqueryRoot.join("course");

            enrollmentCountSubquery.correlate(root);

            Expression<Long> caseExpression = cb.count(cb.<Long>selectCase()
                    .when(registrationSubqueryRoot.get("status").in(acceptedStatuses), 1L)
                    .otherwise((Long) null)
            );

            enrollmentCountSubquery.select(caseExpression)
                    .where(cb.equal(courseSubqueryJoin.get("id"), root.get("id")));

            // Main query
            query.orderBy(cb.desc(enrollmentCountSubquery));

            return query.getRestriction();
        };
    }

    public static Specification<Course> sortByRatingDesc() {
        return (root, query, builder) -> {
            Subquery<Double> ratingSubquery = getRatingSubquery(builder, root, query);

            query.orderBy(builder.desc(builder.selectCase()
                    .when(builder.isNull(ratingSubquery.getSelection()), -1.0)
                    .otherwise(ratingSubquery)));

            return query.getRestriction();
        };
    }

    private static Subquery<Double> getRatingSubquery(CriteriaBuilder builder, Root<Course> root, CriteriaQuery<?> query) {
        Subquery<Double> ratingSubquery = query.subquery(Double.class);
        Root<CourseReview> courseReviewRoot = ratingSubquery.from(CourseReview.class);
        Join<CourseReview, Course> courseJoin = courseReviewRoot.join("course");

        ratingSubquery.correlate(root);

        ratingSubquery.select(builder.avg(courseReviewRoot.get("rating")))
                .where(builder.equal(courseJoin.get("id"), root.get("id")));

        return ratingSubquery;
    }
}
