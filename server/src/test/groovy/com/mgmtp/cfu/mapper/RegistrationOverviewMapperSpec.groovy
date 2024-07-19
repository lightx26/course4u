package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.RegistrationStatus
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime

class RegistrationOverviewMapperSpec extends Specification {
    @Subject
    RegistrationOverviewMapper registrationOverviewMapper = Mappers.getMapper(RegistrationOverviewMapper.class)
    def "test ToDto"() {
        given:
        def platform = CoursePlatform.LINKEDIN
        def course = Course.builder()
                .id(1)
                .name("Java")
                .status(CourseStatus.AVAILABLE)
                .link("link")
                .platform(platform)
                .thumbnailUrl("thumbnail")
                .createdDate(LocalDate.now())
                .teacherName("Teacher Name")
                .build()
        def registration = Registration.builder()
                .id(1)
                .duration(15)
                .endDate(LocalDateTime.now())
                .registerDate(LocalDate.now())
                .startDate(LocalDateTime.now())
                .status(RegistrationStatus.DONE)
                .score(10)
                .course(course)
                .build()
        when:
        RegistrationOverviewDTO registrationDto = registrationOverviewMapper.toDTO(registration)
        then:
        registrationDto.endDate == registration.getEndDate().toLocalDate()
        registrationDto.registerDate == registration.registerDate
        registrationDto.startDate == registration.getStartDate().toLocalDate()
        registrationDto.status == registration.status
        registrationDto.courseId == registration.getCourse().getId()
    }
}
