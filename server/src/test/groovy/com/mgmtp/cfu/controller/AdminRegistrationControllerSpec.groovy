package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.service.RegistrationService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class AdminRegistrationControllerSpec extends Specification {
    RegistrationService registrationService = Mock()
    @Subject
    AdminRegistrationController AdminRegistrationController = new AdminRegistrationController(registrationService)

    def "getRegistrationForAdmin has correct response status and correct DTO"() {
        given:
            def page = 1
            def status = ""

            RegistrationOverviewDTO registrationOverviewDTO = RegistrationOverviewDTO.builder().build()
            def pagedResponse = new PageImpl<>([registrationOverviewDTO], PageRequest.of(page, 1), 1)
            registrationService.getAllRegistrations(page) >> pagedResponse

        when:
            ResponseEntity<?> response = AdminRegistrationController.getRegistrationForAdmin(page, status)

        then:
            response.statusCode.value() == 200
            response.body == pagedResponse
    }

    def "getRegistrationForAdmin with incorrect page throw an exception"() {
        given:
            def page = -1
            def status = ""

        when:
            ResponseEntity<?> response = AdminRegistrationController.getRegistrationForAdmin(page, status)

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
            ResponseEntity<?> response = AdminRegistrationController.getRegistrationForAdmin(page, status)

        then:
            def e = thrown(RegistrationStatusNotFoundException)
            e.message == "Registration status not found"
    }
}
