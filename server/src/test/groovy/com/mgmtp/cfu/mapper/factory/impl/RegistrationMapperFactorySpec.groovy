package com.mgmtp.cfu.mapper.factory.impl

import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.mapper.DTOMapper
import com.mgmtp.cfu.mapper.EntityMapper
import spock.lang.Specification

class RegistrationMapperFactorySpec extends Specification {
    def "Constructor should create a map"() {
        given:
        def dtoMapper = new MockRegistrationDTOMapper()
        def dtoMapperSet = [dtoMapper] as Set<DTOMapper<Object, Course>>

        def entityMapper = new MockRegistrationEntityMapper()
        def entityMapperSet = [entityMapper] as Set<EntityMapper<Object, Course>>

        when:
        def result = new RegistrationMapperFactory(dtoMapperSet, entityMapperSet)

        then:
        result._dtoMappers.size() == 1
        result._dtoMappers.get(Object.class) == dtoMapper
        result._entityMappers.size() == 1
        result._entityMappers.get(Object.class) == entityMapper
    }
}

class MockRegistrationDTOMapper implements DTOMapper<Object, Registration> {
    @Override
    Object toDTO(Registration entity) {
        return null
    }
}

class MockRegistrationEntityMapper implements EntityMapper<Object, Registration> {
    @Override
    Registration toEntity(Object dto) {
        return null
    }
}
