package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.RegistrationFeedback
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.CoursePlatform
import com.mgmtp.cfu.enums.CourseStatus
import com.mgmtp.cfu.enums.DurationUnit
import com.mgmtp.cfu.enums.Gender
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.enums.Role
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


class RegistrationDetailMapperSpec extends Specification {
    @Subject
    RegistrationDetailMapper registrationDetailMapper = Mappers.getMapper(RegistrationDetailMapper.class)

    def "test toDTO"() {
        given:
        def platform = CoursePlatform.UDEMY
        Registration registration = new Registration(
                id: 1L,
                status: RegistrationStatus.APPROVED,
                startDate: LocalDateTime.of(2023, 7, 1,0,0,0).atZone(ZoneId.systemDefault()),
                endDate: LocalDateTime.of(2023, 12, 31,0,0,0).atZone(ZoneId.systemDefault()),
                score: 95,
                registerDate: LocalDate.of(2023, 6, 15),
                duration: 6,
                lastUpdated: LocalDateTime.of(2023, 7, 10, 10, 0),
                durationUnit: DurationUnit.MONTH,
                course: new Course(
                        name: "Test Course",
                        platform: platform,
                        id: 1L,
                        categories: new HashSet<Category>([new Category(id: 1,name: "Test Category")]),
                        link: "Test Link",
                        status: CourseStatus.AVAILABLE,
                        level: CourseLevel.ADVANCED,
                        thumbnailUrl: "Test Thumbnail",
                        createdDate: LocalDate.of(2023, 6, 1),
                        teacherName: "Test Teacher",
                ),
                user: new User(
                        id: 1L,
                        username: "test",
                        email: "test@gmail.com",
                        password: "test",
                        fullName: "Test User",
                        telephone: "0123456789",
                        avatarUrl: "Test Avatar",
                        dateOfBirth: LocalDate.of(1999, 1, 1),
                        role: "USER",
                        gender: "MALE"
                ),
                registrationFeedbacks: new HashSet<RegistrationFeedback>(
                        [new RegistrationFeedback(
                                id: 1,
                                comment: "Test Feedback",
                                createdDate: LocalDateTime.of(2023, 7, 10, 10, 0),
                                user: new User(
                                        id: 1L,
                                        username: "test",
                                        email: "admin",
                                        avatarUrl: "Test Avatar",
                                        role: Role.ADMIN,
                                        fullName: "Admin"
                                )
                        )]
                )
        )
        when:
            RegistrationDetailDTO dto = registrationDetailMapper.toDTO(registration)

        then:
            dto.id == 1L
            dto.status == RegistrationStatus.APPROVED
            dto.startDate == LocalDateTime.of(2023, 7, 1,0,0,0).atZone(ZoneId.systemDefault())
            dto.endDate == LocalDateTime.of(2023, 12, 31,0,0,0).atZone(ZoneId.systemDefault())
            dto.score == 95
            dto.registerDate == LocalDate.of(2023, 6, 15)
            dto.duration == 6
            dto.durationUnit == DurationUnit.MONTH

            def course = dto.course
            course.platform == platform.getLabel()
            course.name == "Test Course"
            course.id == 1L
            course.categories.size() == 1
            course.categories[0].name == "Test Category"
            course.link == "Test Link"
            course.status == CourseStatus.AVAILABLE
            course.level == CourseLevel.ADVANCED
            course.thumbnailUrl == "Test Thumbnail"
            course.createdDate == LocalDate.of(2023, 6, 1)
            course.teacherName == "Test Teacher"

            def user = dto.user
            user.id == 1L
            user.username == "test"
            user.email == "test@gmail.com"
            user.telephone == "0123456789"
            user.avatarUrl == "Test Avatar"
            user.dateOfBirth == LocalDate.of(1999, 1, 1)
            user.role == Role.USER
            user.gender == Gender.MALE

            def feedback = dto.registrationFeedbacks[0]
            feedback.id == 1
            feedback.comment == "Test Feedback"
            feedback.createdDate == LocalDateTime.of(2023, 7, 10, 10, 0)
            feedback.user.username == "test"
            feedback.user.email == "admin"
            feedback.user.avatarUrl == "Test Avatar"
            feedback.user.role == Role.ADMIN
            feedback.user.fullName == "Admin"

            def userFeedback = dto.registrationFeedbacks[0].user
            userFeedback.username == "test"
            userFeedback.email == "admin"
            userFeedback.avatarUrl == "Test Avatar"
            userFeedback.role == Role.ADMIN
            userFeedback.fullName == "Admin"
    }
}
