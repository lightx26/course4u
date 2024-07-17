package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.coursedto.CourseOverviewDTO
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

class CourseOverviewMapperSpec extends Specification {
    CourseDataProvider courseDataProvider = Mock(CourseDataProvider)

    @Subject
    CourseOverviewMapper courseMapper = Mappers.getMapper(CourseOverviewMapper.class)



    def "Should return a null object"() {
        given:
        courseMapper.courseDataProvider = courseDataProvider
        Course courseEntity = null

        when:
        CourseOverviewDTO courseDTO = courseMapper.toDTO(courseEntity)

        then:
        courseDTO == null
    }

    def "Should mapping all the fields from entity to DTO correctly"() {
        given:
        courseMapper.courseDataProvider = courseDataProvider
        int enrollmentCount = 5
        Course courseEntity = new Course(id:1, name:"Test Course", platform: CoursePlatform.UDEMY)
        courseDataProvider.countLegitRegistrationInCourse(courseEntity.id) >> enrollmentCount

        when:
        CourseOverviewDTO courseDTO = courseMapper.toDTO(courseEntity)

        then:
        courseDTO.id == courseEntity.id
        courseDTO.name == courseEntity.name
        courseDTO.platform == courseEntity.platform
        courseDTO.enrollmentCount == enrollmentCount
    }
}
