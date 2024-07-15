package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.RegistrationStatus
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate


class RegistrationDetailMapperSpec extends Specification {
    @Subject
    RegistrationDetailMapper registrationDetailMapper = Mappers.getMapper(RegistrationDetailMapper.class)
    def "test ToDto"() {
        given:
        def course = Course.builder()
                .id(1)
                .name("Java")
                .status(CourseStatus.AVAILABLE)
                .link("link")
                .platform("platform")
                .thumbnailUrl("thumbnail")
                .createdDate(LocalDate.now())
                .teacherName("Teacher Name")
                .build()
        def registration = Registration.builder()
                .id(1)
                .duration(15)
                .endDate(LocalDate.now())
                .registerDate(LocalDate.now())
                .startDate(LocalDate.now())
                .status(RegistrationStatus.DONE)
                .score(10)
                .course(course)
                .build()
        when:
            RegistrationDetailDTO registrationDto = registrationDetailMapper.toDTO(registration)
        then:
            registrationDto.duration == registration.duration
            registrationDto.endDate == registration.endDate
            registrationDto.registerDate == registration.registerDate
            registrationDto.startDate == registration.startDate
            registrationDto.status == registration.status
            registrationDto.score == registration.score
            registrationDto.course.id == registration.getCourse().getId()
    }
}
