package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.coursedto.CourseSearchRequest
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.dto.coursedto.CourseDto
import com.mgmtp.cfu.dto.coursedto.CourseOverviewDTO
import com.mgmtp.cfu.dto.coursedto.CoursePageFilter
import com.mgmtp.cfu.enums.CoursePageSortOption
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.util.CoursePageUtil

import com.mgmtp.cfu.dto.coursedto.CourseRequest
import com.mgmtp.cfu.dto.coursedto.CourseResponse
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class CourseControllerSpec extends Specification {
    private CourseService courseServiceMock = Mock(CourseService)

    @Subject
    private CourseController courseController = new CourseController(courseServiceMock)

    def "Get available courses with valid page size returns OK response"() {
        given:
        def filter = new CoursePageFilter()
        def request = new CourseSearchRequest(page: page, pageSize: pageSize, sortBy: sortBy, filter: filter, search: "")
        def coursesPage = new PageImpl([new CourseOverviewDTO()]) // Dữ liệu mẫu trả về từ service
        courseServiceMock.getAvailableCoursesPage(request) >> coursesPage

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(request)

        then:
        1 * courseServiceMock.getAvailableCoursesPage(request) >> coursesPage
        response.getStatusCode().value() == 200
        response.getBody() == coursesPage

        where:
        page | pageSize | sortBy
        0 | 8 | "NEWEST"
        1 | 8 | "NEWEST"
        2 | 12 | "MOST_ENROLLED"
        -1 | 8 | "MOST_ENROLLED"
        999 | 16 | "RATING"
    }

    def "Throw exception when sort criteria is not in the list"() {
        given:
        def page = 1
        def pageSize = 8
        def sortBy = "INVALID_SORT_BY"

        when:
        def request = new CourseSearchRequest(page: page, pageSize: pageSize, sortBy: sortBy)

        then:
        thrown(IllegalArgumentException)
    }

    def "Get available courses with invalid page size returns UNPROCESSABLE_ENTITY response"() {
        given:
        def request = new CourseSearchRequest(page: 1, pageSize: 0)

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(request)

        then:
        response.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY
        response.getBody() == "Invalid page size. Page size must be between 1 and ${CoursePageUtil.getMaxPageSize()}"
    }

    def "Return Internal Server Error when service thrown an exception"() {
        given:
        def filter = new CoursePageFilter()
        def request = new CourseSearchRequest(page: 1, pageSize: 8, sortBy: CoursePageSortOption.NEWEST, filter: filter, search: "")
        courseServiceMock.getAvailableCoursesPage(request) >> { throw new MapperNotFoundException() }

        when:
        ResponseEntity<?> response = courseController.getAvailableCourses(request)

        then:
        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
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

    def'deleteCourse: delete success'(){
        given:
        courseServiceMock.deleteCourseById(1L)>{}
        when:
        courseController.deleteCourse(1L)
        then:
        1*courseServiceMock.deleteCourseById(1L)
    }

    def'deleteCourse: Course id is null.'(){
        given:
        when:
        courseController.deleteCourse(null)
        then:
        def ex=thrown(BadRequestRuntimeException.class)
    }


    def'getRelatedCourses: Course id is null.'(){
        given:
        when:
        courseController.getRelatedCourses(null)
        then:
        def ex=thrown(BadRequestRunTimeException.class)
    }
    def'getRelatedCourses: return ok.'(){
        given:
        courseServiceMock.getRelatedCourses(_ as Long)>> List.of()
        when:
        def response =courseController.getRelatedCourses(1)
        then:
        response.statusCode.value()==200
    }
}