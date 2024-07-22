package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationUserDTO> getAllNotificationByCurrUser();
    void sendNotificationToUser(User user, NotificationType type, String message);
}
