package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.UserDto;
import com.mgmtp.cfu.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserDto> editUserProfile(@Valid @ModelAttribute UserDto userDto) {
        return ResponseEntity.ok(userService.editUserProfile(userDto));
    }

}
