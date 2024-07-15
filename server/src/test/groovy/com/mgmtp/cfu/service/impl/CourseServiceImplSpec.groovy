package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseOverviewDTO
import com.mgmtp.cfu.dto.coursedto.CoursePageDTO
import com.mgmtp.cfu.dto.coursedto.CourseRequest
import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.enums.CoursePageSortOption
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.CourseMapperFactory
import com.mgmtp.cfu.mapper.CourseOverviewMapper
import com.mgmtp.cfu.repository.CategoryRepository
import com.mgmtp.cfu.repository.CourseRepository
import com.mgmtp.cfu.service.CategoryService
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.service.UploadService
import org.springframework.data.domain.PageImpl
import org.modelmapper.ModelMapper
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

import java.time.LocalDate
import spock.lang.Subject

class CourseServiceImplSpec extends Specification {
    CourseRepository courseRepository = Mock(CourseRepository)
    MapperFactory<Course> courseMapperFactory = Mock(CourseMapperFactory)
    CourseOverviewMapper courseOverviewMapper = Mock(CourseOverviewMapper)
    CategoryService categorySerivce = Mock()
    UploadService uploadService = Mock()
    @Subject
    CourseService courseService = new CourseServiceImpl(courseRepository, courseMapperFactory, categorySerivce, uploadService)
    def "test createCourse with thumbnail file"() {
        given:
        MultipartFile thumbnailFile = Mock(MultipartFile)
        thumbnailFile.getOriginalFilename() >> "abc.jpg"

        CourseRequest.CategoryCourseRequestDTO javaCategory = new CourseRequest.CategoryCourseRequestDTO()
        javaCategory.setLabel("Java")
        javaCategory.setValue("1")

        CourseRequest courseRequest = CourseRequest.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform("Udemy")
                .teacherName("John Doe")
                .thumbnailFile(thumbnailFile)
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories([javaCategory] as List)
                .build()

        Category mockCategory = new Category(id: 1, name: "Java", status:"AVAILABLE")

        Course course = Course.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform("Udemy")
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .teacherName("John Doe")
                .createdDate(LocalDate.now())
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories(Set.of(mockCategory))
                .build()

        categorySerivce.findCategoriesByIds(_) >> List.of(mockCategory)
        courseRepository.save(_) >> course

        when:
        CourseResponse courseResponse = courseService.createCourse(courseRequest)

        then:
        courseResponse != null
        courseResponse.name == courseRequest.name
        courseResponse.link == courseRequest.link
        courseResponse.platform == courseRequest.platform
        courseResponse.teacherName == courseRequest.teacherName
        courseResponse.status == courseRequest.status
        courseResponse.level == courseRequest.level
    }



    def "test createCourse with thumbnail URL"() {
        given:
        CourseRequest.CategoryCourseRequestDTO javaCategory = new CourseRequest.CategoryCourseRequestDTO()
        javaCategory.setLabel("Java")
        javaCategory.setValue("1")
        CourseRequest courseRequest = CourseRequest.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform("Udemy")
                .teacherName("John Doe")
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories([javaCategory] as List)
                .build()
        Category mockCategory = new Category(id: 1, name: "Java", status:"AVAILABLE")
        Course course = Course.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform("Udemy")
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .teacherName("John Doe")
                .createdDate(LocalDate.now())
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories(Set.of(mockCategory))
                .build()
        categorySerivce.findCategoriesByIds(_) >> List.of(mockCategory)
        courseRepository.save(_) >> course

        when:
        CourseResponse courseResponse = courseService.createCourse(courseRequest)

        then:
        courseResponse != null
        courseResponse.name == courseRequest.name
        courseResponse.link == courseRequest.link
        courseResponse.platform == courseRequest.platform
        courseResponse.teacherName == courseRequest.teacherName
        courseResponse.categories.size() > 0
        // Verify interactions
    }

    def "Should throw an Exception when Mapper not found"() {
        given:
        int pageNo = 1
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.NEWEST
        List<Course> courses = createCourses(5)

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.empty()

        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(sortOption, pageNo, pageSize)

        then:
        thrown(MapperNotFoundException)
    }

    def "Should return the first page since pageNo is too low"() {
        given:
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.MOST_ENROLLED
        List<Course> courses = createCourses(10)

        Page mockCoursePage = new PageImpl<>(courses.subList(0, 5), PageRequest.of(0, pageSize), courses.size())

        courseRepository.findAll(_, _) >> mockCoursePage

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(0, 5).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()

        when:
        def result = courseService.getAvailableCoursesPage(sortOption, pageNo, pageSize)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 10

        where:
        pageNo << [-10, -1, 0, 1]
    }

    def "Should return a full page when pageNo and pageSize are reasonable"() {
        given:
        int pageNo = 2
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.RATING
        List<Course> courses = createCourses(15)

        Page mockCoursePage = new PageImpl<>(courses.subList(5, 10), PageRequest.of(1, pageSize), courses.size())


        courseRepository.findAll(_, _) >> mockCoursePage

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(5, 10).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()


        when:
        def result = courseService.getAvailableCoursesPage(sortOption, pageNo, pageSize)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 3
        result.totalElements == 15
    }

    def "Should return the last page when pageNo is too high"() {
        given:
        int pageNo = 99
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.NEWEST
        List<Course> courses = createCourses(7)
        int callCount = 0

        Page mockCoursePage = new PageImpl<>(courses.subList(5, 7), PageRequest.of(1, pageSize), courses.size())

        courseRepository.findAll(_, _) >> { _, Pageable pageable ->
            callCount++
            if (callCount == 1) {
                return new PageImpl<>(Collections.emptyList(), pageable, courses.size())
            } else {
                return mockCoursePage
            }
        }

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(5, 7).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()

        when:
        def result = courseService.getAvailableCoursesPage(sortOption, pageNo, pageSize)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 7
    }

    List<Course> createCourses(int numCourses) {
        return (1..numCourses).collect { new Course(id: it) }
    }
}
