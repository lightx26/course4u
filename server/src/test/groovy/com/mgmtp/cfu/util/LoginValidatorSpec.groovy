package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.LoginRequest
import spock.lang.Specification

class LoginValidatorSpec extends Specification {
    def "IsValid"() {
        given:
        when:
        def isValidRequest = LoginValidator.isValid(new LoginRequest(username, password))
        then:
        isValidRequest == expectResult
        where:
        username    | password    | expectResult
        "username " | "password"  | true
        null        | "password " | false
        "username"  | null        | false
        null        | null        | false
        " "         | " "         | false
        "username"  | " "         | false
    }
}
