package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO
import com.mgmtp.cfu.service.NotificationService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class NotificationControllerSpec extends Specification {
    def notificationService = Mock(NotificationService)

    @Subject
    def notificationController = new NotificationController(notificationService)

    def "should return notifications for the current user"() {
        given:
            def notifications = [new NotificationUserDTO(id: 1L,content: "content",seen:false)]
            def timestamp = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
            notificationService.getAllNotificationByCurrUser(timestamp, 10) >> notifications
        when:
            def result = notificationController.getAllNotificationByCurrUser(timestamp, 10)
        then:
            1 * notificationService.getAllNotificationByCurrUser(timestamp, 10) >> notifications
            result.statusCode.value() == 200
    }

    def "should return ok status when mark all notification as seen"() {
        when:
            def result = notificationController.markAllAsRead()
        then:
            1 * notificationService.markAllAsRead()
            result.statusCode.value() == 200
    }

    def "should return ok status when mark notification as seen"() {
        given:
            def notificationId = 1L
        when:
            def result = notificationController.markAsReadById(notificationId)
        then:
            1 * notificationService.markAsReadById(notificationId)
            result.statusCode.value() == 200
    }
}
