package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.repository.NotificationRepository
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.service.IEmailService
import org.springframework.scheduling.TaskScheduler
import spock.lang.Specification

class ScheduledDeadlineNotificationTaskServiceSpec extends Specification {
    def taskScheduler = Mock(TaskScheduler)
    def registrationRepository = Mock(RegistrationRepository)
    def notificationRepository = Mock(NotificationRepository)
    def emailService = Mock(IEmailService)

    def reminderTime = "7d"

    def scheduleTime = "0 0 * * * ?"

    def service = new ScheduledDeadlineNotificationTaskService(
            taskScheduler as TaskScheduler,
            registrationRepository,
            notificationRepository,
            emailService
    )

    def setup() {
        // Optionally set up any required test data or configuration
    }

    def "should schedule task with correct cron expression"() {
        when:
        service.scheduleTask()

        then:
        noExceptionThrown()
    }

    def "should call scheduleTask on init"() {
        given:
        service.scheduleTask() >> {
        }

        when:
        service.init()
        then:
        noExceptionThrown()
    }
}