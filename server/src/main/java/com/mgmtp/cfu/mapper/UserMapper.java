package com.mgmtp.cfu.mapper;

import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.entity.User;

public class UserMapper {
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
}
