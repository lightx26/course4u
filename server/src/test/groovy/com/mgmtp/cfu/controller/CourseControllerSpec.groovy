package com.mgmtp.cfu.controller


import com.mgmtp.cfu.dto.CoursePageDTO
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.CoursePageValidator
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class CourseControllerSpec extends Specification {
    private CourseService courseServiceMock = Mock(CourseService)

    @Subject
    private CourseController courseController = new CourseController(courseServiceMock)

    def "Get available courses with valid page size returns OK response"() {
        given:
        CoursePageDTO coursesPage = new CoursePageDTO() // Dữ liệu mẫu trả về từ service
        courseServiceMock.getAvailableCoursesPage(page, pageSize) >> coursesPage

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(page, pageSize)

        then:
        1 * courseServiceMock.getAvailableCoursesPage(page, pageSize) >> coursesPage
        response.getStatusCode().value() == 200
        response.getBody() == coursesPage

        where:
        page | pageSize
        0 | 8
        1 | 8
        2 | 12
        -1 | 8
        999 | 16
    }

    def "Get available courses with invalid page size returns BAD_REQUEST response"() {
        given:
        def page = 1
        def pageSize = 0
        CoursePageDTO coursesPage = new CoursePageDTO() // Dữ liệu mẫu trả về từ service
        courseServiceMock.getAvailableCoursesPage(page, pageSize) >> coursesPage

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(page, pageSize)

        then:
        0 * courseServiceMock.getAvailableCoursesPage(page, pageSize) >> coursesPage
        response.getStatusCode().value() == 422
        response.getBody() == "Invalid page size. Page size must be between 1 and ${CoursePageValidator.getMaxPageSize()}"
    }
}