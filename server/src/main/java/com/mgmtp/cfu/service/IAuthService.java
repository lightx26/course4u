package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.LoginRequest;
import com.mgmtp.cfu.dto.LoginResponse;
import com.mgmtp.cfu.dto.SignUpRequest;
import com.mgmtp.cfu.dto.SignUpResponse;

public interface IAuthService {
    LoginResponse authenticate(LoginRequest loginRequest);

    SignUpResponse handleSignUpNewUser(SignUpRequest signUpRequest);
}
