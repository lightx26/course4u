package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    List<NotificationUserDTO> getAllNotificationByCurrUser(int batch, int size);
    void sendNotificationToUser(User user, NotificationType type, String message);
    void markAllAsRead();
    void markAsReadById(Long id);
}
