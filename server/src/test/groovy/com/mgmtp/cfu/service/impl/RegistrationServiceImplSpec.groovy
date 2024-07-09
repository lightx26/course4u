package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.exception.RegistrationNotFoundException
import com.mgmtp.cfu.mapper.RegistrationDetailMapper
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.RegistrationMapperFactory
import com.mgmtp.cfu.repository.RegistrationRepository
import spock.lang.Specification
import spock.lang.Subject


class RegistrationServiceImplSpec extends Specification {

    RegistrationRepository registrationRepository = Mock(RegistrationRepository)
    MapperFactory<Registration> registrationMapperFactory = Mock(RegistrationMapperFactory)
    RegistrationDetailMapper registrationDetailMapper = Mock(RegistrationDetailMapper)
    @Subject
    RegistrationServiceImpl registrationService = new RegistrationServiceImpl(registrationRepository, registrationMapperFactory)

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
            def ex = thrown(IllegalStateException)
            ex.message == "No mapper found for registrationDtoMapperOpt"
    }
}
