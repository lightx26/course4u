package com.mgmtp.cfu.controller;

import com.mgmtp.cfu.dto.authdto.SignUpRequest;
import com.mgmtp.cfu.dto.authdto.LoginRequest;
import com.mgmtp.cfu.service.IAuthService;
import com.mgmtp.cfu.util.LoginValidator;
import com.mgmtp.cfu.util.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        List<String> validationError = SignUpValidator.validateSignUpRequest(signUpRequest);
        if (!validationError.isEmpty()) {
            return ResponseEntity.badRequest().body(validationError);
        }
        var signupResponse = authService.handleSignUpNewUser(signUpRequest);

        return ResponseEntity.ok(signupResponse);
    }




}
