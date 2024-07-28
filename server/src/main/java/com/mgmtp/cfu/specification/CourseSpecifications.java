package com.mgmtp.cfu.specification;

import com.mgmtp.cfu.dto.coursedto.CoursePageFilter;
import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.entity.CourseReview;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.CourseLevel;
import com.mgmtp.cfu.enums.CoursePageSortOption;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CourseSpecifications {
    public static Specification<Course> nameLike(String name) {
        return (root, query, builder) -> builder.like(builder.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<Course> hasStatus(CourseStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<Course> hasCategories(List<Long> categoryIds) {
        return (root, query, builder) -> {
            // Sub-query instead of join category table
            Subquery<Long> categorySubquery = query.subquery(Long.class);
            Root<Course> courseSubqueryRoot = categorySubquery.from(Course.class);
            Join<Course, Category> categoryJoin = courseSubqueryRoot.join("categories");

            categorySubquery.select(courseSubqueryRoot.get("id"))
                    .where(categoryJoin.get("id").in(categoryIds));

            // Main query
            return builder.in(root.get("id")).value(categorySubquery);
        };
    }

    public static Specification<Course> hasRatings(List<Integer> ratings) {
        return (root, query, builder) -> {
            Subquery<Double> ratingSubquery = getRatingSubquery(builder, root, query);

            List<Predicate> predicates = ratings.stream().map(
                    rating -> builder.and(
                            builder.greaterThanOrEqualTo(ratingSubquery, rating.doubleValue()),
                            builder.lessThan(ratingSubquery, rating.doubleValue() + 1)
                    )).toList();

            query.where(builder.or(predicates.toArray(new Predicate[0])));

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
            query.orderBy(
                    builder.desc(root.get("createdDate")),
                    builder.desc(root.get("id"))
            );
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
            query.orderBy(
                    cb.desc(enrollmentCountSubquery),
                    cb.desc(root.get("id"))
            );

            return query.getRestriction();
        };
    }

    public static Specification<Course> sortByRatingDesc() {
        return (root, query, builder) -> {
            Subquery<Double> ratingSubquery = getRatingSubquery(builder, root, query);

            query.orderBy(
                    builder.desc(ratingSubquery.getSelection()),
                    builder.desc(root.get("id"))
            );

            return query.getRestriction();
        };
    }

    private static Subquery<Double> getRatingSubquery(CriteriaBuilder builder, Root<Course> root, CriteriaQuery<?> query) {
        Subquery<Double> ratingSubquery = query.subquery(Double.class);
        Root<CourseReview> courseReviewRoot = ratingSubquery.from(CourseReview.class);
        Join<CourseReview, Course> courseJoin = courseReviewRoot.join("course");

        ratingSubquery.correlate(root);

        Expression<Double> avgRating = builder.coalesce(builder.avg(courseReviewRoot.get("rating")), -1.0);
        Expression<Double> roundedAvgRating = builder.function("ROUND", Double.class, avgRating, builder.literal(1));

        ratingSubquery.select(roundedAvgRating)
                .where(builder.equal(courseJoin.get("id"), root.get("id")));

        return ratingSubquery;
    }

    public static Specification<Course> getFilterSpec(CoursePageFilter filter) {
        Specification<Course> spec = Specification.where(null);

        if (!filter.getCategoryFilters().isEmpty()) {
            spec = spec.and(hasCategories(filter.getCategoryFilters()));
        }

        if (!filter.getRatingFilters().isEmpty()) {
            spec = spec.and(hasRatings(filter.getRatingFilters()));
        }

        if (!filter.getLevelFilters().isEmpty()) {
            spec = spec.and(hasLevels(filter.getLevelFilters()));
        }

        if (!filter.getPlatformFilters().isEmpty()) {
            spec = spec.and(hasPlatforms(filter.getPlatformFilters()));
        }

        return spec;
    }

    public static Specification<Course> getSortSpec(CoursePageSortOption sortBy) {
        return switch (sortBy) {
            case NEWEST -> sortByCreatedDateDesc();
            case MOST_ENROLLED -> sortByEnrollmentCountDesc(RegistrationStatusUtil.ACCEPTED_STATUSES);
            case RATING -> sortByRatingDesc();
        };
    }
}
