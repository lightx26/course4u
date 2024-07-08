package com.mgmtp.cfu.util

import spock.lang.Specification
import com.mgmtp.cfu.util.RegistrationValidator

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
}
