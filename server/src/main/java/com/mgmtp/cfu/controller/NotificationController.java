package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationUserDTO>> getAllNotificationByCurrUser(@RequestParam(defaultValue = "1") int batch,
                                                                                  @RequestParam(defaultValue = "25") int batchSize) {
        return ResponseEntity.ok(notificationService.getAllNotificationByCurrUser(1, 20));
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
