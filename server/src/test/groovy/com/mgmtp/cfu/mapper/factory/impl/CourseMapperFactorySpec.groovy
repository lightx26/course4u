package com.mgmtp.cfu.mapper.factory.impl

import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.mapper.DTOMapper
import com.mgmtp.cfu.mapper.EntityMapper
import spock.lang.Specification

class CourseMapperFactorySpec extends Specification {
    def "Constructor should create a map"() {
        given:
        def dtoMapper = new MockCourseDTOMapper()
        def dtoMapperSet = [dtoMapper] as Set<DTOMapper<Object, Course>>

        def entityMapper = new MockCourseEntityMapper()
        def entityMapperSet = [entityMapper] as Set<EntityMapper<Object, Course>>

        when:
        def result = new CourseMapperFactory(dtoMapperSet, entityMapperSet)

        then:
        result._dtoMappers.size() == 1
        result._dtoMappers.get(Object.class) == dtoMapper
        result._entityMappers.size() == 1
        result._entityMappers.get(Object.class) == entityMapper
    }
}

class MockCourseDTOMapper implements DTOMapper<Object, Course> {
    @Override
    Object toDTO(Course entity) {
        return null
    }
}

class MockCourseEntityMapper implements EntityMapper<Object, Course> {
    @Override
    Course toEntity(Object dto) {
        return null
    }
}
