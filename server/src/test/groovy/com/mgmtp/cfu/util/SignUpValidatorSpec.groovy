package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.SignUpRequest
import spock.lang.Specification

class SignUpValidatorSpec extends Specification {
    def "ValidateSignUpRequest returns errors for invalid input"() {
        given:
        def signUpRequest = new SignUpRequest(username, email, password, confirmPassword, fullname, dateOfBirth, null)

        when:
        def errors = SignUpValidator.validateSignUpRequest(signUpRequest)

        then:
        errors.size() == expectedErrorCount
        errors.containsAll(expectedErrors)

        where:
        username     | email              | password     | confirmPassword | fullname       | dateOfBirth    | expectedErrorCount | expectedErrors
        null         | null               | null         | null            | null           | null           | 3                  | ["Username is required.", "Email is required.", "Password and confirm password are required."]
        "user"       | "user@mgm-tp.com"  | "pass"       | "pass"          | "Fullname"     | null           | 4                  | ["Password must be at least 8 characters.","Password must contain at least one uppercase letter.","Password must contain at least one number.","Password must contain at least one special character."]
        "username"   | "user@mgm-tp.com"  | "Password2!" | "Password1!"    | "Fullname"     | null           | 1                  | ["Password and confirm password must be the same."]
        "username"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | null           | 0                  | []
        "username!"  | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | null           | 1                  | ["Username cannot have special characters."]
        "username"   | "user@invalid.com" | "Password1!" | "Password1!"    | "Fullname"     | null           | 1                  | ["Please enter a valid email address."]
        "usernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusernameusername"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | null           | 1                  | ["Max length for username is 50 characters."]
        "username"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | "01/01/2025"    | 1                  | ["Date of birth cannot be a future date."]
        "username"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | "01/01/2000"    | 0                  | []
        "username"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | "invalid-date" | 1                  | ["Invalid date of birth format. Use dd/MM/yyyy."]
        "username"   | "user@mgm-tp.com"  | "Password1!" | "Password1!"    | "Fullname"     | "" |      0             | []
    }
}
