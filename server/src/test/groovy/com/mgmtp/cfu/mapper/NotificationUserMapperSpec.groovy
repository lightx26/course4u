package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO
import com.mgmtp.cfu.entity.Notification
import com.mgmtp.cfu.enums.NotificationType
import org.mapstruct.factory.Mappers
import spock.lang.Specification

import java.time.LocalDateTime

class NotificationUserMapperSpec extends Specification {
    def notificationUserMapper = Mappers.getMapper(NotificationUserMapper.class)

    def "ToListDTO"() {
        given:
            def notifications = [new Notification(id: 1L,content: "content",createdDate: LocalDateTime.now(),seen: false,type: NotificationType.ERROR)]
        when:
            def result = notificationUserMapper.toListDTO(notifications)
        then:
            result.size() == 1
            result[0].id == notifications[0].id
            result[0].content == notifications[0].content
            result[0].createdDate == notifications[0].createdDate
            result[0].seen == notifications[0].seen
            result[0].type == notifications[0].type
    }
}
