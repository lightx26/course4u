package com.mgmtp.cfu.repository;

import com.mgmtp.cfu.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByUserIdOrderByCreatedDateDesc(Long userId);
    List<Notification> findAllByUserId(Long userId);
}
