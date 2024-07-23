package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.userdto.MyScoreOfStatisticsDTO;
import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.dto.userdto.UserScore;

public interface IUserService {

    UserDto getMyProfile();

    UserDto editUserProfile(UserDto userDto);

    int changeUserPassword(String oldPassword, String newPassword);

    MyScoreOfStatisticsDTO getMyScoreStatistics();

    UserScore getMyScore(String year);
}
