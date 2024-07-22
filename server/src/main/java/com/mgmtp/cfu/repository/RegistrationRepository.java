package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> getByUserId(Long userId, Sort sort);

    @Query("SELECT r FROM Registration r WHERE r.status NOT IN ?1")
    Page<Registration> findAllExceptStatus(List<RegistrationStatus> excludedStatuses, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.course.id = ?1 and (r.status in ?2)")
    int countRegistrationInCourse(Long courseId, List<RegistrationStatus> Statuses);

    Page<Registration> findAllByStatus(RegistrationStatus status, Pageable pageable);

    boolean existsByIdAndUserId(Long registrationId, Long userId);

    List<Registration> findAllByStatus(RegistrationStatus registrationStatus);
}
