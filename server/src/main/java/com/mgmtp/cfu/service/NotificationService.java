package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationUserDTO> getAllNotificationByCurrUser();
}
