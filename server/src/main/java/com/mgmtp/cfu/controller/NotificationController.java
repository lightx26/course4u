package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationUserDTO>> getAllNotificationByCurrUser() {
        return ResponseEntity.ok(notificationService.getAllNotificationByCurrUser());
    }
}
