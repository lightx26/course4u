package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.CourseOverviewDTO
import com.mgmtp.cfu.dto.CoursePageDTO
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.CourseMapperFactory
import com.mgmtp.cfu.mapper.CourseOverviewMapper
import com.mgmtp.cfu.repository.CourseRepository
import com.mgmtp.cfu.service.CourseService
import org.springframework.data.domain.PageImpl
import spock.lang.Specification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

import spock.lang.Subject

class CourseServiceImplSpec extends Specification {
    CourseRepository courseRepository = Mock(CourseRepository)
    MapperFactory<Course> mapperFactory = Mock(CourseMapperFactory)
    CourseOverviewMapper courseOverviewMapper = Mock(CourseOverviewMapper)

    @Subject
    CourseService courseService = new CourseServiceImpl(courseRepository, mapperFactory)

    def "Should throw an Exception when Mapper not found"() {
        given:
        int pageNo = 1
        int pageSize = 5
        List<Course> courses = createCourses(5)

        Page mockCoursePage = new PageImpl<>(courses, PageRequest.of(0, pageSize), courses.size())

        courseRepository.findByStatus(CourseStatus.AVAILABLE, _) >> mockCoursePage

        mapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.empty()

        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(pageNo, pageSize)

        then:
        thrown(IllegalStateException)
    }

    def "Should return the first page since pageNo is too low"() {
        given:
        int pageSize = 5
        List<Course> courses = createCourses(10)

        Page mockCoursePage = new PageImpl<>(courses.subList(0, 5), PageRequest.of(0, pageSize), courses.size())

        courseRepository.findByStatus(CourseStatus.AVAILABLE, _) >> mockCoursePage

        mapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(0, 5).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()

        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(pageNo, pageSize)

        then:
        result.courses == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 10

        where:
        pageNo << [-10, -1, 0, 1]
    }

    def "Should return a full page when pageNo and pageSize are reasonable"() {
        given:
        int pageNo = 2
        int pageSize = 5
        List<Course> courses = createCourses(15)

        Page mockCoursePage = new PageImpl<>(courses.subList(5, 10), PageRequest.of(1, pageSize), courses.size())


        courseRepository.findByStatus(CourseStatus.AVAILABLE, _) >> mockCoursePage

        mapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(5, 10).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()


        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(pageNo, pageSize)

        then:
        result.courses == courseOverviewDTOs
        result.totalPages == 3
        result.totalElements == 15
    }

    def "Should return the last page when pageNo is too high"() {
        given:
        int pageNo = 99
        int pageSize = 5
        List<Course> courses = createCourses(7)
        int callCount = 0

        Page mockCoursePage = new PageImpl<>(courses.subList(5, 7), PageRequest.of(1, pageSize), courses.size())

        courseRepository.findByStatus(CourseStatus.AVAILABLE, _) >> { CourseStatus status, Pageable pageable ->
            callCount++
            if (callCount == 1) {
                return new PageImpl<>(Collections.emptyList(), pageable, courses.size())
            } else {
                return mockCoursePage
            }
        }

        mapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(5, 7).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()

        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(pageNo, pageSize)

        then:
        result.courses == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 7
    }

    List<Course> createCourses(int numCourses) {
        return (1..numCourses).collect { new Course(id: it) }
    }
}
