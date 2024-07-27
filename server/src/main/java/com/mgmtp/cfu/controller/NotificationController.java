package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.dto.notificationdto.NotificationsResponse;
import com.mgmtp.cfu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotificationsByCurrUser(@RequestParam(required = false) ZonedDateTime timestamp,
                                                                              @RequestParam(defaultValue = "25") int batchSize) {
        if (timestamp == null) {
            timestamp = ZonedDateTime.now();
        }
        NotificationsResponse response = new NotificationsResponse();
        response.setContent(notificationService.getNotificationsByCurrUser(timestamp, batchSize));
        response.setTotalUnread(notificationService.countUnreadNotification());

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsReadById(@PathVariable Long id) {
        notificationService.markAsReadById(id);
        return ResponseEntity.ok().build();
    }
}
