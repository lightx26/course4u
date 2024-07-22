package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.repository.CourseReviewRepository
import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewDto
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Gender
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.exception.CourseNotFoundException
import com.mgmtp.cfu.repository.CourseRepository
import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO
import com.mgmtp.cfu.entity.CourseReview
import com.mgmtp.cfu.exception.MapperNotFoundException
import com.mgmtp.cfu.mapper.CourseReviewOverviewMapper
import com.mgmtp.cfu.mapper.factory.impl.CourseReviewMapperFactory
import com.mgmtp.cfu.service.CourseReviewService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

class CourseReviewServiceImplSpec extends Specification {

    CourseReviewRepository courseReviewRepository = Mock()
    CourseReviewMapperFactory courseReviewMapperFactory = Mock()
    CourseReviewOverviewMapper mapper = Mock()
    CourseRepository courseRepository = Mock()

    @Subject
    CourseReviewService courseReviewService = new CourseReviewServiceImpl(courseReviewRepository, courseReviewMapperFactory, courseRepository)

    def setup() {
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        def user = User.builder().id(1)
                .fullName("Jane Doe")
                .username("janedoe")
                .email("janedoe@example.com")
                .role(Role.USER)
                .avatarUrl("/aksh/sdh")
                .gender(Gender.FEMALE)
                .telephone("008837623")
                .build()
        authentication.getCredentials() >> user
    }

    def "should return an initial rating page"() {
        given:
        def courseId = 1
        courseReviewRepository.calculateAvgRating(courseId) >> null

        when:
        def result = courseReviewService.getRatingsOfCourse(courseId)

        then:
        result.getAverageRating() == 0
        result.getDetailRatings().get(1) == 0L
        result.getDetailRatings().get(2) == 0L
        result.getDetailRatings().get(3) == 0L
        result.getDetailRatings().get(4) == 0L
        result.getDetailRatings().get(5) == 0L
    }

    def cleanup() {
        SecurityContextHolder.clearContext()
    }

    def "getRatingsPage returns ratings when ratings are present"() {
        given:
        Long courseId = 1L
        Double avgRating = 4.5
        def ratings = [
                new Object[]{1, 1L},
                new Object[]{2, 2L},
                new Object[]{3, 3L},
                new Object[]{4, 4L},
                new Object[]{5, 5L},

        ]
        courseReviewRepository.calculateAvgRating(courseId) >> avgRating
        courseReviewRepository.getRatingsInCourse(courseId) >> ratings

        when:
        def result = courseReviewService.getRatingsOfCourse(courseId)

        then:
        result.getAverageRating() == (double) 4.5
        result.getDetailRatings().get(1) == 1L
        result.getDetailRatings().get(2) == 2L
        result.getDetailRatings().get(3) == 3L
        result.getDetailRatings().get(4) == 4L
        result.getDetailRatings().get(5) == 5L
    }

    def "Should return a fully page of reviews when page is reasonable"() {
        given:
        def page = 2
        def courseId = 1
        def size = 5
        def reviews = createReviews(20)

        Page mockReviewsPage = new PageImpl<>(reviews.subList(5, 10), PageRequest.of(0, size), reviews.size())

        courseReviewRepository.findAll(_, _) >> mockReviewsPage

        courseReviewMapperFactory.getDTOMapper(CourseReviewOverviewDTO.class) >> Optional.of(mapper)

        reviews.subList(0, 5).each { course ->
            mapper.toDTO(course) >> new CourseReviewOverviewDTO(userFullName: "User " + course.id, comment: "Comment " + course.id)
        }

        List courseReviewOverviewDTOs = mockReviewsPage.map(mapper::toDTO).getContent()

        when:
        def result = courseReviewService.getReviewsPageOfCourse(courseId, starFilter, page, size)

        then:
        result.content == courseReviewOverviewDTOs
        result.totalPages == 4
        result.totalElements == 20

        where:
        starFilter << [null, 5, 4, 0]
    }

    def "saveFeedback save review"() {
        given:
        CourseReviewDto courseReviewDto = new CourseReviewDto(2, "Good", 2)
        Course course = new Course(id: 2)
        courseRepository.findById(_) >> Optional.of(course)

        when:
        courseReviewService.saveReview(courseReviewDto)

        then:
        1 * courseReviewRepository.save(_)
    }

    def "saveFeedback throws exception when course is not found"() {
        given:
        CourseReviewDto courseReviewDto = new CourseReviewDto(2, "Good", 1000)
        courseRepository.findById(_) >> Optional.empty()

        when:
        courseReviewService.saveReview(courseReviewDto)

        then:
        thrown(CourseNotFoundException)
    }

    def "Should return the last page of reviews when page is bigger than number of total pages"() {
        given:
        def courseId = 1
        def starFilter = 3
        def page = 99
        def size = 5
        def courses = createReviews(7)

        int callCount = 0

        Page mockReviewsPage = new PageImpl<>(courses.subList(5, 7), PageRequest.of(1, size), courses.size())

        courseReviewRepository.findAll(_, _) >> { _, Pageable pageable ->
            callCount++
            if (callCount == 1) {
                return new PageImpl<>(Collections.emptyList(), pageable, courses.size())
            } else {
                return mockReviewsPage
            }
        }

        courseReviewMapperFactory.getDTOMapper(CourseReviewOverviewDTO.class) >> Optional.of(mapper)

        courses.subList(5, 7).each { course ->
            mapper.toDTO(course) >> new CourseReviewOverviewDTO(userFullName: "User " + course.id, comment: "Comment " + course.id)
        }

        List courseReviewOverviewDTOs = mockReviewsPage.map(mapper::toDTO).getContent()

        when:
        def result = courseReviewService.getReviewsPageOfCourse(courseId, starFilter, page, size)

        then:
        result.content == courseReviewOverviewDTOs
        result.totalPages == 2
        result.totalElements == 7
    }

    def "should throw MapperNotFoundException when mapper is not found"() {
        given:
        courseReviewMapperFactory.getDTOMapper(CourseReviewOverviewDTO.class) >> Optional.empty()

        when:
        def result = courseReviewService.getReviewsPageOfCourse(1, 1, 1, 1)

        then:
        thrown(MapperNotFoundException)
    }

    List<CourseReview> createReviews(int num) {
        return (1..num).collect { new CourseReview(id: it) }
    }

}