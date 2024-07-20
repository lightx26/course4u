package com.mgmtp.cfu.dto.notificationdto;

import com.mgmtp.cfu.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class NotificationUserDTO {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private NotificationType type;
    private Boolean seen;
}