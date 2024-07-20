package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.coursedto.CourseRegistrationDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.exception.BadRequestRuntimeException
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
import com.mgmtp.cfu.repository.RegistrationRepository

import com.mgmtp.cfu.service.IEmailService
import com.mgmtp.cfu.service.NotificationService;
import com.mgmtp.cfu.service.RegistrationFeedbackService;
import org.springframework.data.domain.Page
import org.springframework.data.domain.Page;
import com.mgmtp.cfu.service.IEmailService;
import com.mgmtp.cfu.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime


class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock(RegistrationRepository)
    MapperFactory<Registration> registrationMapperFactory = Mock(MapperFactory)
    RegistrationOverviewMapper registrationOverviewMapper = Mock(RegistrationOverviewMapper)
    CourseRepository courseRepository = Mock(CourseRepository)
    NotificationService notificationService = Mock(NotificationService)
    RegistrationFeedbackService feedbackService = Mock(RegistrationFeedbackService)
    IEmailService emailService = Mock(IEmailService)
    UserRepository userRepository = Mock(UserRepository)
    def registrationDetailMapper = Mock(RegistrationDetailMapper)
    NotificationRepository notificationRepository = Mock();
    CourseService courseService = Mock()
    RegistrationFeedbackRepository registrationFeedbackRepository = Mock();

    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(
            registrationRepository, registrationMapperFactory, registrationOverviewMapper,
            courseRepository, notificationRepository, registrationFeedbackRepository,
            emailService, userRepository, notificationService, feedbackService, courseService)

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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations,registrayion2)
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDateTime.now()).build()
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
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDateTime.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        registrationMapperFactory.getDTOMapper(_) >> Optional.of(registrationOverviewMapper)
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations,registrayion2)
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
        registrationRepository.getSortedRegistrations(userId) >> List.of(registrations)
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

            def registration1 = Registration.builder().id(0).build()
            def registration2 = Registration.builder().id(1).build()

            Page<Registration> registrations = new PageImpl<>([registration1, registration2], PageRequest.of(page, 8), 2)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(0).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(1).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getRegistrations(params, page)

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

            def registration1 = Registration.builder().id(1).build()
            def registration2 = Registration.builder().id(2).build()

            Page<Registration> registrations = new PageImpl<>([registration2, registration1], PageRequest.of(page - 1, 8), 2)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).descending()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
            Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

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

            def registration1 = Registration.builder().id(1).lastUpdated(LocalDateTime.now().plusDays(1)).build()
            def registration2 = Registration.builder().id(2).lastUpdated(LocalDateTime.now()).build()

            Page<Registration> registrations = new PageImpl<>([registration2, registration1], PageRequest.of(page - 1, 8), 2)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
            Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

        then:
            response.size() == 2
            response[0].id == 2
            response[1].id == 1
    }

    def "getRegistrations should return the registrations with the provided status"(){
        given:
            def status = "submitted"
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

            def registration1 = Registration.builder().id(1).status(RegistrationStatus.SUBMITTED).build()
            def registration2 = Registration.builder().id(2).status(RegistrationStatus.APPROVED).build()

            Page<Registration> registrations = new PageImpl<>([registration1], PageRequest.of(page - 1, 8), 1)

            registrationRepository.getOptionalRegistrationsWithStatus(_, _, _) >> {
                args -> {
                    def pageRequest = args[2] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.SUBMITTED).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).status(RegistrationStatus.APPROVED).build()

        when:
            Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

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

            Course course1 = Course.builder().id(1).name("machine learning & AI").build()
            Course course2 = Course.builder().id(1).name("NodeJS").build()
            def registration1 = Registration.builder().id(1).course(course1).build()
            def registration2 = Registration.builder().id(2).course(course2).build()

            Page<Registration> registrations = new PageImpl<>([registration1], PageRequest.of(page - 1, 8), 1)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    def searchInput = args[0]
                    assert searchInput.toLowerCase() == search.toLowerCase()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).courseName(course1.name).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).courseName(course2.name).build()

        when:
        Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

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

            User user1 = User.builder().username("Phan Hoang").build()
            User user2 = User.builder().username("SangTraan").build()
            def registration1 = Registration.builder().id(1).user(user1).build()
            def registration2 = Registration.builder().id(2).user(user2).build()

            Page<Registration> registrations = new PageImpl<>([registration1], PageRequest.of(page - 1, 8), 1)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    def searchInput = args[0]
                    assert searchInput.toLowerCase() == search.toLowerCase()

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).userName(user1.username).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).userName(user2.username).build()

        when:
            Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

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

            User user = User.builder().username("Em Linh xinh gai").build()
            Course course =  Course.builder().name("Em Duy dep gai").build()
            def registration1 = Registration.builder().id(1).user(user).build()
            def registration2 = Registration.builder().id(2).course(course).build()

            Page<Registration> registrations = new PageImpl<>([registration1, registration2], PageRequest.of(page - 1, 8), 1)

            registrationRepository.getOptionalRegistrationsWithoutStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest
                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8
                    assert pageRequest.sort == Sort.by(orderBy).ascending()

                    def searchInput = args[0]
                    assert searchInput == search

                    registrations
                }
            }

        registrationOverviewMapper.toDTO(registration1) >> RegistrationOverviewDTO.builder().id(1).userName(user.username).build()
        registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).courseName(course.name).build()

        when:
            Page<RegistrationOverviewDTO> response = registrationService.getRegistrations(params, page)

        then:
            response.size() == 2
            response[0].userName == user.username
            response[1].courseName == course.name
    }



    def "should approve registration when status is submitted and no duplicate course exists"() {
        given:
        Long registrationId = 1L
        User user = new User(email: "test@example.com")
        Course course = new Course(name: "Course Name", link: "course-link", categories: [new Category(status: CategoryStatus.PENDING)])
        Registration registration = new Registration(id: registrationId, status: RegistrationStatus.SUBMITTED, course: course, user: user)

        registrationRepository.findById(registrationId) >> Optional.of(registration)
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(course.link,CourseStatus.AVAILABLE) >> Optional.empty()

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
        def e = thrown(BadRequestRuntimeException)
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
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(_,_) >> Optional.of(duplicateCourse)

        when:
        registrationService.approveRegistration(registrationId)

        then:
        thrown(DuplicateCourseException)
    }

    def "declineRegistration should decline a registration"() {
        given:
        def feedbackRequest = new FeedbackRequest(comment: "Not suitable")
        def registration = new Registration(id: 1L, status: RegistrationStatus.SUBMITTED, course: new Course(name: "Course 101"), user: new User(email: "user@example.com"))
        def user = new User(id: 1L)
        registrationRepository.findById(1L) >> Optional.of(registration)
        registrationRepository.save(_ as Registration) >> { Registration reg -> reg }
        when:

        registrationService.declineRegistration(1, feedbackRequest)

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

    def "verifyDeclineRegistration should throw exception if registration not in verifying status"() {
        given:
        Long id = 1L
        FeedbackRequest feedbackRequest = new FeedbackRequest(comment: "Invalid document")
        Registration registration = new Registration(id: id, status: RegistrationStatus.VERIFIED)
        userRepository.findAllByRole(Role.ADMIN) >> List.of(User.builder().username("a").email("abc@gmail.com").build())
        emailService.sendMessage(_ as String, _ as String, _ as String, _ as List<MailContentUnit>) >> {}
        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        registrationService.verifyDeclineRegistration(id, feedbackRequest)

        then:
        thrown(BadRequestRuntimeException)
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
    def "createRegistrationFromExistingCourses should throw BadRequestRuntimeException when duration or durationUnit is null"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: null, durationUnit: null)

        when:
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

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
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

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

        when:
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

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
}
