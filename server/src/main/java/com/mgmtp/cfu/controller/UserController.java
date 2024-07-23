package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.userdto.ChangePasswordRequest;
import com.mgmtp.cfu.dto.userdto.MyScoreOfStatisticsDTO;
import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.service.IUserService;
import com.mgmtp.cfu.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfiles() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/edit")
    public ResponseEntity<UserDto> editUserProfile(@ModelAttribute UserDto userDto) {
        return ResponseEntity.ok(userService.editUserProfile(userDto));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        if (oldPassword.equals(newPassword))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("New password cannot be the same as the old password");

        int check = userService.changeUserPassword(oldPassword, newPassword);
        if (check == 1)
            return ResponseEntity.status(HttpStatus.OK).body("Change password successfully!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password failed!");
    }

    @GetMapping("/my-score/statistics")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MyScoreOfStatisticsDTO> getMyScoreStatistics() {
        return ResponseEntity.ok(userService.getMyScoreStatistics());
    }
    @GetMapping("/my-score")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getMyScore(@RequestParam("year") int year) {
        var currentYear = LocalDate.now().getYear();
        if(year < Constant.LEADERBOARD_YEAR_LIMIT || year > currentYear)
            year = currentYear;
        return ResponseEntity.ok(userService.getMyScore(String.valueOf(year)));
    }

}
