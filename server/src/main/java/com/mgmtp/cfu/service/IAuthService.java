package com.mgmtp.cfu.service;

import com.mgmtp.cfu.DTO.LoginRequest;
import com.mgmtp.cfu.DTO.LoginResponse;

public interface IAuthService {
    LoginResponse authenticate(LoginRequest loginRequest);
}
