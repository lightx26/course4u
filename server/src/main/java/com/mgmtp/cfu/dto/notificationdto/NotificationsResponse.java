package com.mgmtp.cfu.dto.notificationdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class NotificationsResponse {
    List<NotificationUserDTO> content = new ArrayList<>();
    Integer totalUnread;
}
