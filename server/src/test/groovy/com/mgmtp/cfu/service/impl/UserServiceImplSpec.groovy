package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.userdto.ScorePerYearDTO
import com.mgmtp.cfu.dto.userdto.UserDto
import com.mgmtp.cfu.dto.userdto.UserScore
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Gender
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.repository.queries.ScoreQueryManager
import com.mgmtp.cfu.service.UploadService
import org.springframework.web.multipart.MultipartFile
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import java.time.LocalDate

import static com.mgmtp.cfu.util.AuthUtils.getCurrentUser

class UserServiceImplSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def uploadService = Mock(UploadService)
    def passwordEncoder = Mock(PasswordEncoder)
    ScoreQueryManager scoreQueryManager=Mock()

    @Subject
    def userService = new UserServiceImpl(userRepository, uploadService,
            passwordEncoder,scoreQueryManager)

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

    def cleanup() {
        SecurityContextHolder.clearContext()
    }

    def 'getMyProfile'() {
        given:
        when:
        def userDto = userService.getMyProfile()
        then:
        userDto.getId() == 1
        userDto.fullName == "Jane Doe"
        userDto.username == "janedoe"
        userDto.email == "janedoe@example.com"
        userDto.role == Role.USER
        userDto.avatarUrl == "/aksh/sdh"
        userDto.gender == Gender.FEMALE
        userDto.telephone == "008837623"
    }

    def "editUserProfile update user info and return UserDto"() {
        given:
        def imageFile = Mock(MultipartFile)
        def uploadPath = "uploads/img"
        def userDto = new UserDto(1, "janedoe", "NTN", "janedoe@example.com", "123",
                                  "/img/", LocalDate.now(), Role.USER, Gender.MALE, imageFile)

        when:
        uploadService.uploadThumbnail(imageFile, uploadPath)
        def result = userService.editUserProfile(userDto)

        then:
        result.getId() == userDto.getId()
    }

    def "Change user password successfully and return 1"() {
        given:
        def user = AuthUtils.getCurrentUser()
        def oldPassword = "123"
        def newPassword = "123456"
        passwordEncoder.matches(oldPassword, user.getPassword()) >> true

        when:
        int result = userService.changeUserPassword(oldPassword, newPassword)

        then:
        result == 1
    }

    def "Change user password failed and return 0"() {
        given:
        def user = AuthUtils.getCurrentUser()
        def oldPassword = "123"
        def newPassword = "123456"
        passwordEncoder.matches(oldPassword, user.getPassword()) >> false

        when:
        int result = userService.changeUserPassword(oldPassword, newPassword)

        then:
        result == 0
    }

    def "Change user password failed and return 0"() {
        given:
        scoreQueryManager.getMyScoreStatistics(_ as Long)>>List.of(
                new ScorePerYearDTO(2021,122L,122L),
                new ScorePerYearDTO(2023,122L,122L),
                new ScorePerYearDTO(2022,122L,122L)
        )

        when:
        def result = userService.getMyScoreStatistics()

        then:
        result.label.size()==3
    }
    def "getMyScore"() {
        given:
        scoreQueryManager.getMyScore(_ as String, _ as Long)>> UserScore.builder().build()

        when:
        def result = userService.getMyScore("2021")

        then:
        noExceptionThrown()
    }


}
