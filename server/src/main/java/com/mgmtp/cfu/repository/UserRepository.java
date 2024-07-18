package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByEmail(String email);

    List<User> findAllByRole(Role role);

    @Transactional
    @Modifying
    @Query("UPDATE User SET password = :password WHERE id = :id")
    void changeUserPassword(@Param("password") String password, @Param("id") Long id);

}
