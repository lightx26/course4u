package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.mapper.UserMapper;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    @Override
    public UserDto getMyProfile() {
        var user =AuthUtils.getCurrentUser();
        return UserMapper.toUserDto(user);
    }
}
