package com.mgmtp.cfu.mapper.factory.impl

import com.mgmtp.cfu.dto.categorydto.CategoryDTO
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.mapper.DTOMapper
import com.mgmtp.cfu.mapper.EntityMapper
import spock.lang.Specification

class CategoryMapperFactorySpec extends Specification {
    def "Constructor should create a map for mapper"() {
        def dtoMapper = new MockCategoryMapper()
        def dtoMapperSet = [dtoMapper] as Set<DTOMapper<Object, Course>>

        def entityMapper = new MockCategoryMapper()
        def entityMapperSet = [entityMapper] as Set<EntityMapper<Object, Course>>

        when:
        def result = new CategoryMapperFactory(dtoMapperSet, entityMapperSet)

        then:
        result._dtoMappers.size() == 1
        result._dtoMappers.get(Object.class) == dtoMapper
        result._entityMappers.size() == 1
        result._entityMappers.get(Object.class) == entityMapper
    }
}

class MockCategoryMapper implements DTOMapper<Object, Category>, EntityMapper<Object, Category> {
    @Override
    Object toDTO(Category entity) {
        return null;
    }

    @Override
    Category toEntity(Object dto) {
        return null;
    }
}
