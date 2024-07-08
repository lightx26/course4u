package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.RegistrationOverviewDTO
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.RegistrationStatus
import spock.lang.Specification
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper

import java.time.LocalDate

class RegistrationOverviewUtilsSpec extends Specification {
    def registrationOverviewMapper = Mock(RegistrationOverviewMapper)

    def "test getRegistrationOverviewDTOS with page #page and registrations #registrations"() {
        given:
        List<Registration> myRegistrations = List.of(
                Registration.builder().id(1).registerDate(LocalDate.now()).build(),
                Registration.builder().id(1).registerDate(LocalDate.now()).build(),
                Registration.builder().id(1).registerDate(LocalDate.now()).build(),
                Registration.builder().id(1).registerDate(LocalDate.now()).build()

        )
        registrationOverviewMapper.toDTO(_) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        when:
        List<RegistrationOverviewDTO> result = RegistrationOverviewUtils.getRegistrationOverviewDTOS(1, myRegistrations, registrationOverviewMapper)

        then:
        result.size() == 4
    }
}