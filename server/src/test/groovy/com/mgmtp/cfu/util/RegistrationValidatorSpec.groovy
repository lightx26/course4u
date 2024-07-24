package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.exception.RegistrationFieldNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import spock.lang.Specification

class RegistrationValidatorSpec extends Specification {

    def "isStatusDefault should return #expectedResult for status '#status'"() {
        given:
        when:
        def isDefault=RegistrationValidator.isDefaultStatus(status)
        then:
        RegistrationValidator.isDefaultStatus(status) == expectedResult
        where:
        status         | expectedResult
        "ALL"          | true
        "all"          | true
        "default"      | true
        "DEFAULT"      | true
        "Default"      | true
        "none"         | false
        "something"    | false
        "Defaul"       | false
        null           | true
        ""             | true

    }

    def "validateRegistrationOverviewParams should throw errors when status or orderBy is not found"(){
        given:
            def search = ""
            def isAscending = true

        when:
            def params = RegistrationOverviewParams.builder()
                    .status(status)
                    .search(search)
                    .orderBy(orderBy)
                    .isAscending(isAscending)
                    .build()
            RegistrationValidator.validateRegistrationOverviewParams(params)

        then:
            thrown(expectedException)

        where:
            status            | orderBy           | expectedException
            "NotFoundStatus"  | ""                | RegistrationStatusNotFoundException
            ""                | "NotFoundOrderBy" | RegistrationFieldNotFoundException
    }
}
