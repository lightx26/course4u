package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.categorydto.CategoryDTO
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.enums.CategoryStatus
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

class CategoryMapperSpec extends Specification {
    @Subject
    CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class)

    def "Should mapping all the fields of entity to DTO correctly"() {
        given:
        Category categoryEntity = new Category(id: 1, status: CategoryStatus.AVAILABLE, name: "Test Category")

        when:
        CategoryDTO categoryDTO = categoryMapper.toDTO(categoryEntity)

        then:
        categoryDTO.id == categoryEntity.id
        categoryDTO.name == categoryEntity.name
    }

    def "Should return null dto when entity is null"() {
        given:
        Category categoryEntity = null

        when:
        CategoryDTO categoryDTO = categoryMapper.toDTO(categoryEntity)

        then:
        categoryDTO == null
    }

    def "Should mapping all the fields of DTO to entity correctly"() {
        given:
        CategoryDTO categoryDTO = new CategoryDTO(id: 1, name: "Test Category")

        when:
        Category categoryEntity = categoryMapper.toEntity(categoryDTO)

        then:
        categoryEntity.id == categoryDTO.id
        categoryEntity.name == categoryDTO.name
        categoryEntity.status == null
        categoryEntity.courses == null
    }

    def "Should return null entity when DTO is null"() {
        given:
        CategoryDTO categoryDTO = null

        when:
        Category categoryEntity = categoryMapper.toEntity(categoryDTO)

        then:
        categoryEntity == null
    }
}
