package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.UserDto;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.mapper.UserMapper;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getMyProfile() {
        var user =AuthUtils.getCurrentUser();
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto editUserProfile(UserDto userDto) {
        MultipartFile imageFile = userDto.getImageFile();
        String originalFilename = imageFile.getOriginalFilename();
        int dotIndex = originalFilename.lastIndexOf(".");
        String uniqueFileName = UUID.randomUUID() + "." + originalFilename.substring(dotIndex + 1);

        Path uploadPath = Path.of("uploads/img");
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = AuthUtils.getCurrentUser();
        user.setFullName(userDto.getFullName());
        user.setTelephone(userDto.getTelephone());
        user.setAvatarUrl("/img/" + uniqueFileName);
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

}
