package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.UserDto;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.mapper.UserMapper;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.service.UploadService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UploadService uploadService;

    @Override
    public UserDto getMyProfile() {
        var user =AuthUtils.getCurrentUser();
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto editUserProfile(UserDto userDto) {
        String uniqueFileName;
        try {
            uniqueFileName = uploadService.uploadThumbnail(userDto.getImageFile(), "uploads/img");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = AuthUtils.getCurrentUser();
        user.setFullName(userDto.getFullName());
        user.setTelephone(userDto.getTelephone());
        user.setAvatarUrl("/profile/" + uniqueFileName);
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

}
