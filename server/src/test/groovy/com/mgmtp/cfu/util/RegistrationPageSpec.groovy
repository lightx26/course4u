package com.mgmtp.cfu.util

import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewPage
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper
import spock.lang.Specification

import java.time.LocalDate

class RegistrationOverviewPageSpec extends Specification {
    RegistrationOverviewMapper registrationOverviewMapper=Mock(RegistrationOverviewMapper)


    def "test RegistrationPage constructor with valid page and non-null registrations"() {
        given:
        int page = 2
        List<Registration> registrations = (1..15).collect { Mock(Registration) }
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        when:
        def registrationPage = new RegistrationOverviewPage(page, registrations,10,registrationOverviewMapper)

        then:
        registrationPage.page == page
        registrationPage.limit == Constant.MY_REGISTRATION_PAGE_LIMIT
        registrationPage.registrations.size() == 15
        registrationPage.pageTotal == 2
    }

    def "test RegistrationPage constructor with page less than or equal to 0"() {
        given:
        int page = 0
        List<Registration> registrations = (1..15).collect { Mock(Registration) }
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        when:
        def registrationPage = new RegistrationOverviewPage(page, registrations,10,registrationOverviewMapper)

        then:
        registrationPage.page == 1
        registrationPage.limit == Constant.MY_REGISTRATION_PAGE_LIMIT
        registrationPage.registrations.size() == 15
        registrationPage.pageTotal == 2
    }

    def "test RegistrationPage constructor with null registrations"() {
        given:
        int page = 1
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        when:
        def registrationPage = new RegistrationOverviewPage(page, null,10,registrationOverviewMapper)

        then:
        registrationPage.page == page
        registrationPage.limit == Constant.MY_REGISTRATION_PAGE_LIMIT
        registrationPage.registrations.size() == 0
        registrationPage.pageTotal == 0
    }
}
