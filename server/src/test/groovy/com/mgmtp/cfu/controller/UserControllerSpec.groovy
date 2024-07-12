package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.userdto.UserDto
import com.mgmtp.cfu.service.impl.UserServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class UserControllerSpec extends Specification {
    def userService=Mock(UserServiceImpl)

    @Subject
    UserController controller=new UserController(userService)
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

}
