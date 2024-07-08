package com.mgmtp.cfu.util

import com.mgmtp.cfu.util.LoginValidator
import spock.lang.Specification
import spock.lang.Subject

class CoursePageValidatorSpec extends Specification {

    @Subject
    LoginValidator.CoursePageValidator coursePageValidator = new LoginValidator.CoursePageValidator()

    def "should return true for valid pageSize"() {
        when:
        boolean result = LoginValidator.CoursePageValidator.isValidPageSize(pageSize)

        then:
        result == true

        where:
        pageSize << [1, 4, 10, 16, 32]
    }

    def "should return false for invalid pageSize"() {
        when:
        boolean result = LoginValidator.CoursePageValidator.isValidPageSize(pageSize)

        then:
        result == false

        where:
        pageSize << [-1, 0, 33, 100]
    }
}
