package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.AvailableCourseRequest
import com.mgmtp.cfu.dto.CourseDto
import com.mgmtp.cfu.dto.CoursePageDTO
import com.mgmtp.cfu.controller.CourseController
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.CoursePageUtil

import com.mgmtp.cfu.dto.CourseRequest
import com.mgmtp.cfu.dto.CourseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CourseControllerSpec extends Specification {
    private CourseService courseServiceMock = Mock(CourseService)

    @Subject
    private CourseController courseController = new CourseController(courseServiceMock)

    def "Get available courses with valid page size returns OK response"() {
        given:
        def request = new AvailableCourseRequest(page: page, pageSize: pageSize, sortBy: sortBy)
        CoursePageDTO coursesPage = new CoursePageDTO() // Dữ liệu mẫu trả về từ service
        courseServiceMock.getAvailableCoursesPage(request.getPage(), request.getPageSize(), request.getSortBy()) >> coursesPage

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(request)

        then:
        1 * courseServiceMock.getAvailableCoursesPage(request.getPage(), request.getPageSize(), request.getSortBy()) >> coursesPage
        response.getStatusCode().value() == 200
        response.getBody() == coursesPage

        where:
        page | pageSize | sortBy
        0 | 8 | "CREATED_DATE"
        1 | 8 | "CREATED_DATE"
        2 | 12 | "ENROLLMENTS"
        -1 | 8 | "ENROLLMENTS"
        999 | 16 | "ENROLLMENTS"
    }

    def "Throw exception when sort criteria is not in the list"() {
        given:
        def page = 1
        def pageSize = 8
        def sortBy = "INVALID_SORT_BY"

        when:
        def request = new AvailableCourseRequest(page: page, pageSize: pageSize, sortBy: sortBy)

        then:
        thrown(IllegalArgumentException)
    }

    def "Get available courses with invalid page size returns BAD_REQUEST response"() {
        given:
        def request = new AvailableCourseRequest(page: 1, pageSize: 0)

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(request)

        then:
        response.getStatusCode().value() == 422
        response.getBody() == "Invalid page size. Page size must be between 1 and ${CoursePageUtil.getMaxPageSize()}"
    }

    def "Get courseDto by Id if course exists"() {
        given:
        // courseService.getCourseDtoById(_) >> CourseDto.builder().name("Tim Buchalka").build()
        def courseId = 1
        CourseDto courseDto = courseServiceMock.getCourseDtoById(courseId)

        when:
        ResponseEntity<CourseDto> result = courseController.getCourseDtoById(courseId)

        then:
        result.getBody() == courseDto
    }

    def "Get courseDto by Id if course is not exist"() {
        given:
        def courseId = 100
        courseServiceMock.getCourseDtoById(courseId) >> null

        when:
        ResponseEntity<CourseDto> result = courseController.getCourseDtoById(courseId)

        then:
        result.getBody() == null
    }

    def "should return created status and course response when service creates course successfully"() {
        given:
        def courseRequest = new CourseRequest(/* initialize with required properties */)
        def courseResponse = new CourseResponse(/* initialize with required properties */)
        courseServiceMock.createCourse(courseRequest) >> courseResponse

        when:
        def responseEntity = courseController.createCourse(courseRequest)

        then:
        responseEntity.statusCode == HttpStatus.CREATED
        responseEntity.body == courseResponse
    }

    def "should return internal server error when service throws exception"() {
        given:
        def courseRequest = new CourseRequest(/* initialize with required properties */)
        courseServiceMock.createCourse(courseRequest) >> { throw new RuntimeException("Service exception") }

        when:
        def responseEntity = courseController.createCourse(courseRequest)

        then:
        responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        responseEntity.body == null
    }
}