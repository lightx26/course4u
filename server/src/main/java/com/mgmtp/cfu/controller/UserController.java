package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.userdto.ChangePasswordRequest;
import com.mgmtp.cfu.dto.userdto.UserDto;
import com.mgmtp.cfu.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
