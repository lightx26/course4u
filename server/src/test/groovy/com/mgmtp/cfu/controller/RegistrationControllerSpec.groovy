package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.RegistrationRequest
import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationEnrollDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.exception.CourseNotFoundException
import com.mgmtp.cfu.exception.RegistrationFieldNotFoundException
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.exception.UnknownErrorException
import com.mgmtp.cfu.service.RegistrationService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class RegistrationControllerSpec extends Specification {

    def registrationService = Mock(RegistrationService)

    @Subject
    RegistrationController registrationController = new RegistrationController(registrationService)

    def "getRegistrationsForAdmin with default param should return correct response"() {
        given:
        def status = "all"
        def search = ""
        def orderBy = "id"
        def isAscending = true
        def page = 1
        def pageSize = 8

        RegistrationOverviewParams registrationOverviewParams = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        RegistrationOverviewDTO registrationOverviewDTO = RegistrationOverviewDTO.builder().build()
        def pagedResponse = new PageImpl<>(
                [registrationOverviewDTO],
                PageRequest.of(page - 1, 1, Sort.by(orderBy).ascending()),
                1
        )
        registrationService.getRegistrations(registrationOverviewParams, page, pageSize) >> pagedResponse

        when:
        ResponseEntity<?> response = registrationController.getRegistrationsForAdmin(registrationOverviewParams, page, pageSize)

        then:
        response.statusCode.value() == 200
        response.body == pagedResponse
    }

    def "getRegistrationsForAdmin should response with the correct message when exception is thrown with bad status"() {
        given:
        def falseStatus = "NotFoundStatus"
        def search = ""
        def orderBy = "id"
        def isAscending = true
        def page = 1
        def pageSize = 8

        RegistrationOverviewParams badRequestParam = RegistrationOverviewParams.builder()
                .status(falseStatus)
                .search(search)
                .orderBy(orderBy)
                .isAscending(isAscending)
                .build()

        registrationService.getRegistrations(badRequestParam, page, pageSize) >> { throw new RegistrationStatusNotFoundException("Registration status not found") }

        when:
        registrationController.getRegistrationsForAdmin(badRequestParam, page, pageSize)

        then:
        thrown(RegistrationStatusNotFoundException)
    }

    def "getRegistrationsForAdmin should response with the correct message when exception is thrown with bad orderBy"() {
        given:
        def status = "all"
        def search = ""
        def falseOrderBy = "NotFoundField"
        def isAscending = true
        def page = 1
        def pageSize = 8

        RegistrationOverviewParams badRequestParam = RegistrationOverviewParams.builder()
                .status(status)
                .search(search)
                .orderBy(falseOrderBy)
                .isAscending(isAscending)
                .build()

        registrationService.getRegistrations(badRequestParam, page, pageSize) >> { throw new RegistrationFieldNotFoundException() }

        when:
        registrationController.getRegistrationsForAdmin(badRequestParam, page, pageSize)

        then:
        thrown(RegistrationFieldNotFoundException)
    }

    def "test getDetailRegistration"() {
        given:
            def registrationId = 1
            RegistrationDetailDTO registrationDetailDTO = RegistrationDetailDTO.builder().build()
            registrationService.getDetailRegistration(registrationId) >> registrationDetailDTO
        when:
            ResponseEntity<RegistrationDetailDTO> response = registrationController.getDetailRegistration(registrationId)
        then:
            response.statusCode.value() == 200
            response.body == registrationDetailDTO
    }

    def "test getDetailRegistration failed"() {
        given:
            Long id = 999L
        when:
            registrationService.getDetailRegistration(id) >> { throw new RegistrationNotFoundException("Registration not found") }
        and:
            registrationController.getDetailRegistration(id)
        then:
            def ex = thrown(RegistrationNotFoundException)
            ex.message == "Registration not found"
    }

    def 'getListOfMyRegistration: return ok response'(){
        given:
        registrationService.getMyRegistrationPage(_ as int, _ as String)>>List.of()
        when:
        def response=registrationController.getListOfMyRegistration(page,status)
        then:
        response.statusCode.value()==200
        where:
        page|status
        1|""
        2|null
        0|""
    }

    def 'test approve registration return ok response'() {
        given:
            def registrationId = 1
        when:
            def response = registrationController.approveRegistration(registrationId)
        then:
            response.statusCode.value() == 200
    }

    def 'test approve registration failed'() {
        given:
            def registrationId = 1
        when:
            registrationService.approveRegistration(registrationId) >> { throw new RegistrationNotFoundException("Registration not found") }
        and:
            registrationController.approveRegistration(registrationId)
        then:
            def ex = thrown(RegistrationNotFoundException)
            ex.message == "Registration not found"
    }

    def 'test decline registration return ok response'() {
        given:
            def registrationId = 1
            def userId = 2
            def feedbackRequest = new FeedbackRequest(comment: "Test comment")
        when:
            def response = registrationController.declineRegistration(registrationId,feedbackRequest)
        then:
            response.statusCode.value() == 200
    }

    def 'startLearningCourse return exception'(){
        given:
        registrationService.startLearningCourse(_ as Long)>> false
        when:
        registrationController.startLearningCourse(1)
        then:
        def ex=thrown(UnknownErrorException)
    }

    def 'test delete registration return ok response'() {
        given:
        registrationService.deleteRegistration(_ as Long)>>{}
        when:
        registrationController.deleteRegistration(1)
        then:
        noExceptionThrown()
    }

    def "finishLearning calculates and updates Registration"() {
        given:
        Long id = 1

        when:
        def result = registrationController.finishLearning(id)

        then:
        1 * registrationService.calculateScore(id)
        result.statusCode == HttpStatus.OK
    }

    def "should return ok response when close registration successfully"() {
        given:
            def registrationId = 1
            def feedbackRequest = new FeedbackRequest(comment: "Close registration")
        when:
            def response = registrationController.closeRegistration(registrationId, feedbackRequest)
        then:
            response.statusCode.value() == 200
    }

    def "should throw exception when service throw exception"() {
        given:
            def registrationId = 1
            def feedbackRequest = new FeedbackRequest(comment: "Close registration")
            registrationService.closeRegistration(registrationId, feedbackRequest) >> { throw new RegistrationNotFoundException("Registration not found") }
        when:
            def response = registrationController.closeRegistration(registrationId, feedbackRequest)
        then:
            def e = thrown(RegistrationNotFoundException)
            e.message == "Registration not found"
    }
    def 'test discard registration return ok response'() {
        given:
            registrationService.discardRegistration(_ as Long)>>{}
        when:
            registrationController.discardRegistration(1)
        then:
            noExceptionThrown()
    }
    def 'test discard registration failed'() {
        given:
            def registrationId = 1
        when:
            registrationService.discardRegistration(registrationId) >> { throw new RegistrationNotFoundException("Registration not found") }
        and:
            registrationController.discardRegistration(registrationId)
        then:
            def ex = thrown(RegistrationNotFoundException)
            ex.message == "Registration not found"
    }

    def "createRegistrationFromExistingCourses should return 200 OK when inputs are valid"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: 10, durationUnit: "DAY")

        when:
        ResponseEntity<?> response = registrationController.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

        then:
        1 * registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)
        response.statusCode == HttpStatus.OK
    }

    def "createRegistrationFromExistingCourses should return 400 Bad Request when BadRequestRuntimeException is thrown"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: null, durationUnit: null)
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO) >> { throw new BadRequestRuntimeException("Duration and Duration Unit must not be null") }

        when:
        ResponseEntity<?> response = registrationController.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "createRegistrationFromExistingCourses should return 404 Not Found when CourseNotFoundException is thrown"() {
        given:
        Long courseId = 1L
        RegistrationEnrollDTO registrationEnrollDTO = new RegistrationEnrollDTO(duration: 10, durationUnit: "DAY")
        registrationService.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO) >> { throw new CourseNotFoundException("Course not found") }

        when:
        ResponseEntity<?> response = registrationController.createRegistrationFromExistingCourses(courseId, registrationEnrollDTO)

        then:
        thrown(CourseNotFoundException)
    }

    def "verifyDeclineRegistration should call registrationService with correct parameters"() {
        given:
        Long id = 1L
        Map<String, String> longDocumentStatusMap = ["key": "value"]
        String status = "declined"

        when:
        registrationController.verifyDeclineRegistration(id, longDocumentStatusMap, status)

        then:
        1*registrationService.verifyRegistration(id, longDocumentStatusMap, status)
    }

    def "editRegistration updates Registration"() {
        given:
        def id = 1
        def registrationRequest = Mock(RegistrationRequest)

        when:
        def result = registrationController.editRegistration(id, registrationRequest)

        then:
        result.statusCode == HttpStatus.CREATED
    }

    def "isExistAvailableCourse check if course of registration is AVAILABLE"() {
        given:
        Long id = 6

        when:
        def result = registrationController.isExistAvailableCourse(id)

        then:
        result.statusCode == HttpStatus.OK
    }

}
