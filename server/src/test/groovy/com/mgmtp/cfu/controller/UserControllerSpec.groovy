package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.userdto.UserDto
import com.mgmtp.cfu.service.impl.UserServiceImpl
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
}
