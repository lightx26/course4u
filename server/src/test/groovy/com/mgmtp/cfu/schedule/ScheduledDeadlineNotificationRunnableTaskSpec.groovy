package com.mgmtp.cfu.schedule

import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.DurationUnit
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.repository.NotificationRepository
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.service.IEmailService
import spock.lang.Specification

import java.time.ZonedDateTime

class ScheduledDeadlineNotificationRunnableTaskSpec extends Specification {
    def registrationRepository = Mock(RegistrationRepository)
    def notificationRepository = Mock(NotificationRepository)
    def emailService = Mock(IEmailService)

    def clientUrl = "http://example.com"
    def reminderTime = "7d"

    def service = new ScheduledDeadlineNotificationRunnableTask(
            registrationRepository,
            notificationRepository,
            emailService,
            clientUrl,
            reminderTime
    )

    def setup() {
    }

    def "should send reminder messages for registrations near deadline"() {
        given:
        def registration = Registration.builder()
                .id(1)
                .status(RegistrationStatus.APPROVED)
        .course(Course.builder().name("").build())
        .startDate(ZonedDateTime.now().minusDays(10))
        .user(User.builder().username("").email("").build())
        .duration(12)
        .durationUnit(DurationUnit.DAY)
        .build()
        emailService.sendMail(_,_,_,_)>> {}
        registrationRepository.findAllByStatus(RegistrationStatus.APPROVED) >> List.of(registration)
        when:
        service.run()

        then:
        noExceptionThrown()
    }

    def "should not send reminder messages if not near deadline"() {
        given:
        def registration = Registration.builder()
                .id(1)
                .status(RegistrationStatus.APPROVED)
                .startDate(ZonedDateTime.now().minusDays(10))
                .duration(2)
                .durationUnit(DurationUnit.WEEK)
                .build()
        registrationRepository.findAllByStatus(RegistrationStatus.APPROVED) >> List.of(registration)

        when:
        service.run()

        then:
        noExceptionThrown()
    }
}
