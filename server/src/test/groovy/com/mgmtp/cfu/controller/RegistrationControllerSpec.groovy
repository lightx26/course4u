package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.registrationdto.FeedbackRequest
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.UnknownErrorException
import com.mgmtp.cfu.service.RegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class RegistrationControllerSpec extends Specification {
    def registrationService = Mock(RegistrationService)
    @Subject
    RegistrationController registrationController = new RegistrationController(registrationService)
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
        def response=registrationController.getListOfMyRegistration(page,status);
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
}
