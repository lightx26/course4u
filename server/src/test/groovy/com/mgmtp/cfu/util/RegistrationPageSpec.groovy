package com.mgmtp.cfu.util


import com.mgmtp.cfu.dto.RegistrationPage
import com.mgmtp.cfu.entity.Registration
import spock.lang.Specification

class RegistrationPageSpec extends Specification {

    def "test RegistrationPage constructor with valid page and non-null registrations"() {
        given:
        int page = 2
        List<Registration> registrations = (1..15).collect { Mock(Registration) }

        when:
        def registrationPage = new RegistrationPage(page, registrations)

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

        when:
        def registrationPage = new RegistrationPage(page, registrations)

        then:
        registrationPage.page == 1
        registrationPage.limit == Constant.MY_REGISTRATION_PAGE_LIMIT
        registrationPage.registrations.size() == 15
        registrationPage.pageTotal == 2
    }

    def "test RegistrationPage constructor with null registrations"() {
        given:
        int page = 1

        when:
        def registrationPage = new RegistrationPage(page, null)

        then:
        registrationPage.page == page
        registrationPage.limit == Constant.MY_REGISTRATION_PAGE_LIMIT
        registrationPage.registrations.size() == 0
        registrationPage.pageTotal == 0
    }
}
