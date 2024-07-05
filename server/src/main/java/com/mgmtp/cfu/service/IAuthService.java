package com.mgmtp.cfu.service;

import com.mgmtp.cfu.dto.LoginRequest;
import com.mgmtp.cfu.dto.LoginResponse;

public interface IAuthService {
    LoginResponse authenticate(LoginRequest loginRequest);
}
