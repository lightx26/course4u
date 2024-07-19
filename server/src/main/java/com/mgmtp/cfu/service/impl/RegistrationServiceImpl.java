package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.dto.PageResponse;
import com.mgmtp.cfu.dto.RegistrationRequest;
import com.mgmtp.cfu.dto.coursedto.CourseRequest;
import com.mgmtp.cfu.dto.coursedto.CourseResponse;
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO;
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest;

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO;
import com.mgmtp.cfu.entity.Course;

import com.mgmtp.cfu.entity.RegistrationFeedback;
import com.mgmtp.cfu.enums.CategoryStatus;
import com.mgmtp.cfu.enums.CourseStatus;
import com.mgmtp.cfu.enums.NotificationType;
import com.mgmtp.cfu.enums.RegistrationStatus;
import com.mgmtp.cfu.exception.MapperNotFoundException;
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException;
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper;

import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.exception.RegistrationNotFoundException;
import com.mgmtp.cfu.mapper.DTOMapper;
import com.mgmtp.cfu.mapper.factory.MapperFactory;

import com.mgmtp.cfu.repository.RegistrationRepository;
import com.mgmtp.cfu.service.CourseService;
import com.mgmtp.cfu.repository.*;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.service.RegistrationService;
import com.mgmtp.cfu.util.AuthUtils;
import com.mgmtp.cfu.util.NotificationUtil;
import com.mgmtp.cfu.util.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getRegistrationOverviewDTOS;
import static com.mgmtp.cfu.util.RegistrationOverviewUtils.getSortedRegistrations;

import java.util.ArrayList;
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final MapperFactory<Registration> registrationMapperFactory;
    private final RegistrationOverviewMapper registrationOverviewMapper;
    private final CourseRepository courseRepository;
    private final NotificationRepository notificationRepository;
    private final RegistrationFeedbackRepository registrationFeedbackRepository;
    private final IEmailService emailService;
    private final UserRepository userRepository;
    private final CourseService courseService;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   MapperFactory<Registration> registrationMapperFactory,
                                   RegistrationOverviewMapper registrationOverviewMapper,
                                   CourseRepository courseRepository,
                                   NotificationRepository notificationRepository,
                                   RegistrationFeedbackRepository registrationFeedbackRepository,
                                   IEmailService emailService, UserRepository userRepository,
                                   CourseService courseService) {
        this.registrationRepository = registrationRepository;
        this.registrationMapperFactory = registrationMapperFactory;
        this.registrationOverviewMapper = registrationOverviewMapper;
        this.courseRepository = courseRepository;
        this.notificationRepository = notificationRepository;
        this.registrationFeedbackRepository = registrationFeedbackRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.courseService = courseService;
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
        var userId = AuthUtils.getCurrentUser().getId();

        var myRegistrations = getSortedRegistrations(userId, registrationRepository);

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
        if(registration.getStatus() != RegistrationStatus.SUBMITTED){
            throw new IllegalArgumentException("Registration must be in submitted status to be approved");
        }

        // check duplicate course
        Optional<Course> duplicateCourse = Optional.ofNullable(courseRepository.findFirstByLinkIgnoreCase(registration.getCourse().getLink()));
        if (duplicateCourse.isEmpty()) {
            registration.getCourse().setStatus(CourseStatus.AVAILABLE);
        }

        // change status category to available
        for(var category : registration.getCourse().getCategories()){
            if(category.getStatus() == CategoryStatus.PENDING) {
                category.setStatus(CategoryStatus.AVAILABLE);
            }
        }

        // save notification
        var notification = NotificationUtil.createNotification(NotificationType.SUCCESS,
                registration.getUser(),
                "Your registration for course " + registration.getCourse().getName() + " has been approved");
        notificationRepository.save(notification);
        registration.setLastUpdated(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.APPROVED);
        registrationRepository.save(registration);

        List<MailContentUnit> mailContentUnits=List.of(
                MailContentUnit.builder().id("user_greeting").content("Your registration of course : "+ registration.getCourse().getName() +" has been approved!").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl+"/personal/registration").tag("a").build()
        );
        emailService.sendMessage(registration.getUser().getEmail(), "Registration approved!!","approve_registration_mail_template.xml", mailContentUnits);

    }

    @Override
    public void declineRegistration(Long id , FeedbackRequest feedbackRequest) {
        var registration = registrationRepository.findById(id).orElseThrow(() -> new RegistrationNotFoundException("Registration not found"));
        // check if registration has been declined
        if(registration.getStatus() != RegistrationStatus.SUBMITTED){
            throw new IllegalArgumentException("Registration must be in submitted status to be declined");
        }

        var feedbackUser = userRepository.findById(feedbackRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // save feedback
        var registrationFeedback = RegistrationFeedback.builder()
                .comment(feedbackRequest.getComment())
                .user(feedbackUser)
                .registration(registration)
                .createdDate(LocalDateTime.now())
                .build();
        registrationFeedbackRepository.save(registrationFeedback);

        // save notification
        var notification = NotificationUtil.createNotification(NotificationType.ERROR,
                registration.getUser(),
                "Your registration for course " + registration.getCourse().getName() + " has been declined");
        notificationRepository.save(notification);

        registration.setStatus(RegistrationStatus.DECLINED);
        registration.setLastUpdated(LocalDateTime.now());
        registrationRepository.save(registration);

        List<MailContentUnit> mailContentUnits=List.of(
                MailContentUnit.builder().id("user_greeting").content("Your registration of course : "+ registration.getCourse().getName() +" has been declined!").tag("div").build(),
                MailContentUnit.builder().id("client_url").href(clientUrl+"/personal/registration").tag("a").build()
        );
        emailService.sendMessage(registration.getUser().getEmail(), "Registration declined!!","decline_registration_mail_template.xml", mailContentUnits);
    }
    @Override
    public Page<RegistrationOverviewDTO> getAllRegistrations(int page) {
        List<RegistrationStatus> excludedStatuses = List.of(RegistrationStatus.DRAFT);
        PageRequest pageRequest = PageRequest.of(page - 1, 8);
        Page<Registration> registrations = registrationRepository.findAllExceptStatus(excludedStatuses, pageRequest);

        List<RegistrationOverviewDTO> modifiedResponseContent = registrations
                .getContent()
                .stream()
                .map(registrationOverviewMapper::toDTO)
                .toList();

        return new PageImpl<>(modifiedResponseContent, pageRequest, registrations.getTotalElements());
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
                .user(AuthUtils.getCurrentUser())
                .build();
        Registration savedRegistration = registrationRepository.save(registration);
        if (savedRegistration == null) {
            throw new RuntimeException("Cannot create registration");
        }
        else {
            return true;
        }
    }

    @Override
    public Page<RegistrationOverviewDTO> getRegistrationByStatus(int page, String status) {
        try{
            PageRequest pageRequest = PageRequest.of(page - 1, 8);
            RegistrationStatus registrationStatus = RegistrationStatus.valueOf(status.toUpperCase());
            Page<Registration> registrations = registrationRepository.findAllByStatus(registrationStatus, pageRequest);

            List<RegistrationOverviewDTO> modifiedResponseContent = registrations
                    .getContent()
                    .stream()
                    .map(registrationOverviewMapper::toDTO)
                    .toList();

            return new PageImpl<>(modifiedResponseContent, pageRequest, registrations.getTotalElements());
        }
        catch (IllegalArgumentException e){
            throw new RegistrationStatusNotFoundException("Status not found");
        }
    }
}
