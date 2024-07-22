package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.repository.NotificationRepository;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.schedule.ScheduledDeadlineNotificationRunnableTask;
import com.mgmtp.cfu.service.IEmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;


/**
 * Service for scheduling deadline notification tasks.
 */
@Service
@RequiredArgsConstructor
public class ScheduledDeadlineNotificationTaskService {
    private final TaskScheduler taskScheduler;
    private final RegistrationRepository registrationRepository;
    private final NotificationRepository notificationRepository;
    private final IEmailService emailService;

    @Value("${course4u.deadline.schedule.period}")
    private String scheduleTime; // Cron expression to schedule tasks
    /*
     * we can use * to determine that every time unit will happen the scheduled task.
     */

    /**
     * Schedules the task to run based on the provided cron expression.
     * Uses the cron expression to define the scheduling frequency.
     * The expression is expected to configure minutes and hours.
     */
    @Value("${course4u.vite.frontend.url}")
    private String clientUrl;
    @Value("${course4u.deadline.schedule.reminder-time}")
    private String reminderTime;

    /*
     * 7 day : 7d
     * 7 minute: 7min
     * 7 week: 7wk
     */

    public void scheduleTask() {
        reminderTime=reminderTime!=null?reminderTime:"7d";
        scheduleTime=scheduleTime!=null?scheduleTime:"0 8";
        Runnable task = new ScheduledDeadlineNotificationRunnableTask(
                registrationRepository,
                notificationRepository,
                emailService,
                clientUrl,
                reminderTime
        );

        // Create a CronTrigger with the specified cron expression
        CronTrigger cronTrigger = new CronTrigger("0 " + scheduleTime + " * * ?");
        taskScheduler.schedule(task, cronTrigger);
    }

    /**
     * Initializes the scheduling by calling scheduleTask().
     * This method is called after the bean's properties have been set.
     */
    @PostConstruct
    public void init() {
        scheduleTask();
    }

}