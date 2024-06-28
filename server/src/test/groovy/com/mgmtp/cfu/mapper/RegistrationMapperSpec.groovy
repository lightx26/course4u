package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.RegistrationDTO
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.RegistrationStatus
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate


class RegistrationMapperSpec extends Specification {
    @Subject
    RegistrationMapper registrationMapper = new RegistrationMapper()
    def "test ToDto"() {
        given:
        def registration = Registration.builder()
                .duration(15)
                .endDate(LocalDate.now())
                .registerDate(LocalDate.now())
                .startDate(LocalDate.now())
                .status(RegistrationStatus.DONE)
                .score(10)
                .build()
        when:
            RegistrationDTO registrationDto = registrationMapper.toDto(registration)
        then:
            registrationDto.duration == registration.duration
            registrationDto.endDate == registration.endDate
            registrationDto.registerDate == registration.registerDate
            registrationDto.startDate == registration.startDate
            registrationDto.status == registration.status
            registrationDto.score == registration.score
    }
}
