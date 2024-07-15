package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.authdto.SignUpRequest;
import com.mgmtp.cfu.dto.authdto.SignUpResponse;
import com.mgmtp.cfu.dto.authdto.LoginRequest;
import com.mgmtp.cfu.dto.authdto.LoginResponse;

public interface IAuthService {
    LoginResponse authenticate(LoginRequest loginRequest);

    SignUpResponse handleSignUpNewUser(SignUpRequest signUpRequest);
}
