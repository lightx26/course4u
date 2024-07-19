package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO
import com.mgmtp.cfu.service.NotificationService
import spock.lang.Specification
import spock.lang.Subject

class NotificationControllerSpec extends Specification {
    def notificationService = Mock(NotificationService)

    @Subject
    def notificationController = new NotificationController(notificationService)

    def "should return notifications for the current user"() {
        given:
            def notifications = [new NotificationUserDTO(id: 1L,content: "content",seen:false)]
            notificationService.getAllNotificationByCurrUser() >> notifications
        when:
            def result = notificationController.getAllNotificationByCurrUser()
        then:
            result.statusCode.value() == 200
    }
}
