package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.userdto.UserDto;

public interface IUserService {

    UserDto getMyProfile();

    UserDto editUserProfile(UserDto userDto);

    int changeUserPassword(String oldPassword, String newPassword);

}
