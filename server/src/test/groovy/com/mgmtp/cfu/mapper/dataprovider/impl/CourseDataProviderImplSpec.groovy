package com.mgmtp.cfu.mapper.dataprovider.impl

import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.service.RegistrationService
import com.mgmtp.cfu.service.impl.RegistrationServiceImpl
import spock.lang.Specification
import spock.lang.Subject

class CourseDataProviderImplSpec extends Specification {
    RegistrationRepository registrationRepository = Mock(RegistrationRepository)

    @Subject
    CourseDataProvider courseDataProvider = new CourseDataProviderImpl(registrationRepository)

    def "should return number of legit registration in a course"() {
        given:
        int courseId = 1
        int enrollmentCount = 10
        registrationRepository.countLegitRegistrationInCourse(courseId) >> enrollmentCount

        when:
        int result = courseDataProvider.countLegitRegistrationInCourse(courseId)

        then:
        result == enrollmentCount
    }
}
