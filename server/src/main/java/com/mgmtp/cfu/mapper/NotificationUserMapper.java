package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO;
import com.mgmtp.cfu.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class NotificationUserMapper implements DTOMapper<NotificationUserDTO, Notification>{
    public abstract List<NotificationUserDTO> toListDTO(List<Notification> notifications);
}
