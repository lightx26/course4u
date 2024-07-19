package com.mgmtp.cfu.mapper

import com.mgmtp.cfu.dto.userdto.UserDto
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Gender
import com.mgmtp.cfu.enums.Role
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class UserMapperSpec extends Specification {
    @Subject UserMapper userMapper = Mappers.getMapper(UserMapper.class)
    def "ToUserDto"() {
        given:
            def user = User.builder()
                    .id(1)
                    .fullName("Full Name")
                    .gender(Gender.MALE)
                    .role(Role.USER)
                    .dateOfBirth(LocalDate.now())
                    .avatarUrl("avatar")
                    .telephone("123456789")
                    .build()
        when:
            UserDto userDto = userMapper.toUserDto(user)
        then:
            userDto.id == user.id
            userDto.fullName == user.fullName
            userDto.gender == user.gender
            userDto.role == user.role
            userDto.dateOfBirth == user.dateOfBirth
            userDto.avatarUrl == user.avatarUrl
            userDto.telephone == user.telephone
    }
    def "ToUserDto"() {
        given:
        def user = User.builder()
                .id(1)
                .fullName("Full Name")
                .gender(Gender.MALE)
                .role(Role.USER)
                .dateOfBirth(LocalDate.now())
                .avatarUrl("avatar")
                .telephone("123456789")
                .build()
        when:
        UserDto userDto = userMapper.toDTO(user)
        then:
        userDto.id == user.id
        userDto.fullName == user.fullName
        userDto.gender == user.gender
        userDto.role == user.role
        userDto.dateOfBirth == user.dateOfBirth
        userDto.avatarUrl == user.avatarUrl
        userDto.telephone == user.telephone
    }
    def "ToUser"() {
        given:
            def userDto = UserDto.builder()
                    .id(1)
                    .telephone("123456789")
                    .avatarUrl("avatar")
                    .dateOfBirth(LocalDate.now())
                    .role(Role.USER)
                    .gender(Gender.MALE)
                    .fullName("Full Name")
                    .build()
        when:
            def user = userMapper.toUser(userDto)
        then:
            userDto.id == user.id
            userDto.fullName == user.fullName
            userDto.telephone == user.telephone
            userDto.avatarUrl == user.avatarUrl
            userDto.dateOfBirth == user.dateOfBirth
            userDto.role == user.role
    }
    def "ToUser"() {
        given:
        def userDto = UserDto.builder()
                .id(1)
                .telephone("123456789")
                .avatarUrl("avatar")
                .dateOfBirth(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .fullName("Full Name")
                .build()
        when:
        def user = userMapper.toEntity(userDto)
        then:
        userDto.id == user.id
        userDto.fullName == user.fullName
        userDto.telephone == user.telephone
        userDto.avatarUrl == user.avatarUrl
        userDto.dateOfBirth == user.dateOfBirth
        userDto.role == user.role
    }
}
