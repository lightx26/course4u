package com.mgmtp.cfu.utils

import spock.lang.Specification
import spock.lang.Subject

class CoursePageValidatorSpec extends Specification {

    @Subject
    CoursePageValidator coursePageValidator = new CoursePageValidator()

    def "should return true for valid pageSize"() {
        when:
        boolean result = CoursePageValidator.isValidPageSize(pageSize)

        then:
        result == true

        where:
        pageSize << [1, 4, 10, 16, 32]
    }

    def "should return false for invalid pageSize"() {
        when:
        boolean result = CoursePageValidator.isValidPageSize(pageSize)

        then:
        result == false

        where:
        pageSize << [-1, 0, 33, 100]
    }
}
