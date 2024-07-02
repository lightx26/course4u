package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.CourseOverviewDTO
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.service.RegistrationService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class CourseMapperSpec extends Specification {
    RegistrationService registrationServiceMock = Mock(RegistrationService)

    @Subject
    CourseMapper courseMapper = new CourseMapper(registrationServiceMock)

    def "Should return a null object"() {
        given:
        Course courseEntity = null

        when:
        CourseOverviewDTO courseDTO = courseMapper.toOverviewDTO(courseEntity)

        then:
        courseDTO == null
    }

    def "Return a DTO mapped from an entity"() {
        given:
        Course courseEntity = new Course(1, "Test Course", "Test Link", "Test Platform", CourseLevel.ADVANCED, "Test Thumbnail URL", "Test Teacher's name", LocalDate.of(2024, 1, 1), CourseStatus.AVAILABLE, null)
        courseEntity.id = 1
        int enrollmentCount = 15
        registrationServiceMock.countLegitRegistrationInCourse(courseEntity.id) >> enrollmentCount

        when:
        CourseOverviewDTO courseDTO = courseMapper.toOverviewDTO(courseEntity);

        then:
        1 * registrationServiceMock.countLegitRegistrationInCourse(courseEntity.id) >> enrollmentCount
        courseDTO.id == courseEntity.id
        courseDTO.name == courseEntity.name
        courseDTO.platform == courseEntity.platform
        courseDTO.level == courseEntity.level
        courseDTO.thumbnailUrl == courseEntity.thumbnailUrl
        courseDTO.createdDate == courseEntity.createdDate
        courseDTO.status == courseEntity.status
        courseDTO.enrollmentCount == enrollmentCount
    }
}