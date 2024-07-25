package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewParams
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper
import com.mgmtp.cfu.service.RegistrationService
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

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
        List<RegistrationOverviewDTO> result = RegistrationOverviewUtils.getRegistrationOverviewDTOS(1, myRegistrations, Optional.of(registrationOverviewMapper))

        then:
        result.size() == 4
    }
}