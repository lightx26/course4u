package com.mgmtp.cfu.util;

import com.mgmtp.cfu.entity.Notification;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;
import java.time.LocalDateTime;

public class NotificationUtil {
    public static Notification createNotification(NotificationType type, User user, String content) {
        return Notification.builder()
                .type(type)
                .seen(false)
                .content(content)
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();
    }
}
