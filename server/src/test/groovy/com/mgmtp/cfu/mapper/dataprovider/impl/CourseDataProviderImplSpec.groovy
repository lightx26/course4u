package com.mgmtp.cfu.mapper.dataprovider.impl

import com.mgmtp.cfu.mapper.dataprovider.CourseDataProvider
import com.mgmtp.cfu.repository.CourseReviewRepository
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.util.RegistrationStatusUtil
import spock.lang.Specification
import spock.lang.Subject

class CourseDataProviderImplSpec extends Specification {
    RegistrationRepository registrationRepository = Mock(RegistrationRepository);
    CourseReviewRepository courseReviewRepository = Mock(CourseReviewRepository);

    @Subject
    CourseDataProvider courseDataProvider = new CourseDataProviderImpl(registrationRepository, courseReviewRepository)

    def "should return number of legit registration in a course"() {
        given:
        int courseId = 1
        int enrollmentCount = 10
        registrationRepository.countRegistrationInCourse(courseId, RegistrationStatusUtil.ACCEPTED_STATUSES) >> enrollmentCount

        when:
        int result = courseDataProvider.countLegitRegistrationInCourse(courseId)

        then:
        result == enrollmentCount
    }

    def "should return average rating of a course"() {
        given:
        int courseId = 1
        double averageRating = 4.5
        courseReviewRepository.calculateAvgRating(courseId) >> averageRating

        when:
        Double result = courseDataProvider.calculateAvgRating(courseId)

        then:
        result == averageRating
    }

    def "should return null when course is not found or not have any rating"() {
        given:
        int courseId = 1
        courseReviewRepository.calculateAvgRating(courseId) >> null

        when:
        Double result = courseDataProvider.calculateAvgRating(courseId)

        then:
        result == null
    }
}
