package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewOverviewDTO
import com.mgmtp.cfu.dto.coursereviewdto.RatingsPage
import org.springframework.data.domain.PageImpl
import com.mgmtp.cfu.dto.coursereviewdto.CourseReviewDto
import com.mgmtp.cfu.entity.CourseReview
import com.mgmtp.cfu.service.impl.CourseReviewServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class CourseReviewControllerSpec extends Specification {
    
    def courseReviewService = Mock(CourseReviewServiceImpl)

    @Subject
    CourseReviewController courseController = new CourseReviewController(courseReviewService)

    def "sendFeedback return savedCourseReview"() {
        given:
        CourseReviewDto courseReviewDto = Mock()
        CourseReview savedCourseReview = Mock()
        courseReviewService.saveReview(courseReviewDto) >> savedCourseReview

        when:
        ResponseEntity<CourseReview> result = courseController.sendReview(courseReviewDto)

        then:
        result.body == savedCourseReview
    }

    def "should return ratings of a course"() {
        given:
        def courseId = 1;
        def ratingsPage = new RatingsPage()
        courseReviewService.getRatingsOfCourse(courseId) >> ratingsPage

        when:
        ResponseEntity<?> response = courseController.getRatingsOfCourse(courseId);

        then:
        response.getStatusCode().value() == 200
        response.getBody() == ratingsPage
    }

    def "should return a page of reviews in a course"() {
        given:
        def courseId = 1
        def starFilter = 3
        def page = 1
        def size = 10
        def reviewsPage = new PageImpl([new CourseReviewOverviewDTO(), new CourseReviewOverviewDTO()])
        courseReviewService.getReviewsPageOfCourse(courseId, starFilter, page, size) >> reviewsPage

        when:
        ResponseEntity<?> response = courseController.getReviewsOfCourse(courseId, starFilter, page, size);

        then:
        response.getStatusCode().value() == 200
        response.getBody() == reviewsPage
    }

}