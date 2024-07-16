package com.mgmtp.cfu.service.impl


import com.mgmtp.cfu.dto.registrationdto.RegistrationOverviewDTO
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.exception.RegistrationStatusNotFoundException
import com.mgmtp.cfu.mapper.RegistrationDetailMapper
import com.mgmtp.cfu.mapper.RegistrationOverviewMapper
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.RegistrationMapperFactory
import com.mgmtp.cfu.repository.RegistrationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import java.time.LocalDate


class RegistrationServiceImplSpec extends Specification {
    RegistrationRepository registrationRepository = Mock(RegistrationRepository)
    MapperFactory<Registration> registrationMapperFactory = Mock(RegistrationMapperFactory)
    RegistrationDetailMapper registrationDetailMapper = Mock(RegistrationDetailMapper)
    RegistrationOverviewMapper registrationOverviewMapper=Mock(RegistrationOverviewMapper)

    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(registrationRepository, registrationOverviewMapper, registrationMapperFactory, registrationDetailMapper)

    def "return registration details successfully"() {
        given:
        Long id = 1L
        Registration registration = Registration.builder().id(id).build()
        RegistrationDetailDTO registrationDetailDTO = RegistrationDetailDTO.builder().id(id).build()

        registrationRepository.findById(id) >> Optional.of(registration)
        registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.of(registrationDetailMapper)
        registrationDetailMapper.toDTO(registration) >> registrationDetailDTO
        when:
        RegistrationDetailDTO result = registrationService.getDetailRegistration(id)
        then:
        result.id == registrationDetailDTO.id
    }

    def "return registration details failed"() {
        given:
            Long id = 999L
            registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.of(registrationDetailMapper)
            registrationRepository.findById(id) >> Optional.empty()
        when:
            registrationService.getDetailRegistration(id)
        then:
            def ex = thrown(RegistrationNotFoundException)
            ex.message == "Registration not found"
    }

    def "should return not found registration mapper"() {
        given:
            Long id = 1L
            registrationMapperFactory.getDTOMapper(RegistrationDetailDTO.class) >> Optional.empty()
        when:
            registrationService.getDetailRegistration(1L)
        then:
            def ex = thrown(MapperNotFoundException)
            ex.message == "No mapper found for registrationDtoMapperOpt"
    }

    def "test getMyRegistrationPage with default status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "DEFAULT"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
        when:
        def result = registrationService.getMyRegistrationPage(1, status)
        then:
        result.list.size() == 1
        result.totalElements == 1
    }

    def "test getMyRegistrationPage with specific status"() {
        given:
        def userId = 1
        def status = "APPROVED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations,registrayion2)
        registrationMapperFactory.getDTOMapper(_)>>Optional.of(registrationOverviewMapper)
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        when:
        def result = registrationService.getMyRegistrationPage(1, status)

        then:
        result.list.size() == 2
        result.list[0].status == RegistrationStatus.APPROVED
        result.totalElements == 2
    }

    def "test getMyRegistrationPage with invalid status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "APPROVaaaED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)

        when:
        registrationService.getMyRegistrationPage(0, status)

        then:
        thrown(IllegalArgumentException)
    }

    def "test getMyRegistrationPage with default status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "DEFAULT"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)
        when:
        def result = registrationService.getMyRegistrationPage(1, status)
        then:
        result.list.size() == 1
        result.totalElements == 1
    }

    def "test getMyRegistrationPage with specific status"() {
        given:
        def userId = 1
        def status = "APPROVED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        def registrayion2= Registration.builder().id(2).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()

        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations,registrayion2)
        registrationOverviewMapper.toDTO(_)>> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).registerDate(LocalDate.now()).startDate(LocalDate.now()).build()
        when:
        def result = registrationService.getMyRegistrationPage(1, status)

        then:
        result.list.size() == 2
        result.list[0].status == RegistrationStatus.APPROVED
        result.totalElements == 2
    }

    def "test getMyRegistrationPage with invalid status"() {
        given: "a mock user and registration data"
        def userId = 1
        def status = "APPROVaaaED"
        def registrations = Registration.builder().id(1).course(Course.builder().id(1).name("").build()).status(RegistrationStatus.APPROVED).startDate(LocalDate.now()).build()
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(userId).build()
        }
        SecurityContextHolder.context.authentication = authentication
        registrationRepository.getByUserId(userId,_) >> List.of(registrations)

        when:
        registrationService.getMyRegistrationPage(0, status)

        then:
        thrown(IllegalArgumentException)
    }


    /*
     * Test cases for getAllRegistrations and getRegistrationByStatus
     */

        registrationMapperFactory.getDTOMapper(_)>> Optional.of(registrationOverviewMapper)
    def "getAllRegistrations should return registration list"() {
        given:
            def page = 1
            def registration = Registration.builder().id(1).build()
            def registration2 = Registration.builder().id(2).build()
            registrationRepository.findAll(_) >> {args -> {
                def input = args[0] as PageRequest
                assert input.pageNumber == page - 1
                assert input.pageSize == 8
                Page<Registration> registrations = Mock(){
                    getContent() >> [registration, registration2]
                }
            }}
            registrationOverviewMapper.toDTO(registration) >> RegistrationOverviewDTO.builder().id(1).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getAllRegistrations(page)

        then:
            result.size() == 2
            result[0].id == 1
            result[1].id == 2
    }

    def "getRegistrationByStatus should return the correct registration list"(){
        given:
            def page = 1
            def status = "APPROVED"

            def registration = Registration.builder().id(1).status(RegistrationStatus.APPROVED).build()
            def registration2 = Registration.builder().id(2).status(RegistrationStatus.APPROVED).build()
            Page<Registration> registrations = Mock(){
                getContent() >> [registration, registration2]
            }

            registrationRepository.findAllByStatus(_, _) >> {
                args -> {
                    def pageRequest = args[1] as PageRequest

                    assert pageRequest.pageNumber == page - 1
                    assert pageRequest.pageSize == 8

                    registrations
                }
            }

            registrationOverviewMapper.toDTO(registration) >> RegistrationOverviewDTO.builder().id(1).status(RegistrationStatus.APPROVED).build()
            registrationOverviewMapper.toDTO(registration2) >> RegistrationOverviewDTO.builder().id(2).status(RegistrationStatus.APPROVED).build()

        when:
            Page<RegistrationOverviewDTO> result = registrationService.getRegistrationByStatus(page, status)

        then:
            result.size() == 2
            result[0].id == 1
            result[1].id == 2
    }

    def "getRegistrationByStatus with unavailable status should return an error message"(){
        given:
        def page = 1
        def status = "NOT_EXISTING_STATUS"

        when:
        registrationService.getRegistrationByStatus(page, status)

        then:
        def ex = thrown(RegistrationStatusNotFoundException)
        ex.message == "Status not found"
    }
}
