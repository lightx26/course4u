package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.dto.userdto.MyScoreOfStatisticsDTO;
import com.mgmtp.cfu.dto.userdto.ScorePerYearDTO;
import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.dto.userdto.UserScore;
import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.mapper.UserMapper;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.repository.queries.ScoreQueryManager;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mgmtp.cfu.util.AuthUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final UploadService uploadService;

    private final PasswordEncoder passwordEncoder;
    private final ScoreQueryManager scoreQueryManager;

    @Value("${course4u.upload.profile-directory}")
    private String uploadProfileDir;

    @Override
    public UserDto getMyProfile() {
        var user = getCurrentUser();
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto editUserProfile(UserDto userDto) {

        User user = getCurrentUser();

        user.setFullName(Optional.ofNullable(userDto.getFullName()).orElse("").trim());
        user.setTelephone(Optional.ofNullable(userDto.getTelephone()).orElse("").trim());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());

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
        User user = getCurrentUser();
        boolean checkPassword = passwordEncoder.matches(oldPassword, user.getPassword());
        if (checkPassword) {
            userRepository.changeUserPassword(passwordEncoder.encode(newPassword), user.getId());
            return 1;
        }
        return 0;
    }

    @Override
    public MyScoreOfStatisticsDTO getMyScoreStatistics() {
        var user = getCurrentUser();

        // Get the user's score details for each year from the score manager
        List<ScorePerYearDTO> scoresPanel = scoreQueryManager.getMyScoreStatistics(user.getId());

        // Initialize lists to hold the year, scores, and days data
        var years = new ArrayList<String>();
        var scores = new ArrayList<Long>();
        var days = new ArrayList<Long>();

        // Populate the lists with data from each ScorePerYearDTO
        scoresPanel.forEach(scorePerYearDTO -> {
            var year=scorePerYearDTO.year();
            if(year> LocalDate.now().getYear())
                return;
            years.add(year+"");
            scores.add(scorePerYearDTO.score());
            days.add(scorePerYearDTO.days());
        });

        // Create a Data object using the collected scores and days
        var data = MyScoreOfStatisticsDTO.Data.builder()
                .scores(scores)
                .days(days)
                .build();

        // Build and return the final MyScoreOfStatisticsDTO object with the collected data
        return MyScoreOfStatisticsDTO.builder()
                .label(years)
                .data(data)
                .build();
    }

    @Override
    public UserScore getMyScore(String year) {
        return scoreQueryManager.getMyScore(year, getCurrentUser().getId());
    }


}
