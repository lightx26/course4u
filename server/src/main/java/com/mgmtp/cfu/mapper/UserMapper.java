package com.mgmtp.cfu.mapper;


import com.mgmtp.cfu.dto.userdto.UserDto;

import com.mgmtp.cfu.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements DTOMapper<UserDto, User>, EntityMapper<UserDto, User>{
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .dateOfBirth(user.getDateOfBirth())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .telephone(user.getTelephone())
                .build();
    }

    public abstract User toUser(UserDto userDto);
}
