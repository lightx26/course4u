package com.mgmtp.cfu.schedule;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.entity.Notification;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.util.EmailUtil;
import com.mgmtp.cfu.util.NotificationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private String reminderTime;

    /**
     * Sends reminder messages to users with deadlines approaching within the specified reminder time.
     */
    private void sendRemindMessage() {
        // Calculate the deadline for the registration
        List<Registration> registrations = registrationRepository.findAllByStatus(RegistrationStatus.APPROVED);
        registrations.forEach(registration -> {
            var startDay=registration.getStartDate();
            if(startDay==null || registration.getEndDate()!=null ) return;
            startDay=startDay.plusHours(7);
            try {
                var unit = registration.getDurationUnit().name() + "S";
                var deadline = (startDay.plus(registration.getDuration(), ChronoUnit.valueOf(unit))).toLocalDateTime();
                ZonedDateTime nowInHoChiMinh = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                var now=nowInHoChiMinh.toLocalDateTime();
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
        String stringDeadline = convertDayTimeToString(deadline);
        String notificationMessage = "Attention! The deadline for the %s course is near. Please ensure all tasks are completed and your work is submitted by " + stringDeadline + ".";
        User destination = registration.getUser();
        Notification notification = NotificationUtil.createNotification(NotificationType.WARNING, registration.getUser(), String.format(notificationMessage, registration.getCourse().getName()));
        notificationRepository.save(notification);
        List<MailContentUnit> mailContentUnits = List.of(
                EmailUtil.generateTitle("Important Deadline"),
                EmailUtil.updateTitleStyle("Important Deadline"),
                EmailUtil.generateGreeting("Dear {name},", destination),
                EmailUtil.generateDeadlineEmailContent(registration.getCourse().getName(), stringDeadline)
        );
        emailService.sendMail(destination.getEmail(), EmailUtil.generateSubject("Important Deadline"), "email-template.xml", mailContentUnits);

    }

    /**
     * Runs the task to send reminder messages.
     */
    @Override
    public void run() {
        sendRemindMessage();
    }
}