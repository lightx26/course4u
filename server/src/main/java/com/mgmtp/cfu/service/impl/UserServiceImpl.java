package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.mapper.UserMapper;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.service.UploadService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final UploadService uploadService;

    private final PasswordEncoder passwordEncoder;

    @Value("${course4u.upload.profile-directory}")
    private String uploadProfileDir;

    @Override
    public UserDto getMyProfile() {
        var user = AuthUtils.getCurrentUser();
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto editUserProfile(UserDto userDto) {

        User user = AuthUtils.getCurrentUser();

        user.setFullName(Optional.ofNullable(userDto.getFullName()).orElse("").trim());
        user.setTelephone(Optional.ofNullable(userDto.getTelephone()).orElse("").trim());
        user.setDateOfBirth(Optional.ofNullable(userDto.getDateOfBirth()).orElse(null));
        user.setGender(Optional.ofNullable(userDto.getGender()).orElse(null));

        if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty()) {
            String uniqueFileName;
            try {
                uniqueFileName = uploadService.uploadThumbnail(userDto.getImageFile(), uploadProfileDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setAvatarUrl("/profile/" + uniqueFileName);
        } else {
            user.setAvatarUrl("");
        }

        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public int changeUserPassword(String oldPassword, String newPassword) {
        User user = AuthUtils.getCurrentUser();
        boolean checkPassword = passwordEncoder.matches(oldPassword, user.getPassword());
        if (checkPassword) {
            userRepository.changeUserPassword(passwordEncoder.encode(newPassword), user.getId());
            return 1;
        }
        return 0;
    }

}
