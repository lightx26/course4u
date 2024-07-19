package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.MailContentUnit
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Notification
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.RegistrationFeedback
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CategoryStatus
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.NotificationType
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.mapper.RegistrationDetailMapper
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper
import com.mgmtp.cfu.mapper.UserMapper
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.RegistrationMapperFactory
import com.mgmtp.cfu.dto.coursedto.CourseRequest

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
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.service.IEmailService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.AuthUtils
import org.modelmapper.ModelMapper
import org.springframework.security.core.context.SecurityContext
import spock.lang.Specification
import spock.lang.Subject
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification;
import spock.lang.Subject;


import java.time.LocalDate
import java.time.LocalDateTime;

import java.time.LocalDateTime

class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock(RegistrationRepository)
    MapperFactory<Registration> registrationMapperFactory = Mock(MapperFactory)
    RegistrationOverviewMapper registrationOverviewMapper = Mock(RegistrationOverviewMapper)
    CourseRepository courseRepository = Mock(CourseRepository)
    NotificationRepository notificationRepository = Mock(NotificationRepository)
    UserMapper userMapper = Mock(UserMapper)
    RegistrationFeedbackRepository registrationFeedbackRepository = Mock(RegistrationFeedbackRepository)
    IEmailService emailService = Mock(IEmailService)
    UserRepository userRepository = Mock(UserRepository)
    def registrationDetailMapper = Mock(RegistrationDetailMapper)

    CourseService courseService = Mock()
    ModelMapper modelMapper = Mock()
    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(
            registrationRepository, registrationMapperFactory, registrationOverviewMapper,
            courseRepository, notificationRepository, userMapper,
            registrationFeedbackRepository, emailService,userRepository,courseService
    )

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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
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
                getContent() >> [registration];
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
        Notification notification = new Notification(content: "Your registration for course " + course.name + " has been approved", createdDate: LocalDateTime.now(), seen: false, user: user, type: NotificationType.SUCCESS)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCase(course.link) >> null
        notificationRepository.save(_) >> notification

        when:
        registrationService.approveRegistration(registrationId)

        then:
        1 * emailService.sendMessage(user.email, "Registration approved!!", "approve_registration_mail_template.xml", _)
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
        0 * notificationRepository.save(_)
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
        Notification notification = new Notification(content: "Your registration for course " + course.name + " has been approved", createdDate: LocalDateTime.now(), seen: false, user: user, type: NotificationType.SUCCESS)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCase(course.link) >> duplicateCourse
        notificationRepository.save(_) >> notification

        when:
        registrationService.approveRegistration(registrationId)

        then:
        1 * courseRepository.findFirstByLinkIgnoreCase(course.link)
        1 * notificationRepository.save(_)
        1 * registrationRepository.save(_)
        1 * emailService.sendMessage(user.email, "Registration approved!!", "approve_registration_mail_template.xml", _)
        registration.status == RegistrationStatus.APPROVED
        registration.course.status == CourseStatus.AVAILABLE
        registration.course.categories.every { it.status == CategoryStatus.AVAILABLE }
    }

    def "declineRegistration should decline a registration"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable", userId: 2L)
        def registration = new Registration(id: 1L, status: RegistrationStatus.SUBMITTED, course: new Course(name: "Course 101"), user: new User(email: "user@example.com"))
        def user = new User(id: 2L)
        registrationRepository.findById(1L) >> Optional.of(registration)
        userRepository.findById(2L) >> Optional.of(user)
        registrationFeedbackRepository.save(_ as RegistrationFeedback) >> { RegistrationFeedback feedback -> feedback }
        notificationRepository.save(_ as Notification) >> { Notification notification -> notification }
        registrationRepository.save(_ as Registration) >> { Registration reg -> reg }
        when:

        registrationService.declineRegistration(1,feedbackRequest)

        then:
        1 * registrationFeedbackRepository.save(_ as RegistrationFeedback) >> { RegistrationFeedback feedback ->
            assert feedback.comment == "Not suitable"
            assert feedback.user.id == user.id
            assert feedback.registration == registration
            assert feedback.createdDate != null
        }
        1 * notificationRepository.save(_ as Notification) >> { Notification notification ->
            assert notification.content == "Your registration for course Course 101 has been declined"
            assert notification.createdDate != null
            assert !notification.seen
            assert notification.user == registration.user
            assert notification.type == NotificationType.ERROR
        }
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
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable", userId: 2L)

        when:
        registrationRepository.findById(1L) >> Optional.empty()
        registrationService.declineRegistration(1,feedbackRequest)

        then:
        thrown(RegistrationNotFoundException)
        0 * _
    }

    def "declineRegistration should throw IllegalArgumentException if registration status is not SUBMITTED"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable", userId: 2L)
        def registration = new Registration(id: 1L, status: RegistrationStatus.APPROVED)

        when:
        registrationRepository.findById(1L) >> Optional.of(registration)
        registrationService.declineRegistration(1,feedbackRequest)

        then:
        thrown(IllegalArgumentException)
        0 * _
    }

    def "declineRegistration should throw IllegalArgumentException if user is not found"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable", userId: 2L)
        def registration = new Registration(id: 1L, status: RegistrationStatus.SUBMITTED)

        when:
        registrationRepository.findById(1L) >> Optional.of(registration)
        userRepository.findById(2L) >> Optional.empty()
        registrationService.declineRegistration(1,feedbackRequest)

        then:
        thrown(IllegalArgumentException)
        0 * _
    }
    User currentUser;
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


}
