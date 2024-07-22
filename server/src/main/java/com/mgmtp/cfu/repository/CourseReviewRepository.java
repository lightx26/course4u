package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.CourseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long>, JpaSpecificationExecutor<CourseReview> {
    @Query("SELECT AVG(cr.rating) FROM CourseReview cr WHERE cr.course.id = ?1")
    Double calculateAvgRating(Long courseId);

    @Query("SELECT cr.rating, COUNT(cr.rating) as RatingCount FROM CourseReview cr WHERE cr.course.id = ?1 GROUP BY cr.rating")
    List<Object[]> getRatingsInCourse(Long courseId);
}
