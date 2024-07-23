package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByUserId(Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.seen = true WHERE n.user.id = ?1")
    void markAllAsRead(Long userId);
}
