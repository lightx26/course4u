package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.notificationdto.NotificationUserDTO
import com.mgmtp.cfu.entity.Notification
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.NotificationType
import com.mgmtp.cfu.exception.ForbiddenException
import com.mgmtp.cfu.exception.NotificationNotFoundException
import com.mgmtp.cfu.mapper.NotificationUserMapper
import com.mgmtp.cfu.repository.NotificationRepository
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class NotificationServiceImplSpec extends Specification {
    def notificationRepository = Mock(NotificationRepository)

    def notificationUserMapper = Mock(NotificationUserMapper)

    @Subject
    def notificationService = new NotificationServiceImpl(notificationRepository, notificationUserMapper)

    User currentUser = new User(id: 1L, username: "username")
    def setup() {
        // Mocking SecurityContext and Authentication
        SecurityContext securityContext = Mock(SecurityContext)
        Authentication authentication = Mock(Authentication)

        // Setting up the SecurityContextHolder
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        authentication.getCredentials() >> currentUser
    }

    def "should return notifications for the current user"() {
        given:
        def notifications = [new Notification(), new Notification()]
        def notificationDTOs = [new NotificationUserDTO(), new NotificationUserDTO()]

        LocalDateTime timestamp = LocalDateTime.of(2024, 7, 24, 0, 0, 0)
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate")
        Pageable pageable = PageRequest.of(0, 10, sort)

        notificationRepository.getBatchByUserId(currentUser.id, timestamp, pageable) >> notifications
        notificationUserMapper.toListDTO(notifications) >> notificationDTOs

        when:
        def result = notificationService.getAllNotificationByCurrUser(timestamp, 10)

        then:
        result == notificationDTOs
    }

    def "should create a new record of notification"() {
        given:
        def user = new User(id: 1)
        def message = "notification"

        when:
        notificationService.sendNotificationToUser(user, NotificationType.SUCCESS, message)

        then:
        1 * notificationRepository.save(_)
    }

    def "markAllAsRead should call repository with current userId"() {
        when:
        notificationService.markAllAsRead()

        then:
        1 * notificationRepository.markAllAsRead(currentUser.id)
    }

    def "markAsReadById should throw NotificationNotFoundException for invalid ID"() {
        given:
        def invalidId = 10L
        notificationRepository.findById(_) >> Optional.empty()

        when:
        notificationService.markAsReadById(invalidId)

        then:
        thrown(NotificationNotFoundException)
    }

    def "markAsReadById should throw ForbiddenException if user is not the owner"() {
        given:
        Notification notification = new Notification(user: new User(id: 2L))
        notificationRepository.findById(1L) >> Optional.of(notification)

        when:
        notificationService.markAsReadById(1L)

        then:
        thrown(ForbiddenException)
    }

    def "markAsReadById should mark notification as read for valid ID and owner"() {
        given:
        Notification notification = new Notification(user: currentUser, seen: false)
        notificationRepository.findById(1L) >> Optional.of(notification)

        when:
        notificationService.markAsReadById(1L)

        then:
        1 * notificationRepository.save(notification)
        and:
        notification.seen == true
    }
}
