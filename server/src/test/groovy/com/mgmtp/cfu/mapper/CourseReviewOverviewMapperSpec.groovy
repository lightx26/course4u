package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.entity.CourseReview
import com.mgmtp.cfu.entity.User
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime
import java.time.ZonedDateTime

class CourseReviewOverviewMapperSpec extends Specification {
    @Subject
    CourseReviewOverviewMapper mapper = Mappers.getMapper(CourseReviewOverviewMapper.class)

    def "Should return a null object"() {
        given:
        CourseReview entity = null

        when:
        def dto = mapper.toDTO(entity)

        then:
        entity == null
    }

    def "Should mapping all the fields from entity to DTO correctly"() {
        given:
        CourseReview entity = new CourseReview(id:1, rating: 5, comment: "Peak!", createdAt: ZonedDateTime.now())
        entity.user = new User(fullName: "Quang Nguyen", avatarUrl: "avatarUrl")

        when:
        def dto = mapper.toDTO(entity)

        then:
        dto.userFullName == entity.user.fullName
        dto.userAvatar == entity.user.avatarUrl
        dto.rating == entity.rating
        dto.comment == entity.comment
        dto.createdAt == entity.createdAt
    }
}
