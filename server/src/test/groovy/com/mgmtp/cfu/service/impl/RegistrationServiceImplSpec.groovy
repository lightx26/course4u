package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.entity.Document
import com.mgmtp.cfu.enums.DocumentStatus
import com.mgmtp.cfu.enums.DocumentType
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.exception.ConflictRuntimeException
import com.mgmtp.cfu.dto.MailContentUnit
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.enums.CategoryStatus
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.enums.NotificationType
import com.mgmtp.cfu.exception.CourseNotFoundException
import com.mgmtp.cfu.exception.DuplicateCourseException
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.exception.ForbiddenException
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.exception.RegistrationNotFoundException
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
import com.mgmtp.cfu.service.CategoryService
import com.mgmtp.cfu.service.IEmailService
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.util.EmailUtil
import org.dom4j.DocumentHelper
import org.springframework.data.domain.PageImpl
import com.mgmtp.cfu.service.NotificationService
import com.mgmtp.cfu.service.RegistrationFeedbackService
import com.mgmtp.cfu.service.UploadService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import com.mgmtp.cfu.service.CourseService
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock(RegistrationRepository)

    MapperFactory<Registration> registrationMapperFactory = Mock(MapperFactory)

    RegistrationOverviewMapper registrationOverviewMapper = Mock(RegistrationOverviewMapper)

    CourseRepository courseRepository = Mock(CourseRepository)

    NotificationService notificationService = Mock(NotificationService){
        notificationService.sendNotificationToUser(_,_,_)>> {}
    }

    RegistrationFeedbackService feedbackService = Mock(RegistrationFeedbackService)

    IEmailService emailService = Mock(IEmailService)

    UserRepository userRepository = Mock(UserRepository)

    def registrationDetailMapper = Mock(RegistrationDetailMapper)

    NotificationRepository notificationRepository = Mock(){
        notificationRepository.save(_)>>{}
    }

    CourseService courseService = Mock()

    RegistrationFeedbackRepository registrationFeedbackRepository = Mock()

    def documentService = Mock(DocumentServiceImpl)

    CategoryService categoryService = Mock()

    UploadService uploadService = Mock()

    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(registrationRepository, registrationMapperFactory,
                                                                              registrationOverviewMapper, courseRepository,
                                                                              notificationRepository, registrationFeedbackRepository,
                                                                              emailService, userRepository,
                                                                              notificationService, feedbackService,
                                                                              courseService, categoryService,
                                                                              uploadService, documentService)

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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(ZonedDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
        when:
        def result = registrationService.getMyRegistrationPage(1, status)
        then:
        result.totalElements == 1
    }

    def "test getMyRegistrationPage with specific status"() {
        given:
        def userId = 1
        def status = "APPROVED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(ZonedDateTime.now()).build()
        def registration2 = Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(ZonedDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations, registration2)
        registrationMapperFactory.getDTOMapper(_) >> Optional.of(registrationOverviewMapper)
        registrationOverviewMapper.toDTO(_) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(ZonedDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations)
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(ZonedDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations)
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(ZonedDateTime.now()).build()
        def registration2 = Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(ZonedDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        registrationMapperFactory.getDTOMapper(_) >> Optional.of(registrationOverviewMapper)
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations, registration2)
        registrationOverviewMapper.toDTO(_) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(ZonedDateTime.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)

        when:
        registrationService.getMyRegistrationPage(0, status)

        then:
        thrown(IllegalArgumentException)
    }

    def "startLearningCourse: Not found any registration"() {
        given:
        registrationRepository.existsByIdAndUserId(_ as Long, _ as Long) >> false
        when:
        registrationService.startLearningCourse(1)
        then:
        def e = thrown(BadRequestRuntimeException)
    }

    def "startLearningCourse: This course was started learning"() {
        given:
        registrationRepository.existsByIdAndUserId(_ as Long, _ as Long) >> true
        registrationRepository.findById(_ as Long) >> Optional.of(Registration.builder().id(1).startDate(ZonedDateTime.now()).build())
        when:
        registrationService.startLearningCourse(1)
        then:
        def e = thrown(ConflictRuntimeException)
    }

    def "startLearningCourse: return true"() {
        given:
        registrationRepository.existsByIdAndUserId(_ as Long, _ as Long) >> true
        registrationRepository.findById(_ as Long) >> Optional.of(Registration.builder().id(1).status(RegistrationStatus.APPROVED).startDate(null).build())
        when:
        def result = registrationService.startLearningCourse(1)
        then:
        result
    }

    def "startLearningCourse: This registration requires approval by admin."() {
        given:
        registrationRepository.existsByIdAndUserId(_ as Long, _ as Long) >> true
        registrationRepository.findById(_ as Long) >> Optional.of(Registration.builder().id(1).status(RegistrationStatus.SUBMITTED).startDate(null).build())
        when:
        registrationService.startLearningCourse(1)
        then:
        def e = thrown(BadRequestRuntimeException)
    }

    def "startLearningCourse: return false"() {
        given:
        registrationRepository.existsByIdAndUserId(_ as Long, _ as Long) >> true
        registrationRepository.findById(_ as Long) >> Optional.empty()
        when:
        def result = registrationService.startLearningCourse(1)
        then:
        !result

    }

    /*
     *  Test cases for getAllRegistrations,
     */
    def "getRegistrations should return registration list in the correct order"() {
        given:
        def status = "all"
        def search = ""
        def orderBy = "id"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        def registration1 = Registration.builder().id(0).build()
        def registration2 = Registration.builder().id(1).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration1, registration2], pageable, 2)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(0).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(1).build()

        when:
        Page<RegistrationOverviewDTO> result = registrationService.getRegistrations(params, page, pageSize)

        then:
        result.size() == 2
        result[0].id == 0
        result[1].id == 1
    }

    def "getRegistrations should return registration list in descending order when configurated"(){
        given:
        def status = "all"
        def search = ""
        def orderBy = "id"
        def isAscending = false
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        def registration1 = Registration.builder().id(1).build()
        def registration2 = Registration.builder().id(2).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration2, registration1], pageable, 2)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 2
        response[0].id == 2
        response[1].id == 1
    }

    def "getRegistrations should return registrations ordered by the provided input"(){
        given:
        def status = "all"
        def search = ""
        def orderBy = "lastUpdated"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        def registration1 = Registration.builder().id(1).lastUpdated(LocalDateTime.now().plusDays(1)).build()
        def registration2 = Registration.builder().id(2).lastUpdated(LocalDateTime.now()).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration2, registration1], pageable, 2)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 2
        response[0].id == 2
        response[1].id == 1
    }

    def "getRegistrations should return the registrations with the provided status"(){
        given:
        def status = "SUBMITTED"
        def search = ""
        def orderBy = "id"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        def registration1 = Registration.builder().id(1).status(RegistrationStatus.SUBMITTED).build()
        def registration2 = Registration.builder().id(2).status(RegistrationStatus.APPROVED).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration1], pageable, 1)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.SUBMITTED).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).status(RegistrationStatus.APPROVED).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 1
        response[0].status == RegistrationStatus.SUBMITTED
    }

    def "getRegistrations should return the registrations with the provided courseName"(){
        given:
        def status = "all"
        def search = "Machine"
        def orderBy = "id"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        Course course1 = Course.builder().id(1).name("machine learning & AI").build()
        Course course2 = Course.builder().id(1).name("NodeJS").build()
        def registration1 = Registration.builder().id(1).course(course1).build()
        def registration2 = Registration.builder().id(2).course(course2).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration1], pageable, 1)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).courseName(course1.name).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).courseName(course2.name).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 1
        response[0].courseName == course1.name
    }

    def "getRegistrations should return the registrations with the provided userName"(){
        given:
        def status = "all"
        def search = "Phan"
        def orderBy = "id"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        User user1 = User.builder().username("Phan Hoang").build()
        User user2 = User.builder().username("SangTraan").build()
        def registration1 = Registration.builder().id(1).user(user1).build()
        def registration2 = Registration.builder().id(2).user(user2).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration1], pageable, 1)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).userName(user1.username).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).userName(user2.username).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 1
        response[0].userName == user1.username
    }

    def "getRegistrations should return all registrations if the searchInput is found in both course and user"(){
        given:
        def status = "all"
        def search = "em"
        def orderBy = "id"
        def isAscending = true
        RegistrationOverviewParams params = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        def page = 1
        def pageSize = 8

        User user = User.builder().username("Em Linh xinh gai").build()
        Course course =  Course.builder().name("Em Duy dep gai").build()
        def registration1 = Registration.builder().id(1).user(user).build()
        def registration2 = Registration.builder().id(2).course(course).build()

        Pageable pageable = PageRequest.of(page - 1, pageSize)

        Page<Registration> registrations = new PageImpl<>([registration1, registration2], pageable, 2)

        registrationRepository.findAll(_, pageable) >> registrations

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).userName(user.username).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).courseName(course.name).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page, pageSize)

        then:
        response.size() == 2
        response[0].userName == user.username
        response[1].courseName == course.name
    }

    def "should approve registration when status is submitted and no duplicate course exists"() {
        given:
        Long registrationId = 1L
        User user = new User(email: "test@example.com", fullName: "Quang Nguyen")
        Course course = new Course(name: "Course Name", link: "course-link", categories: [new Category(status: CategoryStatus.PENDING)])
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED, course: course, user: user)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(course.link,CourseStatus.AVAILABLE) >> Optional.empty()
        EmailUtil.generateTitle("Registration Approved") >> new MailContentUnit(id: "title", innerContent: List.of(DocumentHelper.createText("Registration Approved")))
        EmailUtil.generateGreeting("Dear {name},", user) >> new MailContentUnit(id: "greeting", innerContent: List.of(DocumentHelper.createText("Dear Quang,")))
        when:
        registrationService.approveRegistration(registrationId)

        then:
        1 * emailService.sendMail(user.email, EmailUtil.generateSubject("Registration Approved"), "email-template.xml", _)
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
        def e = thrown(BadRequestRuntimeException)
        e.message == "Registration must be in submitted status to be approved"
        0 * courseRepository.findFirstByLinkIgnoreCase(_)
        0 * notificationService.sendNotificationToUser(_, _, _)
        0 * registrationRepository.save(_)
        0 * emailService.sendMail(_, _, _, _)
    }

    def "should approve registration and handle duplicate course"() {
        given:
        Long registrationId = 1L
        User user = new User(email: "test@example.com")
        Course course = new Course(name: "Course Name", link: "course-link", categories: [new Category(status: CategoryStatus.PENDING)])
        Course duplicateCourse = new Course(link: "course-link")
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED, course: course, user: user)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(_,_) >> Optional.of(duplicateCourse)
        registration.getCourse().status = CourseStatus.PENDING

        when:
        registrationService.approveRegistration(registrationId)

        then:
        thrown(DuplicateCourseException)
    }

    def "declineRegistration should decline a registration"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        def registration = new Registration(id: 1L, status: RegistrationStatus.SUBMITTED, course: new Course(name: "Course 101"), user: new User(email: "user@example.com", fullName: "User"))
        registrationRepository.findById(1L) >> Optional.of(registration)
        registrationRepository.save(_ as Registration) >> { Registration reg -> reg }

        when:
        registrationService.declineRegistration(1, feedbackRequest)

        then:
        1 * feedbackService.sendFeedback(registration, feedbackRequest.getComment())
        1 * registrationRepository.save(_ as Registration) >> { Registration reg ->
            assert reg.status == RegistrationStatus.DECLINED
            assert reg.lastUpdated != null
        }
        1 * emailService.sendMail(_, _, _, _)
    }

    def "declineRegistration should throw RegistrationNotFoundException if registration is not found"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")

        when:
        registrationRepository.findById(1L) >> Optional.empty()
        registrationService.declineRegistration(1, feedbackRequest)

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
        registrationService.declineRegistration(1, feedbackRequest)

        then:
        thrown(BadRequestRuntimeException)
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
//        GroovyMock(AuthUtils, global: true)
//        AuthUtils.getCurrentUser() >> currentUser
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
        1 * userRepository.findAllByRole(Role.ADMIN)>>List.of()


        when:
        def result = registrationService.createRegistration(registrationRequest)

        then:
        result == true
    }

    def "finishRegistration: update registration and calculate score successfully"() {
        given:
        def registrationId = 1L
        def course = Course.builder().level(CourseLevel.ADVANCED).build()

        def registration = Registration.builder()
                .id(registrationId)
                .status(RegistrationStatus.APPROVED)
                .startDate(LocalDateTime.of(2024, 7, 22, 18, 0, 0).atZone(ZoneId.systemDefault()))
                .score(1008)
                .course(course)
                .duration(1)
                .durationUnit(DurationUnit.DAY)
                .build()

        registrationRepository.findById(registrationId) >> Optional.of(registration)

        when:
        registrationService.finishRegistration(registrationId)

        then:
        1 * registrationRepository.save(registration) >> { Registration reg ->
            assert reg.status == RegistrationStatus.DONE
            assert reg.lastUpdated != null
            assert reg.score != null
        }
    }

    def "finishRegistration: should throw BadRequestRuntimeException when registration is not APPROVED"() {
        given:
        def registrationId = 1L
        def registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED)
        registrationRepository.findById(registrationId) >> Optional.of(registration)

        when:
        registrationService.finishRegistration(registrationId)

        then:
        thrown(BadRequestRuntimeException)
    }


    def "finishRegistration: should throw RegistrationNotFoundException when id is invalid"() {
        given:
        def registrationId = 101L
        registrationRepository.findById(registrationId) >> Optional.empty()

        when:
        registrationService.finishRegistration(registrationId)

        then:
        thrown(RegistrationNotFoundException)
    }

    def "closeRegistration: should close the registration without sending feedback"() {
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
        1 * emailService.sendMail(user.getEmail(), EmailUtil.generateSubject("Registration Closed"), _, _)
    }

    def "closeRegistration: should close the registration with feedback"() {
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
        1 * emailService.sendMail(user.getEmail(), EmailUtil.generateSubject("Registration Closed"), _, _)
    }

    def "closeRegistration: should throw RegistrationNotFoundException if registration is not found"() {
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
        registrationRepository.findById(id) >> Optional.empty()

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should throw ForbiddenException if user is not the owner of the registration"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().user(User.builder().id(2).build()).build()
        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(ForbiddenException)
    }

    def "should throw BadRequestRunTimeException if registration status is not DRAFT or DISCARDED"() {
        given:
        Registration registration = Registration.builder().user(User.builder().id(1).build()).build()
        Long id = 1L
        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        registrationService.deleteRegistration(id)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should delete registration if all conditions are met"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().status(RegistrationStatus.DISCARDED).user(User.builder().id(1).build()).build()
        registrationRepository.findById(id) >> Optional.of(registration)

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

    def "verifyRegistration should process registration with id: #id and status: #status"() {
        given:
        def registration = Registration.builder().user(User.builder().username("").email("").build()).id(id).status(currentStatus).course(Course.builder().name("").build()).build()
        userRepository.findAllByRole(Role.ADMIN) >> List.of(User.builder().email("").username("").build())
        emailService.sendMail(_, _, _, _) >> {}
        registrationRepository.findById(id) >> Optional.of(registration)
        documentService.verifyDocument(1L, _) >> Document.builder().status(DocumentStatus.APPROVED).type(DocumentType.CERTIFICATE).build()
        documentService.verifyDocument(2L, _) >> Document.builder().status(DocumentStatus.APPROVED).type(DocumentType.PAYMENT).build()
        documentService.verifyDocument(3L, _) >> Document.builder().status(DocumentStatus.REFUSED).type(DocumentType.PAYMENT).build()

        when:
        registrationService.verifyRegistration(id, longDocumentStatusMap, status)

        then:
        noExceptionThrown()

        where:
        id | longDocumentStatusMap                           | status              | currentStatus
        1L | ["1": "APPROVED", "2": "APPROVED"]              | "VERIFIED"          | RegistrationStatus.VERIFYING
        2L | ["3": "REFUSED", "feedbackRequest": "Feedback"] | "DOCUMENT_DECLINED" | RegistrationStatus.VERIFYING
    }

    def "verifyRegistration should throw exception for invalid registration status"() {
        given: "a registration with incorrect status"
        def registration = Registration.builder().user(User.builder().username("").email("").build()).id(id).status(currentStatus).course(Course.builder().name("").build()).build()
        userRepository.findAllByRole(Role.ADMIN) >> List.of(User.builder().email("").username("").build())
        emailService.sendMail(_, _, _, _) >> {}
        registrationRepository.findById(id) >> Optional.of(registration)
        documentService.verifyDocument(1L, _) >> Document.builder().status(DocumentStatus.APPROVED).type(DocumentType.CERTIFICATE).build()
        documentService.verifyDocument(2L, _) >> Document.builder().status(DocumentStatus.APPROVED).type(DocumentType.PAYMENT).build()
        documentService.verifyDocument(3L, _) >> Document.builder().status(DocumentStatus.REFUSED).type(DocumentType.PAYMENT).build()


        when:
        registrationService.verifyRegistration(1L, [:], "VERIFIED")

        then:
        thrown(BadRequestRuntimeException)
        where:
        id | longDocumentStatusMap                           | status              | currentStatus
        1L | ["1": "APPROVED", "2w": "APPROVED"]              | "VERIFIED"          | RegistrationStatus.VERIFYING
        1L | ["1": "APPROVED", "2": "APPROVED"]              | "VERIFIED"          | RegistrationStatus.APPROVED
        1L | ["1": "APPROVED", "2": "APPROVED"]              | "DOCUMENT_DECLINED"          | RegistrationStatus.VERIFYING

    }

    def "createRegistrationFromExistingCourses should throw BadRequestRuntimeException when duration or durationUnit is null"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: null, durationUnit: null)

        when:
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO,false)

        then:
        def e = thrown(BadRequestRuntimeException)
        e.message == "Duration and Duration Unit must not be null"
    }

    def "createRegistrationFromExistingCourses should throw CourseNotFoundException when course is not found"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: 10, durationUnit: "DAY")
        courseRepository.findById(courseId) >> Optional.empty()

        when:
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO,false)

        then:
        def e = thrown(CourseNotFoundException)
        e.message == "Course not found"
    }

    def "createRegistrationFromExistingCourses should create and save a new registration when inputs are valid"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: 10, durationUnit: "DAY")
        Course course = new Course(id: courseId)
        courseRepository.findById(courseId) >> Optional.of(course)
        userRepository.findAllByRole(Role.ADMIN)>>List.of()

        when:
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO,false)

        then:
        1 * registrationRepository.save(_ as Registration) >> { Registration registration ->
            assert registration.course == course
            assert registration.status == RegistrationStatus.SUBMITTED
            assert registration.registerDate == LocalDate.now()
            assert registration.duration == 10
            assert registration.durationUnit == DurationUnit.DAY
            assert registration.lastUpdated != null
            assert registration.user != null
        }
    }

    def "editRegistration updates Registration"() {
        given:
        Long id = 1
        RegistrationRequest registrationRequest = Mock()
        User user = new User(id: 1)
        Course course = new Course(id: 8)
        def asDraft = false;
        def categories = [Mock(Category), Mock(Category)]
        Registration registration = Registration.builder()
                                                .user(user)
                                                .course(course)
                                                .status(RegistrationStatus.DRAFT)
                                                .build()

        and:
        registrationRepository.findById(id) >> Optional.of(registration)
        courseRepository.findById(registration.getCourse().getId()) >> Optional.of(course)
        categoryService.findOrCreateNewCategory(_) >> categories
        userRepository.findAllByRole(Role.ADMIN)>>List.of()


        when:
        registrationService.editRegistration(id, registrationRequest,asDraft)

        then:
        1 * registrationRepository.save(_)
        1 * courseRepository.save(_)
    }

    def "editRegistration throws exception when user is not owner of Registration"() {
        given:
        Long id = 1
        RegistrationRequest registrationRequest = Mock()
        def user = new User(id: 2)
        Course course = new Course(id: 8)
        def asDraft = false
        Registration registration = Registration.builder()
                                                .user(user)
                                                .course(course)
                                                .status(RegistrationStatus.DRAFT)
                                                .build()

        and:
        registrationRepository.findById(id) >> Optional.of(registration)
        courseRepository.findById(registration.getCourse().getId()) >> Optional.of(course)

        when:
        registrationService.editRegistration(id, registrationRequest,asDraft)

        then:
        thrown(IllegalArgumentException)
    }

    def "editRegistration throws exception when status is invalid"() {
        given:
        Long id = 1
        RegistrationRequest registrationRequest = Mock()
        def user = new User(id: 1)
        Course course = new Course(id: 8)
        def asDraft = false
        Registration registration = Registration.builder()
                                                .user(user)
                                                .course(course)
                                                .status(RegistrationStatus.DONE)
                                                .build()

        and:
        registrationRepository.findById(id) >> Optional.of(registration)
        courseRepository.findById(registration.getCourse().getId()) >> Optional.of(course)

        when:
        registrationService.editRegistration(id, registrationRequest,asDraft)

        then:
        thrown(IllegalArgumentException)
    }

    def "isExistAvailableCourse return TRUE or FALSE whether registration has course which has AVAILABLE status"() {
        given:
        Long id = 6
        Course course = new Course(status: CourseStatus.AVAILABLE)
        Registration registration = new Registration(id: id, course: course)

        and:
        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        def result = registrationService.isExistAvailableCourse(id)

        then:
        result == true || result == false
    }

    def "createRegistrationAsDraft should convert RegistrationRequest to CourseRequest and save Registration"() {
        given:
        RegistrationRequest registrationRequest = new RegistrationRequest(
                name: "Test Course",
                link: "http://example.com",
                thumbnailFile: null,
                thumbnailUrl: "http://example.com/image.png",
                teacherName: "Test Teacher",
                duration: 10
        )
        CourseResponse courseResponse = CourseResponse.builder().build();
        courseService.createCourse(_)>>courseResponse
        when: "The method is called"
        registrationService.createRegistrationAsDraft(registrationRequest)

        then:
        noExceptionThrown()
    }
}
