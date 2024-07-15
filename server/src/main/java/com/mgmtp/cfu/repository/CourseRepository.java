package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.course.id = ?1 and (r.status in ?2)")
    int countRegistrationInCourse(Long courseId, List<RegistrationStatus> Statuses);

    @Query("SELECT AVG(cr.rating) FROM CourseReview cr WHERE cr.course.id = ?1")
    Double calculateAvgRating(Long courseId);

    Course findCourseByLinkIgnoreCase(String link);
}
