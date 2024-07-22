package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Course findFirstByLinkIgnoreCase(String link);
    @Query("SELECT c FROM Course c WHERE c.status = 'AVAILABLE' AND c.link = ?1")
    Course findFirstByLinkAndStatus(String link);


}
