package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.Notification;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.exception.ForbiddenException;
import com.mgmtp.cfu.exception.NotificationNotFoundException;
import com.mgmtp.cfu.mapper.NotificationUserMapper;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.service.NotificationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationUserMapper notificationUserMapper;

    @Override
    public List<NotificationUserDTO> getAllNotificationByCurrUser(LocalDateTime timestamp, int batchSize) {
        var user = AuthUtils.getCurrentUser();

        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(0, batchSize, sort);

        var notifications = notificationRepository.getBatchByUserId(user.getId(), timestamp, pageable);
        return notificationUserMapper.toListDTO(notifications);
    }

    @Override
    public void sendNotificationToUser(User user, NotificationType type, String message) {
        Notification notification = NotificationUtil.createNotification(type, user, message);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead() {
        var user = AuthUtils.getCurrentUser();
        notificationRepository.markAllAsRead(user.getId());
    }

    @Override
    public void markAsReadById(Long id) {

        // Check if the notification exists
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        // Check if the user is authorized to mark the notification as read
        if (!notification.getUser().getId().equals(AuthUtils.getCurrentUser().getId())) {
            throw new ForbiddenException("You can't mark this notification as read");
        }

        notification.setSeen(true);
        notificationRepository.save(notification);
    }
}
