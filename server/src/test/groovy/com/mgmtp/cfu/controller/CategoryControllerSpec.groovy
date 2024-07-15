package com.mgmtp.cfu.controller


import com.mgmtp.cfu.dto.categorydto.CategoryDTO
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.service.CategoryService
import org.springframework.data.domain.PageImpl
import spock.lang.Specification
import spock.lang.Subject

class CategoryControllerSpec extends Specification {
    CategoryService categoryService = Mock(CategoryService)
    @Subject
    CategoryController categoryController = new CategoryController(categoryService)

    def "Should return all available categories"() {
        given:
        def category = new CategoryDTO(name: "category")
        def categories = [category] as List<CategoryDTO>
        categoryService.getAvailableCategories() >> categories

        when:
        def result = categoryController.getAvailableCategories()

        then:
        result.getStatusCode().value() == 200
        result.getBody() == categories
    }

    def "Should return Internal Server Error when getAvailableCategories throws an exception"() {
        given:
        categoryService.getAvailableCategories() >> { throw new MapperNotFoundException() }

        when:
        def result = categoryController.getAvailableCategories()

        then:
        result.getStatusCode().value() == 500
    }
}
