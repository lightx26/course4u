package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("SELECT n FROM Notification n WHERE n.user.id = ?1 AND n.createdDate < ?2")
    List<Notification> getBatchByUserId(Long userId, LocalDateTime timestamp, Pageable pageable);

    Integer countByUserIdAndSeen(Long userId, boolean seen);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.seen = true WHERE n.user.id = ?1")
    void markAllAsRead(Long userId);
}
