package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Category;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.CourseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Course findFirstByLinkIgnoreCase(String link);

    @Query(
            "SELECT co FROM Course co " +
                    "JOIN co.categories cat " +
                    "WHERE co.id != :courseId AND co.status = :status AND cat IN :categories " +
                    "GROUP BY co.id " +
                    "ORDER BY COUNT(cat.id) DESC "
    )
    List<Course> findTop8RelatedCourse(@Param("categories") Set<Category> categories, Pageable pageable, @Param("courseId") Long courseId, @Param("status") CourseStatus courseStatus
    );
    Optional<Course> findFirstByLinkIgnoreCaseAndStatus(String link, CourseStatus courseStatus);





}
