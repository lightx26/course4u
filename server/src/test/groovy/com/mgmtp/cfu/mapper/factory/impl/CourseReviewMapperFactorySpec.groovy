package com.mgmtp.cfu.mapper.factory.impl

import com.mgmtp.cfu.entity.CourseReview
import com.mgmtp.cfu.mapper.DTOMapper
import com.mgmtp.cfu.mapper.EntityMapper
import spock.lang.Specification;

class CourseReviewMapperFactorySpec extends Specification {
    def "Constructor should create a map for mapper"() {
        given:
        def dtoMapper = new MockCourseReviewDTOMapper()
        def dtoMapperSet = [dtoMapper] as Set<DTOMapper<Object, CourseReview>>

        def entityMapper = new MockCourseReviewEntityMapper()
        def entityMapperSet = [entityMapper] as Set<EntityMapper<Object, CourseReview>>

        when:
        def result = new CourseReviewMapperFactory(dtoMapperSet, entityMapperSet)

        then:
        result._dtoMappers.size() == 1
        result._dtoMappers.get(Object.class) == dtoMapper
        result._entityMappers.size() == 1
        result._entityMappers.get(Object.class) == entityMapper
    }
}

class MockCourseReviewDTOMapper implements DTOMapper<Object, CourseReview> {
    @Override
    Object toDTO(CourseReview entity) {
        return null
    }
}

class MockCourseReviewEntityMapper implements EntityMapper<Object, CourseReview> {
    @Override
    CourseReview toEntity(Object dto) {
        return null
    }
}
