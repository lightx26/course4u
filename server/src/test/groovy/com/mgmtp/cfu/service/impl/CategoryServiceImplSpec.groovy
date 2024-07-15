package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.categorydto.CategoryDTO
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.enums.CategoryStatus
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.mapper.CategoryMapper
import com.mgmtp.cfu.mapper.factory.impl.CategoryMapperFactory
import com.mgmtp.cfu.repository.CategoryRepository
import com.mgmtp.cfu.service.CategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Collectors

class CategoryServiceImplSpec extends Specification {
    CategoryRepository categoryRepository = Mock(CategoryRepository)
    CategoryMapperFactory categoryMapperFactory = Mock(CategoryMapperFactory)
    CategoryMapper categoryMapper = Mock(CategoryMapper)

    @Subject
    CategoryService categoryService = new CategoryServiceImpl(categoryRepository, categoryMapperFactory)


    def "Should return all available categories"() {
        given:
        List<Category> categories = createCategories(15)

        categoryRepository.findCategoriesByStatus(CategoryStatus.AVAILABLE) >> categories

        categoryMapperFactory.getDTOMapper(CategoryDTO.class) >> Optional.of(categoryMapper)


        def categoryDTO = categories.stream().map(categoryMapper::toDTO).collect(Collectors.toList())

        when:
        def result = categoryService.getAvailableCategories()

        then:
        result == categoryDTO
    }

    def "Should throw an exception when mapper is not found"() {
        given:
        categoryMapperFactory.getDTOMapper(CategoryDTO.class) >> Optional.empty()

        when:
        categoryService.getAvailableCategories()

        then:
        thrown MapperNotFoundException
    }

    List<Category> createCategories(int numCategories) {
        return (1..numCategories).collect { new Category(id: it) }
    }
}
