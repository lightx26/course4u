package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.coursedto.CourseRegistrationDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.exception.ConflictRuntimeException
import com.mgmtp.cfu.dto.MailContentUnit
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.enums.CategoryStatus
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.enums.NotificationType
import com.mgmtp.cfu.exception.ForbiddenException
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.mapper.RegistrationDetailMapper
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper

import com.mgmtp.cfu.mapper.factory.MapperFactory

import com.mgmtp.cfu.dto.RegistrationRequest
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.DurationUnit
import com.mgmtp.cfu.enums.RegistrationStatus

import com.mgmtp.cfu.repository.CourseRepository
import com.mgmtp.cfu.repository.NotificationRepository
import com.mgmtp.cfu.repository.RegistrationFeedbackRepository
import com.mgmtp.cfu.repository.RegistrationRepository

import com.mgmtp.cfu.service.IEmailService
import com.mgmtp.cfu.service.NotificationService;
import com.mgmtp.cfu.service.RegistrationFeedbackService;
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime


class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock()
    MapperFactory<Registration> registrationMapperFactory = Mock()
    RegistrationOverviewMapper registrationOverviewMapper = Mock()
    CourseRepository courseRepository = Mock()
    NotificationService notificationService = Mock()
    RegistrationFeedbackService feedbackService = Mock()
    IEmailService emailService = Mock()
    RegistrationDetailMapper registrationDetailMapper = Mock()
    CourseService courseService = Mock()

    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(
            registrationRepository, registrationMapperFactory, registrationOverviewMapper,
            courseRepository, notificationService, feedbackService,
            emailService, courseService
    );

    def "return registration details successfully"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().id(id).build()
        RegistrationDetailDTO registrationDetailDTO = RegistrationDetailDTO.builder().id(id).build()

        registrationRepository.findById(id) >> Optional.of(registration)
        registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.of(registrationDetailMapper)
        registrationDetailMapper.toDTO(registration) >> registrationDetailDTO

        when:
        RegistrationDetailDTO result = registrationService.getDetailRegistration(id)

        then:
        result.id == registrationDetailDTO.id
    }

    def "return registration details failed"() {
        given:
        Long id = 999L
        registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.of(registrationDetailMapper)
        registrationRepository.findById(id) >> Optional.empty()

        when:
        registrationService.getDetailRegistration(id)

        then:
        def ex = thrown(RegistrationNotFoundException)
        ex.message == "Registration not found"
    }

    def "should return not found registration mapper"() {
        given:
        Long id = 1L
        registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.empty()

        when:
        registrationService.getDetailRegistration(1L)

        then:
        def ex = thrown(MapperNotFoundException)
        ex.message == "No mapper found for registrationDtoMapperOpt"
    }

    def "test getMyRegistrationPage with default status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "DEFAULT"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
        when:
        def result = registrationService.getMyRegistrationPage(1, status)
        then:
        result.list.size() == 1
        result.totalElements == 1
    }

    def "test getMyRegistrationPage with specific status"() {
        given:
        def userId = 1
        def status = "APPROVED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations,registrayion2)
        registrationMapperFactory.getDTOMapper(_)>>Optional.of(registrationOverviewMapper)
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        when:
        def result = registrationService.getMyRegistrationPage(1, status)

        then:
        result.list.size() == 2
        result.list[0].status == RegistrationStatus.APPROVED
        result.totalElements == 2
    }

    def "test getMyRegistrationPage with invalid status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "APPROVaaaED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)

        when:
        registrationService.getMyRegistrationPage(0, status)

        then:
        thrown(IllegalArgumentException)
    }

    def "test getMyRegistrationPage with default status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "DEFAULT"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
        when:
        def result = registrationService.getMyRegistrationPage(1, status)
        then:
        result.list.size() == 1
        result.totalElements == 1
    }

    def "test getMyRegistrationPage with specific status"() {
        given:
        def userId = 1
        def status = "APPROVED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations,registrayion2)
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        when:
        def result = registrationService.getMyRegistrationPage(1, status)

        then:
        result.list.size() == 2
        result.list[0].status == RegistrationStatus.APPROVED
        result.totalElements == 2
    }

    def "test getMyRegistrationPage with invalid status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "APPROVaaaED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)

        when:
        registrationService.getMyRegistrationPage(0, status)

        then:
        thrown(IllegalArgumentException)
    }

    def "startLearningCourse: Not found any registration"(){
        given:
        registrationRepository.existsByIdAndUserId(_ as Long,_ as  Long)>>false
        when:
        registrationService.startLearningCourse(1)
        then:
        def e=thrown(BadRequestRuntimeException)
    }

    def "startLearningCourse: This course was started learning"(){
        given:
        registrationRepository.existsByIdAndUserId(_ as Long,_ as  Long)>>true
        registrationRepository.findById(_ as Long)>> Optional.of(Registration.builder().id(1).startDate(LocalDateTime.now()).build());
        when:
        registrationService.startLearningCourse(1)
        then:
        def e=thrown(ConflictRuntimeException)
    }
    def "startLearningCourse: return true"(){
        given:
        registrationRepository.existsByIdAndUserId(_ as Long,_ as  Long)>>true
        registrationRepository.findById(_ as Long)>> Optional.of(Registration.builder().id(1).status(RegistrationStatus.APPROVED).startDate(null).build());
        when:
        def result =registrationService.startLearningCourse(1)
        then:
        result
    }
    def "startLearningCourse: This registration requires approval by admin."(){
        given:
        registrationRepository.existsByIdAndUserId(_ as Long,_ as  Long)>>true
        registrationRepository.findById(_ as Long)>> Optional.of(Registration.builder().id(1).status(RegistrationStatus.SUBMITTED).startDate(null).build());
        when:
        registrationService.startLearningCourse(1)
        then:
        def e=thrown(BadRequestRuntimeException)
    }


    def "startLearningCourse: return false"(){
        given:
        registrationRepository.existsByIdAndUserId(_ as Long,_ as  Long)>>true
        registrationRepository.findById(_ as Long)>> Optional.empty()
        when:
        def result=registrationService.startLearningCourse(1)
        then:
        !result

    }

    /*
     * Test cases for getAllRegistrations and getRegistrationByStatus
     */
    def "getAllRegistrations should return registration list"() {
        given:
            def page = 1
            def registration = Registration.builder().id(1).build()
            def registration2 = Registration.builder().id(2).build()
            Page<Registration> registrations = Mock(){
                getContent() >> [registration, registration2]
            }
            registrationRepository.findAllExceptStatus(_, _) >> {args -> {
                def input = args[1] as PageRequest
                assert input.pageNumber == page - 1
                assert input.pageSize == 8

                registrations
            }}
            registrationOverviewMapper.toDTO(registration) >> RegistrationOverviewDTO.builder().id(1).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getAllRegistrations(page)

        then:
            result.size() == 2
            result[0].id == 1
            result[1].id == 2
    }

    def "getAllRegistration should return a registration list without DRAFT registrations"(){
        given:
            def page = 1
            def registration = Registration.builder().id(1).status(RegistrationStatus.APPROVED).build()
            def registration2 = Registration.builder().id(2).status(RegistrationStatus.DRAFT).build()

            Page<Registration> registrations = Mock(){
                getContent() >> [registration]
            }

            registrationRepository.findAllExceptStatus(_, _) >>{
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).status(RegistrationStatus.DRAFT).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getAllRegistrations(page)

        then:
            result.size() == 1
            result[0].id == 1
    }

    def "getRegistrationByStatus should return the correct registration list"(){
        given:
            def page = 1
            def status = "APPROVED"

            def registration = Registration.builder().id(1).status(RegistrationStatus.APPROVED).build()
            def registration2 = Registration.builder().id(2).status(RegistrationStatus.APPROVED).build()
            Page<Registration> registrations = Mock(){
                getContent() >> [registration, registration2]
            }

            registrationRepository.findAllByStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest

                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).status(RegistrationStatus.APPROVED).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getRegistrationByStatus(page, status)

        then:
            result.size() == 2
            result[0].id == 1
            result[1].id == 2
    }

    def "getRegistrationByStatus with unavailable status should return an error message"(){
        given:
        def page = 1
        def status = "NOT_EXISTING_STATUS"

        when:
        registrationService.getRegistrationByStatus(page, status)

        then:
        def ex = thrown(RegistrationStatusNotFoundException)
        ex.message == "Status not found"
    }

    def "should approve registration when status is submitted and no duplicate course exists"() {
        given:
        Long registrationId = 1L
        User user = new User(email: "test@example.com")
        Course course = new Course(name: "Course Name", link: "course-link", categories: [new Category(status: CategoryStatus.PENDING)])
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED, course: course, user: user)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCase(course.link) >> null

        when:
        registrationService.approveRegistration(registrationId)

        then:
        1 * emailService.sendMessage(user.email, "Registration approved!!", "approve_registration_mail_template.xml", _)
        1 * notificationService.sendNotificationToUser(user, NotificationType.SUCCESS, _)
        registration.status == RegistrationStatus.APPROVED
        registration.course.status == CourseStatus.AVAILABLE
        registration.course.categories.every { it.status == CategoryStatus.AVAILABLE }
    }

    def "should throw exception if registration status is not submitted"() {
        given:
        Long registrationId = 1L
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.CLOSED)

        registrationRepository.findById(registrationId) >> Optional.of(registration)

        when:
        registrationService.approveRegistration(registrationId)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Registration must be in submitted status to be approved"
        0 * courseRepository.findFirstByLinkIgnoreCase(_)
        0 * notificationService.sendNotificationToUser(_, _, _)
        0 * registrationRepository.save(_)
        0 * emailService.sendMessage(_, _, _, _)
    }

    def "should approve registration and handle duplicate course"() {
        given:
        Long registrationId = 1L
        User user = new User(email: "test@example.com")
        Course course = new Course(name: "Course Name", link: "course-link", categories: [new Category(status: CategoryStatus.PENDING)])
        Course duplicateCourse = new Course(link: "course-link")
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED, course: course, user: user)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCase(course.link) >> duplicateCourse

        when:
        registrationService.approveRegistration(registrationId)

        then:
        1 * courseRepository.findFirstByLinkIgnoreCase(course.link)
        1 * notificationService.sendNotificationToUser(user, NotificationType.SUCCESS, _)
        1 * registrationRepository.save(_)
        1 * emailService.sendMessage(user.email, "Registration approved!!", "approve_registration_mail_template.xml", _)
        registration.status == RegistrationStatus.APPROVED
        registration.course.status == CourseStatus.AVAILABLE
        registration.course.categories.every { it.status == CategoryStatus.AVAILABLE }
    }

    def "declineRegistration should decline a registration"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        def registration = new Registration(id: 1L, status: RegistrationStatus.SUBMITTED, course: new Course(name: "Course 101"), user: new User(email: "user@example.com"))
        def user = new User(id: 1L)
        registrationRepository.findById(1L) >> Optional.of(registration)
        registrationRepository.save(_ as Registration) >> { Registration reg -> reg }
        when:

        registrationService.declineRegistration(1,feedbackRequest)

        then:
        1 * feedbackService.sendFeedback(registration, feedbackRequest.getComment())
        1 * notificationService.sendNotificationToUser(registration.user, NotificationType.ERROR, "Your registration for course Course 101 has been declined")
        1 * registrationRepository.save(_ as Registration) >> { Registration reg ->
            assert reg.status == RegistrationStatus.DECLINED
            assert reg.lastUpdated != null
        }
        1 * emailService.sendMessage("user@example.com", "Registration declined!!", "decline_registration_mail_template.xml", _ as List<MailContentUnit>) >> { String email, String subject, String template, List<MailContentUnit> units ->
            assert units[0].content.contains("Your registration of course : Course 101 has been declined!")
            assert units[1].href.contains("/personal/registration")
        }
    }

    def "declineRegistration should throw RegistrationNotFoundException if registration is not found"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")

        when:
        registrationRepository.findById(1L) >> Optional.empty()
        registrationService.declineRegistration(1,feedbackRequest)

        then:
        thrown(RegistrationNotFoundException)
        0 * _
    }

    def "declineRegistration should throw IllegalArgumentException if registration status is not SUBMITTED"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        def registration = new Registration(id: 1L, status: RegistrationStatus.APPROVED)

        when:
        registrationRepository.findById(1L) >> Optional.of(registration)
        registrationService.declineRegistration(1,feedbackRequest)

        then:
        thrown(IllegalArgumentException)
        0 * _
    }

    User currentUser
    def setup() {
        // Mocking SecurityContext and Authentication
        SecurityContext securityContext = Mock(SecurityContext)
        Authentication authentication = Mock(Authentication)

        // Setting up the current user
        currentUser = new User(id: 1L, username: "testuser")

        // Setting up the SecurityContextHolder
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        authentication.getCredentials() >> currentUser

        // Mocking AuthUtils to return the current user
        GroovyMock(AuthUtils, global: true)
        AuthUtils.getCurrentUser() >> currentUser
    }

    def "createRegistration should create and save a new registration"() {
        given:
        def registrationRequest = new RegistrationRequest()
        registrationRequest.setName("name")
        registrationRequest.setLink("https://google.com")
        registrationRequest.setPlatform(CoursePlatform.COURSERA)
        registrationRequest.setThumbnailFile(null)
        registrationRequest.setThumbnailUrl("thumbnailUrl")
        registrationRequest.setTeacherName("teacherName")
        registrationRequest.setCategories([])
        registrationRequest.setLevel(CourseLevel.INTERMEDIATE)
        registrationRequest.setDuration(10)
        registrationRequest.setDurationUnit(DurationUnit.DAY)

        def courseResponse = new CourseResponse()
        def course = new Course()

        def registration = new Registration()
        registration.setCourse(course)
        registration.setStatus(RegistrationStatus.SUBMITTED)
        registration.setRegisterDate(LocalDate.now())
        registration.setDuration(10)
        registration.setDurationUnit(DurationUnit.DAY)
        registration.setLastUpdated(LocalDateTime.now())
        registration.setUser(currentUser)

        and:
        1 * courseService.createCourse(_) >> courseResponse
        1 * registrationRepository.save(_) >> registration

        when:
        def result = registrationService.createRegistration(registrationRequest)

        then:
        result == true
    }

    def "Calculate score and update Registration"() {
        given:
        Long id = 1
        Course course = Course.builder().level(CourseLevel.ADVANCED).build()

        Registration registration = Registration.builder()
                                                .id(1)
                                                .status(RegistrationStatus.APPROVED)
                                                .startDate(LocalDateTime.of(2024, 7, 22, 18, 0, 0))
                                                .score(1008)
                                                .course(course)
                                                .duration(1)
                                                .durationUnit(DurationUnit.DAY)
                                                .build()

        registrationRepository.findById(_) >> Optional.of(registration)

        when:
        registrationService.calculateScore(id)

        then:
        1 * registrationRepository.save(_)
    }

    def "should close the registration without sending feedback"() {
        given:
        def user = new User(id: 1L, username: "User", email: "user@mgm-tp.com")
        def registration = new Registration(id: 1L, status: RegistrationStatus.VERIFIED, course: new Course(name: "Course 101"), user: user)

        registrationRepository.findById(1L) >> Optional.of(registration)

        when:
        registrationService.closeRegistration(1L, new FeedbackRequest())

        then:
        0 * feedbackService.sendFeedback(registration, _)
        1 * registrationRepository.save(registration) >> { Registration reg ->
            assert reg.status == RegistrationStatus.CLOSED
            assert reg.lastUpdated != null
        }
        1 * notificationService.sendNotificationToUser(user, NotificationType.INFORMATION, "Your registration for course Course 101 has been closed")
        1 * emailService.sendMessage(user.getEmail(), "Registration closed", _, _)
    }

    def "should close the registration with feedback"() {
        given:
        def user = new User(id: 1L, username: "User", email: "user@mgm-tp.com")
        def registration = new Registration(id: 1L, status: RegistrationStatus.DONE, course: new Course(name: "Course 101"), user: user)
        def feedback = new FeedbackRequest(comment: "Not suitable")

        registrationRepository.findById(1L) >> Optional.of(registration)

        when:
        registrationService.closeRegistration(1L, feedback)

        then:
        1 * feedbackService.sendFeedback(registration, feedback.getComment())
        1 * registrationRepository.save(registration) >> { Registration reg ->
            assert reg.status == RegistrationStatus.CLOSED
            assert reg.lastUpdated != null
        }
        1 * notificationService.sendNotificationToUser(user, NotificationType.INFORMATION, "Your registration for course Course 101 has been closed")
        1 * emailService.sendMessage(user.getEmail(), "Registration closed", _, _)
    }

    def "should throw RegistrationNotFoundException if registration is not found"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        registrationRepository.findById(1L) >> Optional.empty()

        when:
        registrationService.closeRegistration(1L, feedbackRequest)

        then:
        thrown(RegistrationNotFoundException)
    }
    def "should throw BadRequestRunTimeException if registration is not found"() {
        given:
        Long id = 1L
        registrationRepository.findById(id)>>Optional.empty()

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should throw ForbiddenException if user is not the owner of the registration"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().user(User.builder().id(2).build()).build()
        registrationRepository.findById(id)>>Optional.of(registration)

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(ForbiddenException)
    }

    def "should throw BadRequestRunTimeException if registration status is not DRAFT or DISCARDED"() {
        given:
        Registration registration = Registration.builder().user(User.builder().id(1).build()).build()
        Long id = 1L
       registrationRepository.findById(id)>>Optional.of(registration)

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should delete registration if all conditions are met"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().status(RegistrationStatus.DISCARDED).user(User.builder().id(1).build()).build()
        registrationRepository.findById(id)>>Optional.of(registration)

        when:
        registrationService.deleteRegistration(id)

        then:
        1 * registrationRepository.delete(registration)
    }

    def "should throw BadRequestRunTimeException if registration status is not closeable"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        def registration = new Registration(id: 1L, status: RegistrationStatus.APPROVED)
        registrationRepository.findById(1L) >> Optional.of(registration)

        when:
        registrationService.closeRegistration(1L, feedbackRequest)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should throw RegistrationNotFoundException if discard, registration is not found"() {
        given:
            def registrationId = 1L
            registrationRepository.findById(registrationId) >> Optional.empty()

        when:
            registrationService.discardRegistration(registrationId)

        then:
            thrown(RegistrationNotFoundException)
    }

    def "should throw BadRequestRunTimeException if discard, registration status is not correct"() {
        given:
            def registrationId = 1L
            def registration = new Registration(id: registrationId, status: RegistrationStatus.CLOSED)
            registrationRepository.findById(registrationId) >> Optional.of(registration)

        when:
            registrationService.discardRegistration(registrationId)

        then:
            thrown(BadRequestRuntimeException)
    }
}
