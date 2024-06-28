package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.course.id = ?1 and (r.status = 'DONE' or r.status = 'VERIFYING' or r.status='VERIFIED' or r.status='CLOSED')")
    int countLegitRegistrationInCourse(Long courseId);
}
