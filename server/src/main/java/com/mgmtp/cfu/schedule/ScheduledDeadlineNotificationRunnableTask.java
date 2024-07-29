package com.mgmtp.cfu.schedule;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.util.NotificationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mgmtp.cfu.util.Constant.DEADLINE_WARNING_EMAIL;
import static com.mgmtp.cfu.util.TimeConverter.convertDayTimeToString;
import static com.mgmtp.cfu.util.TimeConverter.plus;

/**
 * Runnable task that sends reminder notifications for approaching deadlines.
 */
@AllArgsConstructor
@Slf4j
public class ScheduledDeadlineNotificationRunnableTask implements Runnable {
    private final RegistrationRepository registrationRepository;
    private final NotificationRepository notificationRepository;
    private final IEmailService emailService;
    private String clientUrl;
    private String reminderTime;

    /**
     * Sends reminder messages to users with deadlines approaching within the specified reminder time.
     */
    private void sendRemindMessage() {
        // Calculate the deadline for the registration
        List<Registration> registrations = registrationRepository.findAllByStatus(RegistrationStatus.APPROVED);
        registrations.forEach(registration -> {
            try {
                var unit = registration.getDurationUnit().name() + "S";
                var deadline = (registration.getStartDate().plus(registration.getDuration(), ChronoUnit.valueOf(unit))).toLocalDateTime();
                LocalDateTime now = LocalDateTime.now();
                boolean isNearDeadline = now.isBefore(deadline) && plus(now, reminderTime).isAfter(deadline);
                if (isNearDeadline) {
                    notifyNotification(registration, deadline);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    /**
     * Creates and sends a notification for the registration and sends a reminder email.
     *
     * @param registration provide information about deadline.
     * @param deadline     The deadline for the registration
     */
    private void notifyNotification(Registration registration, LocalDateTime deadline) {
        String remindMessage = "That the deadline for the %s course is approaching. Please" +
                " make sure to complete all necessary tasks and submit your work by the due date.";
        var stringDeadline = convertDayTimeToString(deadline);
        var notificationMessage = "Attention! The deadline for the %s course is near. Please ensure all tasks are completed and your work is submitted by " + stringDeadline + " .";

        var destination = registration.getUser();
        remindMessage = String.format(remindMessage, registration.getCourse().getName());
        var notification = NotificationUtil.createNotification(NotificationType.WARNING, registration.getUser(), String.format(notificationMessage, registration.getCourse().getName()));
        notificationRepository.save(notification);
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().content(remindMessage).id("content").tag("div").build(),
                MailContentUnit.builder().id("user_greeting").tag("div").content("Dear " + destination.getUsername()).build(),
                MailContentUnit.builder().id("deadline").tag("div").content("Deadline: " + stringDeadline).build(),
                MailContentUnit.builder().id("client_url").tag("a").href(clientUrl).build()
        );
        emailService.sendMessage(destination.getEmail(), "Important Deadline", DEADLINE_WARNING_EMAIL, mailContentUnits);

    }

    /**
     * Runs the task to send reminder messages.
     */
    @Override
    public void run() {
        sendRemindMessage();
    }
}