package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import com.mgmtp.cfu.dto.coursedto.CourseResponse;
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.entity.Course;
import com.mgmtp.cfu.enums.*;
import com.mgmtp.cfu.entity.RegistrationFeedback;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.enums.*;
import com.mgmtp.cfu.entity.RegistrationFeedback;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.exception.*;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;
import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.*;
import com.mgmtp.cfu.repository.*;
import com.mgmtp.cfu.util.NotificationUtil;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import com.mgmtp.cfu.util.RegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.List;



import java.time.LocalDate;
import java.time.LocalDateTime;


import static com.mgmtp.cfu.util.AuthUtils.getCurrentUser;
import static com.mgmtp.cfu.enums.NotificationType.INFORMATION;
import static com.mgmtp.cfu.enums.RegistrationStatus.*;
import static com.mgmtp.cfu.util.Constant.APPROVE_REGISTRATION_EMAIL_TEMPLATE;
import static com.mgmtp.cfu.util.Constant.DECLINE_REGISTRATION_EMAIL_TEMPLATE;
import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getRegistrationOverviewDTOS;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MapperFactory<Registration> registrationMapperFactory;
    private final RegistrationOverviewMapper registrationOverviewMapper;
    private final CourseRepository courseRepository;
    private final NotificationService notificationService;
    private final RegistrationFeedbackService feedbackService;
    private final IEmailService emailService;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final RegistrationFeedbackRepository registrationFeedbackRepository;
    private final DocumentServiceImpl documentService;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   MapperFactory<Registration> registrationMapperFactory,
                                   RegistrationOverviewMapper registrationOverviewMapper,
                                   CourseRepository courseRepository,
                                   NotificationRepository notificationRepository,
                                   RegistrationFeedbackRepository registrationFeedbackRepository,
                                   IEmailService emailService, UserRepository userRepository,
                                   NotificationService notificationService,
                                   RegistrationFeedbackService feedbackService,
                                   CourseService courseService, DocumentServiceImpl documentService) {
        this.registrationRepository = registrationRepository;
        this.registrationMapperFactory = registrationMapperFactory;
        this.registrationOverviewMapper = registrationOverviewMapper;
        this.courseRepository = courseRepository;
        this.notificationService = notificationService;
        this.feedbackService = feedbackService;
        this.emailService = emailService;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.registrationFeedbackRepository=registrationFeedbackRepository;
        this.documentService = documentService;
    }

    @Value("${course4u.vite.frontend.url}")
    private String clientUrl;

    @Override
    public RegistrationDetailDTO getDetailRegistration(Long id) {
        Optional<DTOMapper<RegistrationDetailDTO, Registration>> registrationDtoMapperOpt = registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class);

        if (registrationDtoMapperOpt.isEmpty()) {
            throw new MapperNotFoundException("No mapper found for registrationDtoMapperOpt");
        }
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));
        return registrationDtoMapperOpt.get().toDTO(registration);
    }

    @Override
    public PageResponse getMyRegistrationPage(int page, String status) {
        status = status.trim();
        var userId = getCurrentUser().getId();

        var myRegistrations = registrationRepository.getSortedRegistrations(userId);
        myRegistrations=myRegistrations!=null?myRegistrations:new ArrayList<>();
        myRegistrations.forEach(registration -> log.info(registration.getLastUpdated()!=null? (String) registration.getLastUpdated().toString() :"null"));
        if (!RegistrationValidator.isDefaultStatus(status)) {
            try {
                var statusEnum = RegistrationStatus.valueOf(status.toUpperCase());
                myRegistrations = myRegistrations.stream().filter(registration -> registration.getStatus().equals(statusEnum)).toList();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        if (myRegistrations == null || myRegistrations.isEmpty()) {
            return PageResponse.builder().totalElements(0).list(new ArrayList<>()).build();
        }
        var listOfMyRegistration = getRegistrationOverviewDTOS(page, myRegistrations, registrationMapperFactory.getDTOMapper(RegistrationOverviewDTO.class));
        return PageResponse.builder().list(listOfMyRegistration).totalElements(myRegistrations.size()).build();
    }

    @Override
    public void approveRegistration(Long id) {
        var registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));

        // check if registration has a submitted status
        if (registration.getStatus() != RegistrationStatus.SUBMITTED) {
            throw new BadRequestRuntimeException("Registration must be in submitted status to be approved");
        }

        // check if course with the same link already exists and available
        if(courseRepository.findFirstByLinkIgnoreCaseAndStatus(registration.getCourse().getLink(), CourseStatus.AVAILABLE).isPresent() && !registration.getCourse().getStatus().equals(CourseStatus.AVAILABLE)){
            throw new DuplicateCourseException("Course with link " + registration.getCourse().getLink() + " already exists and available");
        }
        registration.getCourse().setStatus(CourseStatus.AVAILABLE);

        // change status category to available
        for (var category : registration.getCourse().getCategories()) {
            if (category.getStatus() == CategoryStatus.PENDING) {
                category.setStatus(CategoryStatus.AVAILABLE);
            }
        }

        // Update registration
        registration.setLastUpdated(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.APPROVED);
        registrationRepository.save(registration);

        // send notification
        notificationService.sendNotificationToUser(registration.getUser(), NotificationType.SUCCESS, "Your registration for course " + registration.getCourse().getName() + " has been approved");

        // send email
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("user_greeting").content("Your registration of course : " + registration.getCourse().getName() + " has been approved!").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl + "/personal/registration").tag("a").build()
        );
        emailService.sendMessage(registration.getUser().getEmail(), "Registration approved!!", "approve_registration_mail_template.xml", mailContentUnits);
    }

    @Override
    public void declineRegistration(Long id, FeedbackRequest feedbackRequest) {
        var registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));
        // check if registration has been declined
        if (registration.getStatus() != RegistrationStatus.SUBMITTED) {
            throw new BadRequestRuntimeException("Registration must be in submitted status to be declined");
        }
        // send feedback
        if (feedbackRequest == null || feedbackRequest.getComment() == null || feedbackRequest.getComment().isBlank())
            throw new BadRequestRuntimeException("Feedback comment is required");
        feedbackService.sendFeedback(registration, feedbackRequest.getComment().trim());

        // Update registration
        registration.setStatus(RegistrationStatus.DECLINED);
        registration.setLastUpdated(LocalDateTime.now());
        registrationRepository.save(registration);

        // send notification
        notificationService.sendNotificationToUser(registration.getUser(), NotificationType.ERROR, "Your registration for course " + registration.getCourse().getName() + " has been declined");

        // send email
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("user_greeting").content("Your registration of course : " + registration.getCourse().getName() + " has been declined!").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl + "/personal/registration").tag("a").build()
        );
        emailService.sendMessage(registration.getUser().getEmail(), "Registration declined!!", "decline_registration_mail_template.xml", mailContentUnits);
    }


    @Transactional
    public Boolean createRegistration(RegistrationRequest registrationRequest) {
        // Create course if needed
        var modelMapper = new ModelMapper();
        CourseRequest courseRequest = CourseRequest.builder().name(registrationRequest.getName())
                .link(registrationRequest.getLink())
                .platform(registrationRequest.getPlatform())
                .thumbnailFile(registrationRequest.getThumbnailFile())
                .thumbnailUrl(registrationRequest.getThumbnailUrl())
                .teacherName(registrationRequest.getTeacherName())
                .categories(registrationRequest.getCategories())
                .level(registrationRequest.getLevel())
                .build();
        CourseResponse course = courseService.createCourse(courseRequest);
        Registration registration = Registration.builder()
                .course(modelMapper.map(course, Course.class))
                .status(RegistrationStatus.SUBMITTED)
                .registerDate(LocalDate.now())
                .duration(registrationRequest.getDuration())
                .durationUnit(registrationRequest.getDurationUnit())
                .lastUpdated(LocalDateTime.now())
                .user(getCurrentUser())
                .build();
        registrationRepository.save(registration);
        return true;
    }

    // Admin Registration Services
    /**
     * Get optional registrations by configuration of Admin
     *
     * @param params: parameters for searching, filtering and sorting
     * @param page: page number
     * @return Page<RegistrationOverviewDTO>
     */
    @Override
    public Page<RegistrationOverviewDTO> getRegistrations(RegistrationOverviewParams params, int page) {
        String status = params.getStatus();
        String search = params.getSearch();
        String orderBy = (params.getOrderBy().isEmpty()) ? "id" : params.getOrderBy();
        Boolean isAscending = params.getIsAscending();

        Page<Registration> registrations;
        PageRequest pageRequest = (
                isAscending
                        ? PageRequest.of(page - 1, 8, Sort.by(orderBy).ascending())
                        : PageRequest.of(page - 1, 8, Sort.by(orderBy).descending())
        );

        if (status.isEmpty() || status.equalsIgnoreCase("all")) {
            registrations = registrationRepository.getOptionalRegistrationsWithoutStatus(search, pageRequest);
        } else {
            RegistrationStatus mappedStatus = RegistrationStatus.valueOf(status.toUpperCase());
            registrations = registrationRepository.getOptionalRegistrationsWithStatus(mappedStatus, search, pageRequest);
        }

        return registrations.map(registrationOverviewMapper::toDTO);
    }

    @Override
    public void calculateScore(Long id) {

        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration with id " + id + " not found"));

        if (registration.getStatus().equals(RegistrationStatus.APPROVED)) {

            LocalDateTime startDate = registration.getStartDate();
            LocalDateTime endDate = LocalDateTime.now();
            Integer estimatedDuration = registration.getDuration();
            DurationUnit durationUnit = registration.getDurationUnit(); // Day || Week || Month
            CourseLevel level = registration.getCourse().getLevel();

            // Actual study hours
            Duration duration = Duration.between(startDate, endDate);
            long actualDuration = duration.toHours();

            long estimatedHour;
            if (durationUnit.equals(DurationUnit.DAY))
                estimatedHour = estimatedDuration * 24;
            else if (durationUnit.equals(DurationUnit.WEEK))
                estimatedHour = estimatedDuration * 168;
            else
                estimatedHour = estimatedDuration * 720;

            long bonusPoints = Math.max(0, estimatedHour - actualDuration);

            long score;
            if (level.equals(CourseLevel.BEGINNER))
                score = actualDuration + (bonusPoints * 2);
            else if (level.equals(CourseLevel.INTERMEDIATE))
                score = 2 * actualDuration + (bonusPoints * 2);
            else
                score = 3 * actualDuration + (bonusPoints * 2);

            registration.setStatus(RegistrationStatus.DONE);
            registration.setEndDate(endDate);
            registration.setScore((int) score);
            registration.setLastUpdated(LocalDateTime.now());

            registrationRepository.save(registration);
        } else
            throw new InvalidRegistrationStatusException("The status of the Registration is not APPROVED");
    }

    @Override
    @Transactional
    public void closeRegistration(Long id, FeedbackRequest feedbackRequest) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));


        if (!RegistrationStatusUtil.isCloseableStatus(registration.getStatus())) {
            throw new BadRequestRuntimeException("Registration status must be in [DONE, VERIFYING, DOCUMENT_DECLINED, VERIFIED]  to be closed");
        }

        // If the registration is not verified, send feedback
        if (registration.getStatus() != RegistrationStatus.VERIFIED) {
            if (feedbackRequest == null || feedbackRequest.getComment() == null || feedbackRequest.getComment().isBlank())
                throw new BadRequestRuntimeException("Feedback comment is required");
            feedbackService.sendFeedback(registration, feedbackRequest.getComment().trim());
        }

        registration.setStatus(RegistrationStatus.CLOSED);
        registration.setLastUpdated(LocalDateTime.now());
        registrationRepository.save(registration);

        // send notification
        notificationService.sendNotificationToUser(registration.getUser(), NotificationType.INFORMATION, "Your registration for course " + registration.getCourse().getName() + " has been closed");

        // send email
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("user_greeting").content("Your registration of course : " + registration.getCourse().getName() + " has been closed!").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl + "/personal/registration").tag("a").build()
        );
        emailService.sendMessage(registration.getUser().getEmail(), "Registration closed", "close_registration_mail_template.xml", mailContentUnits);
    }

    @Override
    public void deleteRegistration(Long id) {
        var registration = registrationRepository.findById(id)
                .orElseThrow(() -> new BadRequestRuntimeException("Registration not found"));

        if (!Objects.equals(getCurrentUser().getId(), registration.getUser().getId())) {
            throw new ForbiddenException("You do not have permission to delete this registration.");
        }

        if (registration.getStatus() != RegistrationStatus.DRAFT && registration.getStatus() != RegistrationStatus.DISCARDED) {
            throw new BadRequestRuntimeException("Registration must be in DRAFT or DISCARDED status to be deleted.");
        }

        registrationRepository.delete(registration);
    }

    @Override
    public void discardRegistration(Long id) {
        var registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));

        if (!RegistrationStatusUtil.isDiscardableStatus(registration.getStatus())) {
            throw new BadRequestRuntimeException("Registration must be in correct status to be discarded.");
        }
        registration.setLastUpdated(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.DISCARDED);
        registrationRepository.save(registration);
    }

    @Override
    public boolean startLearningCourse(Long registrationId) {
        var userId = getCurrentUser().getId();
        if (!registrationRepository.existsByIdAndUserId(registrationId, userId))
            throw new BadRequestRuntimeException("Not found any registration that id is "+registrationId);
        var registrationOpt = registrationRepository.findById(registrationId);
        if (registrationOpt.isPresent()) {
            var registration=registrationOpt.get();
            if(Objects.nonNull(registration.getStartDate()))
                throw new ConflictRuntimeException("This course was started learning");
            if (!registration.getStatus().equals(RegistrationStatus.APPROVED)) {
                throw new BadRequestRuntimeException("This registration requires approval by admin.");
            }
            registration.setStartDate(LocalDateTime.now());
            registration.setLastUpdated(LocalDateTime.now());
            registrationRepository.save(registration);
            return true;
        }
        return false;
    }



    @Override
    @Transactional(rollbackOn = Exception.class)
    public void verifyRegistration(Long id, Map<String, String> longDocumentStatusMap, String status) {
        // Fetch the registration by ID or throw an error if not found
        var registration = registrationRepository.findById(id)
                .orElseThrow(() -> new BadRequestRuntimeException("Error: Registration not found."));

        // Check if the registration status is VERIFYING
        if (registration.getStatus() == null || registration.getStatus() != RegistrationStatus.VERIFYING) {
            throw new BadRequestRuntimeException("Error: Registration must be in VERIFYING status.");
        }

        RegistrationStatus registrationStatus;
        try {
            // Convert status to RegistrationStatus enum
            registrationStatus = RegistrationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestRuntimeException("Error: Invalid status. Must be 'VERIFIED' or 'DOCUMENT_DECLINED'.");
        }

        // Get feedback and remove from map
        String feedback = longDocumentStatusMap.getOrDefault("feedbackRequest", "");
        longDocumentStatusMap.remove("feedbackRequest");

        int numberOfApprovedCertificateDocument = 0;
        int numberOfApprovedPaymentDocument = 0;

        try {
            // Process each document status entry
            for (Map.Entry<String, String> documentStatusMap : longDocumentStatusMap.entrySet()) {
                long documentId;
                try {
                     documentId = Long.parseLong(documentStatusMap.getKey());
                }catch (Exception e){
                    continue;
                }
                var documentStatus = DocumentStatus.valueOf(documentStatusMap.getValue().toUpperCase());
                var document = documentService.verifyDocument(documentId, documentStatus);

                // Count approved documents by type
                if (documentStatus == DocumentStatus.APPROVED) {
                    if (document.getType() == DocumentType.CERTIFICATE) {
                        numberOfApprovedCertificateDocument++;
                    }
                    if (document.getType() == DocumentType.PAYMENT) {
                        numberOfApprovedPaymentDocument++;
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestRuntimeException("Error: Failed to process documents. " + e.getMessage());
        }

        // Validate documents and set registration status
        if (registrationStatus == RegistrationStatus.VERIFIED) {
            if (numberOfApprovedCertificateDocument <= 0 || numberOfApprovedPaymentDocument <= 0) {
                throw new BadRequestRuntimeException("Error: At least one approved payment and certificate document are required.");
            }
            registration.setStatus(RegistrationStatus.VERIFIED);
        } else if (registrationStatus == RegistrationStatus.DOCUMENT_DECLINED) {
            registration.setStatus(RegistrationStatus.DOCUMENT_DECLINED);
            if (feedback.isEmpty()) {
                throw new BadRequestRuntimeException("Error: Feedback cannot be null or empty.");
            }
            var registrationFeedback = RegistrationFeedback.builder()
                    .comment(feedback)
                    .createdDate(LocalDateTime.now())
                    .user(getCurrentUser())
                    .registration(registration)
                    .build();
            registrationFeedbackRepository.save(registrationFeedback);
        } else {
            throw new BadRequestRuntimeException("Error: Status must be 'VERIFIED' or 'DOCUMENT_DECLINED'.");
        }

        // Notify about the verified documents and save the registration
        notifyVerifiedDocument(registration, feedback);
        registration.setLastUpdated(LocalDateTime.now());
        registrationRepository.save(registration);
    }

    private void notifyVerifiedDocument(Registration registration, String feedback) {
        var accountant = getCurrentUser();

        if (registration.getStatus().equals(RegistrationStatus.VERIFIED)) {
            var admins = userRepository.findAllByRole(Role.ADMIN);
            var user = registration.getUser();

            // Notify admins about the verification
            admins.forEach(admin -> {
                String courseName = registration.getCourse().getName();
                String message = String.format(
                        "The course titled '%s' that user %s registered has been successfully verified by our accountant %s.",
                        courseName, user.getUsername(), accountant.getUsername()
                );
                notificationRepository.save(NotificationUtil.createNotification(NotificationType.INFORMATION, admin, message));
                sendEmail(registration, admin, message);
            });

            // Notify the user about the verification
            String userMessage = "Congratulations! Your course titled " + registration.getCourse().getName() + " has been verified successfully.";
            sendEmail(registration, user, userMessage);
            log.info(userMessage);

        } else if (registration.getStatus().equals(RegistrationStatus.DOCUMENT_DECLINED)) {
            log.info("DOCUMENT_DECLINED");
            String message = "We regret to inform you that your submitted documents for the course " +
                    registration.getCourse().getName() + " have been rejected. " + feedback;
            notificationRepository.save(NotificationUtil.createNotification(NotificationType.INFORMATION, registration.getUser(), message));
            sendEmail(registration, registration.getUser(), message);
            log.info(message);
        }
    }

    private void sendEmail(Registration registration, User user, String message) {
        var status = registration.getStatus();

        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("user_greeting").content("Dear " + user.getUsername()).tag("div").build(),
                MailContentUnit.builder().id("content").content(message).tag("div").build(),
                MailContentUnit.builder().id("title").content(status == RegistrationStatus.DOCUMENT_DECLINED ? "Document Decline" : "Document Approval").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl).content("LOGIN NOW").tag("a").build()
        );

        emailService.sendMessage(
                registration.getUser().getEmail(),
                status == RegistrationStatus.DOCUMENT_DECLINED ? "Document Decline" : "Document Approval",
                status == RegistrationStatus.VERIFIED ? APPROVE_REGISTRATION_EMAIL_TEMPLATE : DECLINE_REGISTRATION_EMAIL_TEMPLATE,
                mailContentUnits
        );
    }






    @Override
    public void createRegistrationFromExistingCourses(Long courseId, RegistrationEnrollDTO registrationEnrollDTO) {
        if (registrationEnrollDTO.getDuration() == null || registrationEnrollDTO.getDurationUnit() == null) {
            throw new BadRequestRuntimeException("Duration and Duration Unit must not be null");
        }

        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Registration registration = Registration.builder()
                .course(course)
                .status(RegistrationStatus.SUBMITTED)
                .registerDate(LocalDate.now())
                .duration(registrationEnrollDTO.getDuration().intValue())
                .durationUnit(registrationEnrollDTO.getDurationUnit())
                .lastUpdated(LocalDateTime.now())
                .user(getCurrentUser())
                .build();
        registrationRepository.save(registration);
    }
}