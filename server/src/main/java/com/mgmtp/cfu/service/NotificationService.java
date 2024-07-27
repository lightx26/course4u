package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface NotificationService {
    List<NotificationUserDTO> getNotificationsByCurrUser(ZonedDateTime timestamp, int size);
    Integer countUnreadNotification();
    void sendNotificationToUser(User user, NotificationType type, String message);
    void markAllAsRead();
    void markAsReadById(Long id);
}
