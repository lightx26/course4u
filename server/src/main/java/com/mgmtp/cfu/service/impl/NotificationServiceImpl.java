package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.Notification;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.mapper.NotificationUserMapper;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.service.NotificationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationUserMapper notificationUserMapper;

    @Override
    public List<NotificationUserDTO> getAllNotificationByCurrUser() {
        var user = AuthUtils.getCurrentUser();
        var notifications = notificationRepository.findAllByUserIdOrderByCreatedDateDesc(user.getId());
        return notificationUserMapper.toListDTO(notifications);
    }

    @Override
    public void sendNotificationToUser(User user, NotificationType type, String message) {
        Notification notification = NotificationUtil.createNotification(type, user, message);
        notificationRepository.save(notification);
    }
}
