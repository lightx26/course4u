package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Registration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> getByUserId(Long userId, Sort sort);
}
