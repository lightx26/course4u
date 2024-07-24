package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.exception.RegistrationFieldNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.service.RegistrationService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class AdminRegistrationControllerSpec extends Specification {
    RegistrationService registrationService = Mock()
    @Subject
    AdminRegistrationController AdminRegistrationController = new AdminRegistrationController(registrationService)
    def "getRegistrationsForAdmin with default param should return correct response"() {
        given:
            def status = "all"
            def search = ""
            def orderBy = "id"
            def isAscending = true
            def page = 1

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
            registrationService.getRegistrations(registrationOverviewParams, page) >> pagedResponse

        when:
            ResponseEntity<?> response = AdminRegistrationController.getRegistrationsForAdmin(registrationOverviewParams, page)

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

            RegistrationOverviewParams badRequestParam = RegistrationOverviewParams.builder()
                    .status(falseStatus)
                    .search(search)
                    .orderBy(orderBy)
                    .isAscending(isAscending)
                    .build()

            registrationService.getRegistrations(badRequestParam, page) >> { throw new RegistrationStatusNotFoundException("Registration status not found") }

        when:
            AdminRegistrationController.getRegistrationsForAdmin(badRequestParam, page)

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

            RegistrationOverviewParams badRequestParam = RegistrationOverviewParams.builder()
                    .status(status)
                    .search(search)
                    .orderBy(falseOrderBy)
                    .isAscending(isAscending)
                    .build()

            registrationService.getRegistrations(badRequestParam, page) >> { throw new RegistrationFieldNotFoundException() }

        when:
            AdminRegistrationController.getRegistrationsForAdmin(badRequestParam, page)

        then:
            thrown(RegistrationFieldNotFoundException)
    }
}
