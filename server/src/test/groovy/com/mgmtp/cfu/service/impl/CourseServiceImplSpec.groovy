package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.coursedto.CourseOverviewDTO
import com.mgmtp.cfu.dto.coursedto.CoursePageDTO
import com.mgmtp.cfu.dto.coursedto.CoursePageFilter
import com.mgmtp.cfu.dto.coursedto.CourseRequest
import com.mgmtp.cfu.dto.coursedto.CourseResponse
import com.mgmtp.cfu.dto.coursedto.CourseSearchRequest
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CoursePageSortOption
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.exception.CourseNotFoundException
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.exception.ServerErrorRuntimeException
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.mapper.factory.MapperFactory
import com.mgmtp.cfu.mapper.factory.impl.CourseMapperFactory
import com.mgmtp.cfu.mapper.CourseOverviewMapper
import com.mgmtp.cfu.repository.CourseRepository
import com.mgmtp.cfu.service.CategoryService
import com.mgmtp.cfu.service.CourseService
import com.mgmtp.cfu.service.UploadService
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.dao.CannotAcquireLockException
import org.springframework.data.domain.PageImpl
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
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
    CategoryService categoryService = Mock()
    UploadService uploadService = Mock()
    @Subject
    CourseService courseService = new CourseServiceImpl(courseRepository, courseMapperFactory, categoryService, uploadService)


    def setup() {
        AuthUtils.getCurrentUser() >> new User()
    }
    def "test createCourse with thumbnail file"() {
        given:
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(1).role(Role.USER).build()
        }
        SecurityContextHolder.context.authentication = authentication
        MultipartFile thumbnailFile = Mock(MultipartFile)
        thumbnailFile.getOriginalFilename() >> "abc.jpg"

        CourseRequest.CategoryCourseRequestDTO javaCategory = new CourseRequest.CategoryCourseRequestDTO()
        javaCategory.setLabel("Java")
        javaCategory.setValue("1")

        CourseRequest courseRequest = CourseRequest.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform(CoursePlatform.UDEMY)
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
                .platform(CoursePlatform.UDEMY)
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .teacherName("John Doe")
                .createdDate(LocalDate.now())
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories(Set.of(mockCategory))
                .build()

        categoryService.findOrCreateNewCategory(_) >> List.of(mockCategory)
        courseRepository.save(_) >> course
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(_, _) >> Optional.empty()
        when:
        CourseResponse courseResponse = courseService.createCourse(courseRequest)

        then:
        courseResponse != null
        courseResponse.name == courseRequest.name
        courseResponse.link == courseRequest.link
        courseResponse.platform == courseRequest.platform.name()
        courseResponse.teacherName == courseRequest.teacherName
        courseResponse.status == courseRequest.status
        courseResponse.level == courseRequest.level
    }



    def "test createCourse with thumbnail URL"() {
        given:
        def authentication = Mock(Authentication) {
            getCredentials() >> User.builder().id(1).role(Role.USER).build()
        }
        SecurityContextHolder.context.authentication = authentication
        CourseRequest.CategoryCourseRequestDTO javaCategory = new CourseRequest.CategoryCourseRequestDTO()
        javaCategory.setLabel("Java")
        javaCategory.setValue("1")
        CourseRequest courseRequest = CourseRequest.builder()
                .name("Java Programming")
                .link("https://example.com/java-course")
                .platform(CoursePlatform.UDEMY)
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
                .platform(CoursePlatform.UDEMY)
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .teacherName("John Doe")
                .createdDate(LocalDate.now())
                .status(CourseStatus.PENDING)
                .level(CourseLevel.INTERMEDIATE)
                .categories(Set.of(mockCategory))
                .build()
        categoryService.findOrCreateNewCategory(_) >> List.of(mockCategory)
        courseRepository.save(_) >> course
        courseRepository.findFirstByLinkIgnoreCaseAndStatus(_, _) >> Optional.empty()
        when:
        CourseResponse courseResponse = courseService.createCourse(courseRequest)

        then:
        courseResponse != null
        courseResponse.name == courseRequest.name
        courseResponse.link == courseRequest.link
        courseResponse.platform == courseRequest.platform.name()
        courseResponse.teacherName == courseRequest.teacherName
        courseResponse.categories.size() > 0
        // Verify interactions
    }

    def "Should throw an Exception when Mapper not found"() {
        given:
        int pageNo = 1
        int pageSize = 5
        String search = "java"
        CoursePageSortOption sortOption = CoursePageSortOption.NEWEST
        CoursePageFilter filter = new CoursePageFilter()
        CourseSearchRequest searchRequest = new CourseSearchRequest(page: pageNo, pageSize: pageSize, search: search, sortBy: sortOption, filter: filter)
        List<Course> courses = createCourses(5)

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.empty()

        when:
        CoursePageDTO result = courseService.getAvailableCoursesPage(searchRequest)

        then:
        thrown(MapperNotFoundException)
    }

    def "Should return the first page when pageNo is too low"() {
        given:
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.MOST_ENROLLED
        CoursePageFilter filter = new CoursePageFilter(categoryFilters: [1, 2, 3])
        CourseSearchRequest searchRequest = new CourseSearchRequest(page: pageNo, pageSize: pageSize, search: search, sortBy: sortOption, filter: filter)
        List<Course> courses = createCourses(10)

        Page mockCoursePage = new PageImpl<>(courses.subList(0, 5), PageRequest.of(0, pageSize), courses.size())

        courseRepository.findAll(_, _) >> mockCoursePage

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(0, 5).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()

        when:
        def result = courseService.getAvailableCoursesPage(searchRequest)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 10

        where:
        pageNo << [-10, -1, 0, 1]
        search << ["", "Java", "  Python  ", "   "]
    }

    def "Should return a full page when pageNo and pageSize are reasonable"() {
        given:
        int pageNo = 2
        int pageSize = 5
        CoursePageSortOption sortOption = CoursePageSortOption.RATING
        CourseSearchRequest searchRequest = new CourseSearchRequest(page: pageNo, pageSize: pageSize, search: search, sortBy: sortOption, filter: filter)
        List<Course> courses = createCourses(15)

        Page mockCoursePage = new PageImpl<>(courses.subList(5, 10), PageRequest.of(1, pageSize), courses.size())


        courseRepository.findAll(_, _) >> mockCoursePage

        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)

        courses.subList(5, 10).each { course ->
            courseOverviewMapper.toDTO(course) >> new CourseOverviewDTO(id: course.id)
        }

        List courseOverviewDTOs = mockCoursePage.map(courseOverviewMapper::toDTO).getContent()


        when:
        def result = courseService.getAvailableCoursesPage(searchRequest)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 3
        result.totalElements == 15

        where:
        filter << [new CoursePageFilter(),
                   new CoursePageFilter(categoryFilters: [1, 2, 3],
                           levelFilters: [CourseLevel.BEGINNER, CourseLevel.INTERMEDIATE],
                           ratingFilters: [3],
                           platformFilters: ["UDEMY"]),
                   new CoursePageFilter(categoryFilters: [], levelFilters: [CourseLevel.ADVANCED], ratingFilters: [], platformFilters: []),
                   new CoursePageFilter()]
        search << ["", "Java", "  PYTHON  ", "   "]
    }

    def "Should return the last page when pageNo is too high"() {
        given:
        int pageNo = 99
        int pageSize = 5
        String search = "123"
        CoursePageFilter filter = new CoursePageFilter()
        CoursePageSortOption sortOption = CoursePageSortOption.NEWEST
        CourseSearchRequest searchRequest = new CourseSearchRequest(page: pageNo, pageSize: pageSize, search: search, sortBy: sortOption, filter: filter)
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
        def result = courseService.getAvailableCoursesPage(searchRequest)

        then:
        result.content == courseOverviewDTOs
        result.totalPages == 2
        result.totalElements == 7
    }

    List<Course> createCourses(int numCourses) {
        return (1..numCourses).collect { new Course(id: it) }
    }


    // test for removing course:
    def "deleteCourseById return ok"() {
        given:
        courseRepository.existsById(_ as Long) >> true;
        courseRepository.findById(_) >> Optional.of(Course.builder().id(1).registrations(new HashSet<Registration>()).build())
        courseRepository.deleteById(_) >> { }
        when:
        courseService.deleteCourseById(1)
        then:
        1*courseRepository.existsById(_ as Long) >> true;
    }

    def "deleteCourseById: course don't exist"() {
        given:
        courseRepository.existsById(_ as Long) >> false
        when:
        courseService.deleteCourseById(1)
        then:
        def ex = thrown(BadRequestRuntimeException)
        ex.getMessage() == "Course don't exist."
    }

    def "deleteCourseById: course can't be delete"() {
        given:
        courseRepository.existsById(_ as Long) >> true;
        courseRepository.findById(_ as Long) >> Optional.of(Course.builder().id(1).registrations(new HashSet<Registration>(Arrays.asList(Registration.builder().id(1).build()))).build())
        when:
        courseService.deleteCourseById(1)
        then:
        def ex = thrown(BadRequestRuntimeException)

        ex.getMessage() == "Course can't be removed. It was registered by someone."
    }

    def "deleteCourseById: database arisen error"() {
        given:
        courseRepository.existsById(_ as Long) >> { throw new CannotAcquireLockException("") }
        when:
        courseService.deleteCourseById(1)
        then:
        def ex = thrown(ServerErrorRuntimeException)
    }

    def "getRelatedCourses return ok"(){
        given:
        var categories=Set.of(Category.builder().name("ABC").id(1).build())
        courseRepository.findById(_)>>> Optional.of(Course.builder().categories(categories).id(1).status(CourseStatus.AVAILABLE).registrations(new HashSet<Registration>(Arrays.asList(Registration.builder().id(1).build()))).build())
        courseRepository.findTop8RelatedCourse(_,_,_,_)>> createAvailableCourses(7)
        courseMapperFactory.getDTOMapper(CourseOverviewDTO.class) >> Optional.of(courseOverviewMapper)
        courseOverviewMapper.toDTO(_) >> new CourseOverviewDTO(id: 1)

        when:
        def list=courseService.getRelatedCourses(1)
        then:
        list.size()==7
    }
    List<Course> createAvailableCourses(int numCourses) {
        var categories=Set.of(Category.builder().name("ABC").id(1).build())
        return (1..numCourses).collect { new Course(id: it, status: CourseStatus.AVAILABLE, categories: categories) }
    }


    def "getRelatedCourses return The course with ID \" + courseId + \" isn't found."(){
        given:
        courseRepository.findById(_)>>> Optional.empty()
        when:
        courseService.getRelatedCourses(1)
        then:
        thrown(CourseNotFoundException)
    }

}
