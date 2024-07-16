package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.registrationdto.RegistrationDetailDTO
import com.mgmtp.cfu.entity.Category
import com.mgmtp.cfu.entity.Course
import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.CourseLevel
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


class RegistrationDetailMapperSpec extends Specification {
    @Subject
    RegistrationDetailMapper registrationDetailMapper = Mappers.getMapper(RegistrationDetailMapper.class)

    def "test toDTO"() {
        given:
        Registration registration = new Registration(
                id: 1L,
                status: RegistrationStatus.APPROVED,
                startDate: LocalDate.of(2023, 7, 1),
                endDate: LocalDate.of(2023, 12, 31),
                score: 95,
                registerDate: LocalDate.of(2023, 6, 15),
                duration: 6,
                lastUpdated: LocalDateTime.of(2023, 7, 10, 10, 0),
                durationUnit: DurationUnit.MONTH,
                course: new Course(
                        name: "Test Course",
                        platform: "Test Platform",
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

        )

        when:
            RegistrationDetailDTO dto = registrationDetailMapper.toDTO(registration)

        then:
            dto.id == 1L
            dto.status == RegistrationStatus.APPROVED
            dto.startDate == LocalDate.of(2023, 7, 1)
            dto.endDate == LocalDate.of(2023, 12, 31)
            dto.score == 95
            dto.registerDate == LocalDate.of(2023, 6, 15)
            dto.duration == 6
            dto.durationUnit == DurationUnit.MONTH

            def course = dto.course
            course.platform == "Test Platform"
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
    }
}
