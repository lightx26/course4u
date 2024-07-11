package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.RegistrationDetailDTO
import com.mgmtp.cfu.dto.RegistrationOverviewDTO
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.service.RegistrationService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

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

    def "getRegistrationForAdmin has correct response status and correct DTO"() {
        given:
        def page = 1
        def status = ""

        RegistrationOverviewDTO registrationOverviewDTO = RegistrationOverviewDTO.builder().build()
        def pagedResponse = new PageImpl<>([registrationOverviewDTO], PageRequest.of(page, 1), 1)
        registrationService.getAllRegistrations(page) >> pagedResponse

        when:
        ResponseEntity<?> response = registrationController.getRegistrationForAdmin(page, status)

        then:
        response.statusCode.value() == 200
        response.body == pagedResponse
    }

    def "getRegistrationForAdmin with incorrect page throw an exception"() {
        given:
        def page = -1
        def status = ""

        when:
        ResponseEntity<?> response = registrationController.getRegistrationForAdmin(page, status)

        then:
        response.statusCode.value() == 400
        response.getBody() == "Page number must be greater than 0"
    }

    def "getRegistrationForAdmin with incorrect status throw an exception"(){
        given:
        def page = 1
        def status = "incorrect"

        registrationService.getRegistrationByStatus(page, status) >> { throw new RegistrationStatusNotFoundException("Registration status not found") }

        when:
        ResponseEntity<?> response = registrationController.getRegistrationForAdmin(page, status)

        then:
        def e = thrown(RegistrationStatusNotFoundException)
        e.message == "Registration status not found"
    }

    def 'getListOfMyRegistration: return ok response'(){
        given:
        registrationService.getMyRegistrationPage(_,_)>>List.of()
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

}
