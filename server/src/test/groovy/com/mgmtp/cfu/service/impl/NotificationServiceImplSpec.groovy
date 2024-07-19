package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO
import com.mgmtp.cfu.entity.Notification
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.mapper.NotificationUserMapper
import com.mgmtp.cfu.repository.NotificationRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

class NotificationServiceImplSpec extends Specification {
    def notificationRepository = Mock(NotificationRepository)

    def notificationUserMapper = Mock(NotificationUserMapper)

    @Subject
    def notificationService = new NotificationServiceImpl(notificationRepository, notificationUserMapper)

    def "should return notifications for the current user"() {
        given:
            def user = Mock(User)
            def userId = 123L
            user.getId() >> userId

            def notifications = [new Notification(), new Notification()]
            def notificationDTOs = [new NotificationUserDTO(), new NotificationUserDTO()]
            def authentication = Mock(Authentication) {
                getCredentials() >> User.builder().id(userId).build()
            }
            SecurityContextHolder.context.authentication = authentication
            notificationRepository.findAllByUserId(userId) >> notifications
            notificationUserMapper.toListDTO(notifications) >> notificationDTOs

        when:
            def result = notificationService.getAllNotificationByCurrUser()

        then:
            result == notificationDTOs
    }
}
