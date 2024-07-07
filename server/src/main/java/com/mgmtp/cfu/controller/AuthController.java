package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.LoginRequest;
import com.mgmtp.cfu.service.IAuthService;
import com.mgmtp.cfu.utils.LoginValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    @PostMapping("/login")
    ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest){
        if(!LoginValidator.isValid(loginRequest))
            throw new IllegalArgumentException("Missing required field(s): username, password.");
        var loginResponse=authService.authenticate(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }


}
