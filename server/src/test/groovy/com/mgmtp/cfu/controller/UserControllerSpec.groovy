package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.userdto.ChangePasswordRequest
import com.mgmtp.cfu.dto.userdto.UserDto
import com.mgmtp.cfu.service.impl.UserServiceImpl
import com.mgmtp.cfu.util.Constant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class UserControllerSpec extends Specification {
    
    def userService = Mock(UserServiceImpl)

    @Subject
    UserController controller = new UserController(userService)
    
    def "GetMyProfiles"() {
        given:
        userService.getMyProfile()>> UserDto.builder().id(1).fullName("FULL_NAME").build()
        when:
        def response=controller.getMyProfiles();
        then:
        response.statusCode.value()==200
        response.body.getAt("id")==1
    }

    def "editUserProfile should return ResponseEntity with updated user data"() {
        given:
        UserDto userDto = new UserDto()
        UserDto updatedUserDto = new UserDto()

        when:
        userService.editUserProfile(userDto) >> updatedUserDto
        ResponseEntity<UserDto> response = controller.editUserProfile(userDto)

        then:
        response.body == updatedUserDto
    }


    def "changePassword throws exception when newPassword equals oldPassword"() {
        given:
        def oldPassword = "123"
        def newPassword = "123"
        def request = new ChangePasswordRequest(oldPassword, newPassword)
        def mockResult = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("New password cannot be the same as the old password")

        when:
        def result = controller.changePassword(request)

        then:
        result.statusCode == mockResult.statusCode
        result.body == mockResult.body
    }

    def "Change password successfully"() {
        given:
        def oldPassword = "123"
        def newPassword = "abc"
        def request = new ChangePasswordRequest(oldPassword, newPassword)
        def mockResult = ResponseEntity.status(HttpStatus.OK).body("Change password successfully!")
        userService.changeUserPassword(oldPassword, newPassword) >> 1

        when:
        def result = controller.changePassword(request)

        then:
        result.statusCode == mockResult.statusCode
        result.body == mockResult.body
    }

    def "Change password failed because the old password entered did not match the password in the database"() {
        given:
        def oldPassword = "123"
        def newPassword = "abc"
        def request = new ChangePasswordRequest(oldPassword, newPassword)
        def mockResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password failed!")
        userService.changeUserPassword(oldPassword, newPassword) >> 0

        when:
        def result = controller.changePassword(request)

        then:
        result.statusCode == mockResult.statusCode
        result.body == mockResult.body
    }

    def "getMyScore"(){
        given:
        userService.getMyScoreStatistics()> null;
        when:
        def response=controller.getMyScoreStatistics()
        then:
        response.statusCode.value()==200
    }

    def "test getMyScore with valid year"() {
        given:
        int year = LocalDate.now().getYear()
        userService.getMyScore(String.valueOf(year))>>null

        when:
        ResponseEntity<?> response = controller.getMyScore(year)

        then:
        response.statusCode == HttpStatus.OK

    }

    def "test getMyScore with year below limit"() {
        given:
        int invalidYear = Constant.LEADERBOARD_YEAR_LIMIT - 1
        userService.getMyScore(String.valueOf(_))>>null

        when:
        ResponseEntity<?> response = controller.getMyScore(invalidYear)

        then:
        response.statusCode == HttpStatus.OK

    }

    def "test getMyScore with year above current year"() {
        given:
        int invalidYear = LocalDate.now().getYear()+1
        userService.getMyScore(String.valueOf(_))>>null

        when:
        ResponseEntity<?> response = controller.getMyScore(invalidYear)

        then:
        response.statusCode == HttpStatus.OK
    }

}
