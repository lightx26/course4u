package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Course findFirstByLinkIgnoreCase(String link);
}
