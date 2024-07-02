package com.mgmtp.cfu.util;

import com.mgmtp.cfu.DTO.LoginRequest;

public class LoginValidator {
    public static boolean isValid(LoginRequest loginRequest) {
        if (loginRequest == null) return false;

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        return username != null && password != null
                && !username.trim().isEmpty()
                && !password.trim().isEmpty();
    }
}
