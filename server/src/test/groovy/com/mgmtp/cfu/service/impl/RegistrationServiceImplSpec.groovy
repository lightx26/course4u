package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.service.RegistrationService
import spock.lang.Specification
import spock.lang.Subject


class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock(RegistrationRepository)

    @Subject
    RegistrationService registrationService = new RegistrationServiceImpl(registrationRepository)

    def "should return number of legit registration in a course"() {
        given:
        int courseId = 1
        int enrollmentCount = 10
        registrationRepository.countLegitRegistrationInCourse(courseId) >> enrollmentCount

        when:
        int result = registrationService.countLegitRegistrationInCourse(courseId)

        then:
        result == enrollmentCount
    }
}
